package com.assets.portfolio.correlation.entities.statistic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.assets.portfolio.correlation.entities.StatisticList;
import com.assets.portfolio.correlation.exceptions.StatisticListMeanCalculationException;

public class LambdaMultithreadStatisticList extends AbstractStatisticList<BigDecimal> {

    private static final Integer N_THREADS = 4;
    
    private ExecutorService serviceExecutor;

    private int firstQuarter;
    private int secondQuarter;
    private int thirdQuarter;
    private int step;
    
    public LambdaMultithreadStatisticList(List<BigDecimal> origin) {
        super(origin);
        serviceExecutor = Executors.newFixedThreadPool(N_THREADS);
        secondQuarter = list.size() / 2;
        firstQuarter = secondQuarter / 2;
        thirdQuarter = secondQuarter + firstQuarter;
        step = firstQuarter; 
    }

    public BigDecimal computeMean() {
        Future<BigDecimal> firstQuarterMean = serviceExecutor.submit(() -> { return computeMean(0); });
        Future<BigDecimal> secondQuarterMean = serviceExecutor.submit(() -> { return computeMean(firstQuarter); });
        Future<BigDecimal> thirdQuarterMean = serviceExecutor.submit(() -> { return computeMean(secondQuarter); });
        Future<BigDecimal> fourthQuarterMean = serviceExecutor.submit(() -> { return computeMean(thirdQuarter); });
        try {
            return firstQuarterMean.get().add(secondQuarterMean.get()).add(thirdQuarterMean.get()).add(fourthQuarterMean.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new StatisticListMeanCalculationException();
        }
    }
    
    private BigDecimal computeMean(int from){
        List<BigDecimal> filteredList = list.stream().skip(from).limit(step).collect(Collectors.toList());
        return filteredList.stream().reduce((x, y) -> x.add(y)).get().divide(new BigDecimal(list.size()), 5, RoundingMode.HALF_DOWN);
    }

    @Override
    public BigDecimal getHighest() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BigDecimal getLowest() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BigDecimal getStdDev() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BigDecimal getCorrelation(StatisticList<BigDecimal> otherList) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BigDecimal getCovariance(StatisticList<BigDecimal> otherList) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<BigDecimal> getList() {
        // TODO Auto-generated method stub
        return null;
    }

}
