package com.assets.portfolio.data.loader;

import java.util.Map;

import com.assets.portfolio.correlation.entities.StockList;

public interface DataLoader {

    Map<String, StockList> loadData();
    
    Map<String, StockList> loadData(Integer amount);
    
    StockList loadStockList(String ticker);
    
    void setDataFile(String dataFile);
}
