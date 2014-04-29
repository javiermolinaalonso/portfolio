package com.assets.trades.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.assets.portfolio.correlation.entities.stock.StockPrice;
import com.assets.trades.service.BuyStrategy;

public class BuyFixedAmountOfMoney implements BuyStrategy {

    private static final BigDecimal MAX_AMOUNT_TO_BUY = BigDecimal.valueOf(1000d);
    
    @Override
    public Integer getSharesToBuy(StockPrice price) {
        return MAX_AMOUNT_TO_BUY.divide(price.getValue(), 0, RoundingMode.DOWN).intValue();
    }

}
