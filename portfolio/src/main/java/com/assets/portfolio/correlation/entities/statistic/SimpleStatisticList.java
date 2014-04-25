package com.assets.portfolio.correlation.entities.statistic;

import java.math.BigDecimal;
import java.util.List;

import com.assets.portfolio.correlation.entities.StatisticList;

public class SimpleStatisticList extends AbstractStatisticList<BigDecimal> {

    public SimpleStatisticList(List<BigDecimal> origin) {
        super(origin);
        // TODO Auto-generated constructor stub
    }

    public BigDecimal computeMean() {
     // TODO Auto-generated constructor stub
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
