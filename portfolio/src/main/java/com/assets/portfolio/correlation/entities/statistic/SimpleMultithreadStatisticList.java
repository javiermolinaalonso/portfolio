package com.assets.portfolio.correlation.entities.statistic;

import java.math.BigDecimal;
import java.util.List;

import com.assets.portfolio.correlation.entities.StatisticList;

public class SimpleMultithreadStatisticList extends AbstractStatisticList<BigDecimal> {

    public SimpleMultithreadStatisticList(List<BigDecimal> origin) {
        super(origin);
    }

    public BigDecimal computeMean() {
        return null;
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
