package com.assets.portfolio.correlation.entities.statistic;

import java.math.BigDecimal;
import java.util.List;

import com.assets.portfolio.correlation.entities.StatisticList;

public abstract class AbstractStatisticList<T> implements StatisticList<BigDecimal> {

    protected final List<T> list;
    
    public AbstractStatisticList(List<T> origin){
        this.list = origin;
    }
 
    public BigDecimal getMean() {
        if(list.isEmpty()){
            return BigDecimal.ZERO;
        }
        return computeMean();
    }

    protected abstract BigDecimal computeMean();
}
