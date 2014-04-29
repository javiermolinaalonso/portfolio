package com.assets.trades.service;

import com.assets.portfolio.correlation.entities.stock.StockPrice;

public interface BuyStrategy {

    public Integer getSharesToBuy(StockPrice price);
    
}
