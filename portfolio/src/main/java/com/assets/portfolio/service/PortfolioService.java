package com.assets.portfolio.service;

import com.assets.options.impl.VolatilityCalculator;
import com.assets.portfolio.entities.Portfolio;
import com.assets.statistic.list.StockList;
import com.assets.statistics.entities.CorrelationTwoStocks;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class PortfolioService {

    private final VolatilityCalculator volatilityCalculator;

    public PortfolioService(VolatilityCalculator volatilityCalculator) {
        this.volatilityCalculator = volatilityCalculator;
    }

    public Portfolio buildPortfolio(Portfolio originalPortfolio, Set<String> subsetTickers) {
        return buildPortfolio(subsetTickers.stream().collect(toMap(Function.identity(), ticker -> originalPortfolio.get(ticker).orElse(null))),
                originalPortfolio.getFrom().atStartOfDay(), originalPortfolio.getTo().atStartOfDay());
    }

    public Portfolio buildPortfolio(Map<String, StockList> stringStockListMap, LocalDateTime from, LocalDateTime to) {
        List<StockList> values = stringStockListMap.entrySet().stream().map(Map.Entry::getValue).collect(toList());

        return Portfolio.PortfolioBuilder.aPortfolio()
                .withRawData(stringStockListMap)
                .withFrom(from.toLocalDate())
                .withTo(to.toLocalDate())
                .withCorrelation(computeCorrelations(from, to, values))
                .withPerformance(calculatePerformance(values))
                .withVolatility(calculateVolatility(values))
                .build();
    }

    private Map<String, BigDecimal> calculateVolatility(List<StockList> values) {
        return values.stream().collect(toMap(StockList::getTicker, x -> BigDecimal.valueOf(volatilityCalculator.getAnnualizedVolatility(x))));
    }

    private Map<String, BigDecimal> calculatePerformance(List<StockList> values) {
        return values.stream().collect(toMap(StockList::getTicker, StockList::getPerformance));
    }

    private List<CorrelationTwoStocks> computeCorrelations(LocalDateTime from, LocalDateTime to, List<StockList> values) {
        List<CorrelationTwoStocks> result = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            for (int j = i + 1; j < values.size(); j++) {
                final StockList firstStockList = values.get(i);
                final StockList secondStockList = values.get(j);
                result.add(new CorrelationTwoStocks(firstStockList, secondStockList, from, to));
            }
        }
        return result;
    }


}
