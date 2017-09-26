package com.assets.portfolio.data.loader;

import com.assets.statistic.list.StockList;

import java.util.Map;


public interface DataLoader {

    Map<String, StockList> loadData();

    StockList loadData(String ticker);
    
    void setDataFile(String dataFile);
}
