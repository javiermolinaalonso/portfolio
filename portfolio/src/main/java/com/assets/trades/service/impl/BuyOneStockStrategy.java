package com.assets.trades.service.impl;

import com.assets.entities.StockPrice;
import com.assets.trades.service.BuyStrategy;

public class BuyOneStockStrategy implements BuyStrategy {

    @Override
    public Integer getSharesToBuy(StockPrice price) {
        return 1;
    }

}
