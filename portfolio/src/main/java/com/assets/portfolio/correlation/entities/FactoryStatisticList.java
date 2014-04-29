package com.assets.portfolio.correlation.entities;

import java.math.BigDecimal;
import java.util.List;

import com.assets.portfolio.correlation.entities.enums.StatisticListType;
import com.assets.portfolio.correlation.entities.statistic.LambdaMultithreadStatisticList;
import com.assets.portfolio.correlation.entities.statistic.LambdaStatisticList;
import com.assets.portfolio.correlation.entities.statistic.SimpleMultithreadStatisticList;
import com.assets.portfolio.correlation.entities.statistic.SimpleStatisticList;

public class FactoryStatisticList {

    public static StatisticList<BigDecimal> getStatisticList(List<BigDecimal> list) {
        return getStatisticList(list, StatisticListType.LAMBDA);
    }
    public static StatisticList<BigDecimal> getStatisticList(List<BigDecimal> list, StatisticListType type) {
        switch(type){
        case LAMBDA:
            return new LambdaStatisticList(list);
        case LAMBDA_MULTI:
            return new LambdaMultithreadStatisticList(list);
        case MULTI:
            return new SimpleMultithreadStatisticList(list);
        case SINGLE:
            return new SimpleStatisticList(list);
        default:
            break;
        
        }
        return new LambdaStatisticList(list);
    }

}
