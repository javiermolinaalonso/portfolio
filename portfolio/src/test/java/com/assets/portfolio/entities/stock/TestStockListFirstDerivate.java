package com.assets.portfolio.entities.stock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.Instant;

import com.assets.entities.StockPrice;
import com.assets.statistic.list.StockList;
import org.junit.Before;
import org.junit.Test;


public class TestStockListFirstDerivate {
private final String TICKER = "TEST";
    
    private StockList list;
    
    @Before
    public void setUp() throws Exception {
        list = new StockList(TICKER);
    }

    @Test
    public void testGetMeanWithSameNumber() {
        list.add(new StockPrice(TICKER, Instant.ofEpochSecond(1000), new BigDecimal(10)));
        list.add(new StockPrice(TICKER, Instant.ofEpochSecond(1001), new BigDecimal(11)));
        list.add(new StockPrice(TICKER, Instant.ofEpochSecond(1002), new BigDecimal(12)));
        list.add(new StockPrice(TICKER, Instant.ofEpochSecond(1003), new BigDecimal(12)));
        list.add(new StockPrice(TICKER, Instant.ofEpochSecond(1004), new BigDecimal(9)));
        
        StockList derivateList = list.getFirstDerivate();
        assertEquals(1, derivateList.get(0).getValue().intValue());
        assertEquals(1, derivateList.get(1).getValue().intValue());
        assertEquals(0, derivateList.get(2).getValue().intValue());
        assertEquals(-3, derivateList.get(3).getValue().intValue());
        assertEquals(Instant.ofEpochSecond(1001).toEpochMilli(), derivateList.get(0).getInstant().toEpochMilli());
        assertEquals(Instant.ofEpochSecond(1004).toEpochMilli(), derivateList.get(3).getInstant().toEpochMilli());
    }
}
