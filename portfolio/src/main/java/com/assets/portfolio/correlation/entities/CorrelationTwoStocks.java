package com.assets.portfolio.correlation.entities;

import com.assets.entities.StockPrice;
import com.assets.portfolio.correlation.entities.statistic.LambdaStatisticList;
import com.assets.portfolio.correlation.exceptions.EmptyStatisticListException;
import com.assets.portfolio.correlation.exceptions.InvalidCorrelationDatesException;
import com.assets.statistic.list.StockList;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

public final class CorrelationTwoStocks {

    private final StockList s1;
    private final StockList s2;
    private final Instant from;
    private final Instant to;

    private StockCorrelation correlation;

    public CorrelationTwoStocks(StockList s1, StockList s2, LocalDateTime from, LocalDateTime to) {
        this(s1, s2, from.toInstant(ZoneOffset.UTC), to.toInstant(ZoneOffset.UTC));
    }

    public CorrelationTwoStocks(StockList s1, StockList s2, Instant from, Instant to) {
        super();
        this.s1 = s1;
        this.s2 = s2;
        this.from = from;
        this.to = to;
    }

    public StockList getS1() {
        return this.s1;
    }

    public StockList getS2() {
        return this.s2;
    }

    public Instant getFrom() {
        return this.from;
    }

    public Instant getTo() {
        return this.to;
    }

    public StockCorrelation calculateCorrelation() {
        if (correlation == null) {
            if (s1 == null || s2 == null) {
                throw new EmptyStatisticListException();
            }
            if (from.isAfter(to)) {
                throw new InvalidCorrelationDatesException();
            }
            StockList firstListFiltered = s1.filterStocksAndSort(s2, from, to);
            StockList secondListFiltered = s2.filterStocksAndSort(s1, from, to);
            LambdaStatisticList firstStockValues = new LambdaStatisticList(firstListFiltered.stream().map(StockPrice::getValue).collect(Collectors.toList()));
            LambdaStatisticList secondStockValues = new LambdaStatisticList(secondListFiltered.stream().map(StockPrice::getValue).collect(Collectors.toList()));
            correlation = new StockCorrelation(s1.getTicker(), s2.getTicker(), firstStockValues.getCorrelation(secondStockValues), firstListFiltered.getFirst().getInstant(), firstListFiltered.getLast().getInstant(), firstListFiltered.size(), firstStockValues, secondStockValues);
        }

        return correlation;
    }

}
