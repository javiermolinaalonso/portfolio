package com.assets.portfolio.correlation;

import java.util.List;
import java.util.Map;

import com.assets.portfolio.correlation.entities.CorrelationIntervalInputData;
import com.assets.portfolio.correlation.entities.CorrelationTwoStocks;
import com.assets.portfolio.correlation.entities.StockCorrelation;
import com.assets.portfolio.correlation.entities.investment.InvestmentResult;

public interface CorrelationService {

    Map<CorrelationIntervalInputData, List<StockCorrelation>> computeCorrelationBetweenTwoStocks(CorrelationTwoStocks inputData, Iterable<CorrelationIntervalInputData> intervals);
    
    List<StockCorrelation> calculateBestIntervalCorrelationInDateRangeSortedByDate(CorrelationTwoStocks stockData, CorrelationIntervalInputData intervalData);
    
    public InvestmentResult calculateCorrelationAlerts(List<StockCorrelation> correlations, CorrelationTwoStocks stockData, CorrelationIntervalInputData inputDataInterval);
}
