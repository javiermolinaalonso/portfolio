package com.assets.portfolio.data.loader.mysql;

import com.assets.entities.StockPrice;
import com.assets.portfolio.data.loader.DataLoader;
import com.assets.portfolio.data.loader.mysql.entities.Tables;
import com.assets.portfolio.data.loader.mysql.entities.tables.DailyQuotes;
import com.assets.statistic.list.StockList;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MySqlDataLoader implements DataLoader {

    private final DataLoader defaultLoader;
    private final Connection connection;
    private final String database;

    public MySqlDataLoader(DataLoader defaultLoader, Connection connection, String database) {
        this.defaultLoader = defaultLoader;
        this.connection = connection;
        this.database = database;
    }

    @Override
    public Map<String, StockList> loadData() {
        return null;
    }

    @Override
    public StockList loadData(String ticker) {
        DSLContext context = DSL.using(connection, SQLDialect.MYSQL);
        Result<Record> result = context.select().from(DailyQuotes.DAILY_QUOTES).fetch();
        if (result.isEmpty()) {
            final StockList stockPrices = defaultLoader.loadData(ticker);
            final Random r = new Random(System.currentTimeMillis());
            stockPrices.stream().forEach(stockPrice -> {
                final Date date = new Date(LocalDateTime.ofInstant(stockPrice.getInstant(), ZoneId.systemDefault()).withHour(0).withMinute(0).withSecond(0).withNano(0).toInstant(ZoneOffset.UTC).toEpochMilli());
                context.insertInto(Tables.DAILY_QUOTES)
                        .set(DailyQuotes.DAILY_QUOTES.ID, r.nextInt())
                        .set(DailyQuotes.DAILY_QUOTES.TICKER, stockPrice.getTicker())
                        .set(DailyQuotes.DAILY_QUOTES.DATE, date)
                        .set(DailyQuotes.DAILY_QUOTES.VALUE, stockPrice.getValue().doubleValue())
                        .execute();
            });
            result = context.select().from(DailyQuotes.DAILY_QUOTES).fetch();
        }

        final List<StockPrice> stockPrices = result.map(r -> {
            final String t = r.getValue(DailyQuotes.DAILY_QUOTES.TICKER);
            Instant instant = new java.util.Date(r.getValue(DailyQuotes.DAILY_QUOTES.DATE).getTime()).toInstant();
            final BigDecimal value = BigDecimal.valueOf(r.getValue(DailyQuotes.DAILY_QUOTES.VALUE));
            return new StockPrice(t, instant, value);
        });
        return new StockList(stockPrices, ticker);
    }

    @Override
    public void setDataFile(String dataFile) {

    }
}
