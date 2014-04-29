package com.assets.portfolio.correlation.entities.stock;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.assets.portfolio.correlation.entities.FactoryStatisticList;
import com.assets.portfolio.correlation.entities.statistic.LambdaStatisticList;
import com.assets.portfolio.correlation.exceptions.StockListMeanParameterException;

public class StockList extends LinkedList<StockPrice> {

    private static final long serialVersionUID = 4376347809602759884L;
    private final String ticker;
    
    public StockList(String ticker){
        this(new ArrayList<StockPrice>(), ticker);
    }
    public StockList(List<StockPrice> origin, String ticker){
        super(origin.stream().sorted( (y, z) -> y.getInstant().compareTo(z.getInstant()) ).collect(Collectors.toList()));
        this.ticker = ticker;
    }
    
    public StockPrice getByInstant(final Instant instant){
        if(stream().anyMatch( x -> x.getInstant().equals(instant) )) {
            return stream().filter(x -> x.getInstant().equals(instant)).iterator().next();
        }else{
            return null;
        }
    }
    public StockList filterStocksAndSort(StockList secondStock) {
        return filterStocksAndSort(secondStock, null, null);
    }
    
    public StockList filterStocksAndSort(StockList secondStock, Instant from, Instant to) {
        List<Instant> secondStockInstants = secondStock.stream().map(y -> y.getInstant()).collect(Collectors.toList());
        return new StockList(parallelStream()
                .filter(x -> secondStockInstants.contains(x.getInstant()))
                .filter(x -> from != null ? x.getInstant().compareTo(from) >= 0 : true)
                .filter(x -> from != null ? x.getInstant().compareTo(to) <= 0 : true)
                .sequential()
                .sorted((y, z) -> y.getInstant().compareTo(z.getInstant()))
                .collect(Collectors.toList()), getTicker());
    }
    
    public StockList getMeanWeek(){
        return getMean(7);
    }
    
    public StockList getMeanMonth(){
        return getMean(30);
    }
    
    public StockList getMean(int sessions){
        if(sessions <= 0){
            throw new StockListMeanParameterException();
        }
        StockList list = new StockList(getTicker());
        for(int i = 0; i <= size() - sessions; i++){
            LambdaStatisticList stList = new LambdaStatisticList(stream().skip(i).limit(sessions).map(x -> x.getValue()).collect(Collectors.toList()));
            BigDecimal mean = stList.getMean();
            list.add(new StockPrice(getTicker(), get(i + sessions - 1).getInstant(), mean));
        }
        return list;
    }
    
    public BigDecimal getStdDevLastSessions(int amountOfSessions, int indexOfLastSession){
        int from = Math.max(indexOfLastSession - amountOfSessions, 0);
        List<BigDecimal> values = stream().skip(from).limit(amountOfSessions).map(x -> x.getValue()).collect(Collectors.toList());
        return FactoryStatisticList.getStatisticList(values).getStdDev();
    }
    
    public StockList getFirstDerivate() {
        StockList derivateList = new StockList(getTicker());
        for(int i = 1; i < size(); i++){
            BigDecimal firstValue = get(i-1).getValue();
            BigDecimal secondValue = get(i).getValue();
            derivateList.add(new StockPrice(getTicker(), get(i).getInstant(), secondValue.subtract(firstValue)));
        }
        return derivateList;
    }
    
    public String getTicker() {
        return this.ticker;
    }
    
}
