package com.assets.portfolio.pick;

import com.assets.portfolio.Portfolio;
import com.assets.portfolio.PortfolioService;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PortfolioStockPickerService {

    private final PortfolioService portfolioService;

    public PortfolioStockPickerService(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    public List<Portfolio> pick(Portfolio portfolio, PickParameters params) {
        final int maxStocks = params.getMaxStocks();
        final Set<Set<String>> sets = Sets.powerSet(portfolio.getTickers()).stream().filter(s -> s.size() <= maxStocks).collect(Collectors.toSet());

        List<Portfolio> portfolios = new ArrayList<>();
        sets.parallelStream().forEach(set -> {
            final Portfolio subsetPortfolio = portfolioService.buildPortfolio(portfolio, set);
            if(filter(subsetPortfolio, params)) {
                portfolios.add(subsetPortfolio);
                subsetPortfolio.printGlobals();
            }
        });

        return portfolios;
    }

    private boolean filter(Portfolio portfolio, PickParameters params) {
        boolean success = isSuccess(portfolio::getGlobalCorrelation, params::getMaxCorrelation, (x, y) -> x.doubleValue() < y, true);
        success = isSuccess(portfolio::getGlobalPerformance, params::getMaxPerformance, (x, y) -> x.doubleValue() < y, success);
        success = isSuccess(portfolio::getGlobalPerformance, params::getMinPerformance, (x, y) -> x.doubleValue() > y, success);
        success = isSuccess(portfolio::getGlobalVolatility, params::getMaxVolatility, (x, y) -> x.doubleValue() < y, success);
        return success;
    }

    private <T, R> boolean isSuccess(Supplier<T> pValue, Supplier<Optional<R>> param, BiFunction<T, R, Boolean> operation, boolean success) {
        return success &&
                (
                        !param.get().isPresent() || operation.apply(pValue.get(), param.get().get())
                );
    }
}
