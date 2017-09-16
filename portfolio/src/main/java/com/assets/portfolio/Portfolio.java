package com.assets.portfolio;

import com.assets.portfolio.correlation.entities.CorrelationTwoStocks;
import com.assets.statistic.list.StockList;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

public class Portfolio {

    private final Map<String, StockList> rawData;
    private final Set<String> tickers;
    private final List<CorrelationTwoStocks> correlation;
    private final Map<String, BigDecimal> volatility;
    private final Map<String, BigDecimal> performance;
    private final LocalDate from;
    private final LocalDate to;

    public Portfolio(PortfolioBuilder builder) {
        this.rawData = builder.rawData;
        this.performance = builder.performance;
        this.from = builder.from;
        this.to = builder.to;
        this.correlation = builder.correlation;
        this.volatility = builder.volatility;
        this.tickers = rawData.entrySet().stream().map(Map.Entry::getKey).collect(toSet());
    }

    public Set<String> getTickers() {
        return tickers;
    }

    public List<CorrelationTwoStocks> getCorrelation() {
        return correlation;
    }

    public Map<String, BigDecimal> getVolatility() {
        return volatility;
    }

    public Map<String, BigDecimal> getPerformance() {
        return performance;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    public BigDecimal getGlobalVolatility() {
        return BigDecimal.valueOf(volatility.values().stream().mapToDouble(BigDecimal::doubleValue).average().orElse(0)).setScale(4, RoundingMode.CEILING);
    }

    public BigDecimal getGlobalPerformance() {
        return BigDecimal.valueOf(performance.values().stream().mapToDouble(BigDecimal::doubleValue).average().orElse(0)).setScale(4, RoundingMode.CEILING);
    }

    public BigDecimal getGlobalCorrelation() {
        return BigDecimal.valueOf(correlation.stream().mapToDouble(x -> Math.abs(x.calculateCorrelation().getCorrelation().doubleValue())).average().orElse(0)).setScale(4, RoundingMode.CEILING);
    }

    public Optional<StockList> get(String ticker) {
        return Optional.ofNullable(rawData.get(ticker));
    }

    public void printGlobals() {
        System.out.println(String.format("Portfolio composed by %s from %s to %s" ,tickers.stream().collect(joining(",", "[", "]")), from.toString(), to.toString()));
        System.out.println(String.format("Global correlation: %s", getGlobalCorrelation()));
        System.out.println(String.format("Global volatility: %s%%", percent(getGlobalVolatility())));
        System.out.println(String.format("Global performance: %s%%", percent(getGlobalPerformance())));
    }

    public void printDetails() {
        getPerformance().forEach((k, v) -> System.out.println(String.format("Performance %s: %s%%", k, percent(v))));
        getVolatility().forEach((k, v) -> System.out.println(String.format("Volatility %s: %s%%", k, percent(v))));
        getCorrelation().forEach(x -> System.out.println(String.format("Correlation %s - %s: %s", x.getS1().getTicker(), x.getS2().getTicker(), x.calculateCorrelation().getCorrelation())));
    }

    private String percent(BigDecimal v) {
        return v.multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_CEILING).toString();
    }

    public void print() {
        printGlobals();
        printDetails();
    }

    public static final class PortfolioBuilder {
        private List<CorrelationTwoStocks> correlation;
        private Map<String, BigDecimal> volatility;
        private Map<String, BigDecimal> performance;
        private LocalDate from;
        private LocalDate to;
        private Map<String, StockList> rawData;

        private PortfolioBuilder() {
        }

        public static PortfolioBuilder aPortfolio() {
            return new PortfolioBuilder();
        }

        public PortfolioBuilder withCorrelation(List<CorrelationTwoStocks> correlation) {
            this.correlation = correlation;
            return this;
        }

        public PortfolioBuilder withVolatility(Map<String, BigDecimal> volatility) {
            this.volatility = volatility;
            return this;
        }

        public PortfolioBuilder withPerformance(Map<String, BigDecimal> performance) {
            this.performance = performance;
            return this;
        }

        public PortfolioBuilder withFrom(LocalDate from) {
            this.from = from;
            return this;
        }

        public PortfolioBuilder withTo(LocalDate to) {
            this.to = to;
            return this;
        }

        public PortfolioBuilder withRawData(Map<String, StockList> data) {
            this.rawData = data;
            return this;
        }

        public Portfolio build() {
            return new Portfolio(this);
        }
    }
}
