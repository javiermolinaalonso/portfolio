package com.assets.portfolio.data.loader.yahoo;

import com.assets.entities.StockPrice;
import com.assets.portfolio.data.loader.DataLoader;
import com.assets.statistic.list.StockList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.*;

public class YahooDataLoader implements DataLoader {

    private static final Logger logger = LoggerFactory.getLogger(YahooDataLoader.class);

    private static final String BASE_URL = "https://l1-query.finance.yahoo.com/v8/finance/chart/";

    private final List<String> tickers;
    private final LocalDateTime to;
    private final LocalDateTime from;
    private final YahooDataPeriod period;

    public YahooDataLoader(List<String> ticker, LocalDateTime from, LocalDateTime to) {
        this(ticker, from, to, YahooDataPeriod.ONE_DAY);
    }

    private YahooDataLoader(List<String> ticker, LocalDateTime from, LocalDateTime to, YahooDataPeriod period) {
        this.tickers = ticker;
        this.from = from;
        this.to = to;
        this.period = period;
    }

    public YahooDataLoader(String ticker, LocalDateTime from, LocalDateTime to, YahooDataPeriod period) {
        this(Collections.singletonList(ticker), from, to, period);
    }

    @Override
    public Map<String, StockList> loadData() {
        Map<String, StockList> result = new HashMap<>();

        ExecutorService service =  Executors.newCachedThreadPool();
        for (String ticker : tickers) {
            final Future<StockList> listFuture = service.submit(() -> loadData(ticker));
            try {
                result.put(ticker, listFuture.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private String buildUrl(String ticker) {
        return String.format("%s%s?period2=%s&period1=%s&interval=%s&indicators=quote&includeTimestamps=true&includePrePost=true", BASE_URL, ticker, getMillis(from), getMillis(to), period.getYahoovalue());
    }

    private String getMillis(LocalDateTime to) {
        return String.valueOf(to.toEpochSecond(ZoneOffset.UTC));
    }

    @Override
    public Map<String, StockList> loadData(Integer integer) {
        return null;
    }

    @Override
    public StockList loadData(String ticker) {
        System.out.println(String.format("Downloading data for %s", ticker));
        final String url = buildUrl(ticker);
        final YahooData data = new RestTemplate().getForObject(url, YahooData.class);
        final YahooChartElement result = data.getChart().getResult().get(0);

        List<StockPrice> prices = new ArrayList<>();
        final List<Integer> timestamp = result.getTimestamp();
        final List<Double> adjclose = result.getIndicators().getQuote().get(0).get("close");
        for (int i = 0; i < timestamp.size(); i++) {
            try {
                prices.add(new StockPrice(ticker, normalizeInstant(timestamp.get(i)), BigDecimal.valueOf(adjclose.get(i))));
            } catch (NullPointerException e) {
                logger.warn("Ticker {} at position {} has null value", ticker, i);
            }
        }

        return new StockList(prices, ticker);
    }

    private Instant normalizeInstant(Integer second) {
        final Instant instant = Instant.ofEpochSecond(second);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).withHour(0).withMinute(0).withSecond(0).withNano(0).toInstant(ZoneOffset.UTC);
    }

    @Override
    public void setDataFile(String s) {

    }

}
