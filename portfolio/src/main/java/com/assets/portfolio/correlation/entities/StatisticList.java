package com.assets.portfolio.correlation.entities;

import java.math.BigDecimal;
import java.util.List;


public interface StatisticList<T> {

    public T getMean();
    
    public T getHighest();
    
    public T getLowest();
    
    public T getStdDev();
    
    public T getCorrelation(StatisticList<T> otherList);
    
    public T getCovariance(StatisticList<T> otherList);
    
    public List<BigDecimal> getList();
}
