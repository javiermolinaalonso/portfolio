package com.assets.trades.service;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.Before;
import org.junit.Test;

import com.assets.portfolio.correlation.entities.stock.StockPrice;
import com.assets.trades.service.impl.BuyFixedAmountOfMoney;

public class TestBuyFixedAmountOfMoney {

    BuyStrategy strategy;
    @Before
    public void setUp() throws Exception {
        this.strategy = new BuyFixedAmountOfMoney();
    }

    @Test
    public void testNearLower() {
        assertEquals(39, strategy.getSharesToBuy(new StockPrice("T", Instant.now(), BigDecimal.valueOf(25.61d))).intValue());
    }

    @Test
    public void testNearHigher() {
        assertEquals(39, strategy.getSharesToBuy(new StockPrice("T", Instant.now(), BigDecimal.valueOf(25.1d))).intValue());
    }
}
