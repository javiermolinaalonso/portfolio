package com.assets.portfolio.pick;

import java.util.Optional;

public class PickParameters {

    private final int maxStocks;
    private final Double maxCorrelation;
    private final Double maxVolatility;
    private final Double minPerformance;
    private final Double maxPerformance;

    private PickParameters (Builder builder) {
        this.maxStocks = builder.maxStocks;
        this.maxCorrelation = builder.maxCorrelation;
        this.maxVolatility = builder.maxVolatility;
        this.minPerformance = builder.minPerformance;
        this.maxPerformance = builder.maxPerformance;
    }

    public int getMaxStocks() {
        return maxStocks;
    }

    public Optional<Double> getMaxCorrelation() {
        return Optional.ofNullable(maxCorrelation);
    }

    public Optional<Double> getMaxVolatility() {
        return Optional.ofNullable(maxVolatility);
    }

    public Optional<Double> getMinPerformance() {
        return Optional.ofNullable(minPerformance);
    }

    public Optional<Double> getMaxPerformance() {
        return Optional.ofNullable(maxPerformance);
    }

    public static final class Builder {
        private int maxStocks;
        private Double maxCorrelation;
        private Double maxVolatility;
        private Double minPerformance;
        private Double maxPerformance;

        private Builder() {
        }

        public static Builder aPickParameters() {
            return new Builder();
        }

        public Builder withMaxStocks(int maxStocks) {
            this.maxStocks = maxStocks;
            return this;
        }

        public Builder withMaxCorrelation(Double maxCorrelation) {
            this.maxCorrelation = maxCorrelation;
            return this;
        }

        public Builder withMaxVolatility(Double maxVolatility) {
            this.maxVolatility = maxVolatility;
            return this;
        }

        public Builder withMinPerformance(Double minPerformance) {
            this.minPerformance = minPerformance;
            return this;
        }

        public Builder withMaxPerformance(Double maxPerformance) {
            this.maxPerformance = maxPerformance;
            return this;
        }

        public PickParameters build() {
            return new PickParameters(this);
        }
    }
}
