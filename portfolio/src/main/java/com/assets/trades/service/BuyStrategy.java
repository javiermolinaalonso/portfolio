package com.assets.trades.service;

import com.assets.entities.StockPrice;

public interface BuyStrategy {

    Integer getSharesToBuy(StockPrice price);
    
}
