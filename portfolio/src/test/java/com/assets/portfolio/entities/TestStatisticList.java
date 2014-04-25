package com.assets.portfolio.entities;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.assets.portfolio.correlation.entities.FactoryStatisticList;
import com.assets.portfolio.correlation.entities.StatisticList;
import com.assets.portfolio.correlation.entities.enums.StatisticListType;
import com.assets.portfolio.correlation.entities.statistic.LambdaStatisticList;

public class TestStatisticList {

    private StatisticList<BigDecimal> sList;
    private List<BigDecimal> prices;
    
    @Before
    public void setUp() throws Exception {
        prices = new ArrayList<BigDecimal>();
        prices.add(BigDecimal.valueOf(10d));
        prices.add(BigDecimal.valueOf(11d));
        prices.add(BigDecimal.valueOf(12d));
        prices.add(BigDecimal.valueOf(13d));
        prices.add(BigDecimal.valueOf(14d));
        sList = FactoryStatisticList.getStatisticList(prices, StatisticListType.LAMBDA_MULTI);
    }

    @Test
    public void testMean() {
        assertEquals(12d, sList.getMean().doubleValue(), 0.001d);
    }
    
    @Test
    public void testMeanEmptyList(){
        assertEquals(0d, sList.getMean().doubleValue(), 0.00d);
    }
    
    @Test
    public void testMaximum() {
        assertEquals(BigDecimal.valueOf(12d), sList.getHighest());
    }
    
    @Test
    public void testMinimum() {
        assertEquals(BigDecimal.valueOf(10d), sList.getLowest());
    }
    
    @Test
    public void testStdDev() {
        assertEquals(0.748d, sList.getStdDev().doubleValue(), 0.001d);
    }
}
