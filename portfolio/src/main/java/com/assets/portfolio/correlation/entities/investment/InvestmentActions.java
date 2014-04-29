package com.assets.portfolio.correlation.entities.investment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.assets.portfolio.correlation.entities.stock.StockList;
import com.assets.portfolio.correlation.entities.stock.StockPrice;
import com.assets.trades.service.BuyStrategy;

public class InvestmentActions {
    
    private BigDecimal benefit;
    private BigDecimal percentBenefit;
    private List<InvestmentAction> actions;
    private InvestmentActions actionsPendingToClose;
    private boolean benefitsUpdated;
    private BigDecimal maxAmountInvested;
    
    public InvestmentActions(){
        this.actions = new ArrayList<InvestmentAction>();
        this.benefit = BigDecimal.ZERO;
        this.percentBenefit = BigDecimal.ZERO;
        this.maxAmountInvested = BigDecimal.ZERO;
        this.actionsPendingToClose = null;
        benefitsUpdated = true;
    }
    
    public InvestmentActions(StockList entryPoints, StockList exitPoints, BuyStrategy strategy){
        this();
        if(entryPoints != null && !entryPoints.isEmpty()){
            StockList allActions = new StockList(entryPoints.getTicker());
            allActions.addAll(entryPoints);
            allActions.addAll(exitPoints);
            allActions.sort( (x, y) -> x.getInstant().compareTo(y.getInstant()) );
            int currentStocksInPossession = 0;
            for(StockPrice price : allActions){
                if(entryPoints.contains(price)){
                    Integer stocksToBuy = strategy.getSharesToBuy(price);
                    add(new InvestmentAction(price, InvestmentActionEnum.BUY, stocksToBuy));
                    currentStocksInPossession+=stocksToBuy;
                }else{
                    add(new InvestmentAction(price, InvestmentActionEnum.SELL, currentStocksInPossession));
                    currentStocksInPossession = 0;
                }
            }
        }
    }
    
    public InvestmentActions addAll(List<InvestmentAction> actions2) {
        actions2.forEach(x -> add(x));
        return this;
    }
    
    public InvestmentActions add(InvestmentAction action){
        this.actions.add(action);
        benefitsUpdated = false;
        return this;
    }
    
    private void updateBenefit(){
        benefit = BigDecimal.ZERO;
        percentBenefit = BigDecimal.ZERO;
        actionsPendingToClose = new InvestmentActions();
        
        computeBenefit(actions);
        
        benefitsUpdated = true;
    }

    private void computeBenefit(List<InvestmentAction> actionsTicker) {
        BigDecimal tickerBenefit = BigDecimal.ZERO;
        BigDecimal roundInvestedAmount = BigDecimal.ZERO;
        BigDecimal lastSellBenefit = BigDecimal.ZERO;
        
        for(InvestmentAction action : actionsTicker){
            tickerBenefit = tickerBenefit.add(action.getAmountInvested().negate());
            if(InvestmentActionEnum.SELL.equals(action.getAction())){
                lastSellBenefit = tickerBenefit;
                maxAmountInvested = roundInvestedAmount.compareTo(maxAmountInvested) > 0 ? roundInvestedAmount : maxAmountInvested;
                roundInvestedAmount  = BigDecimal.ZERO;
                actionsPendingToClose = new InvestmentActions();
            }else{
                roundInvestedAmount = roundInvestedAmount.add(action.getAmountInvested());
                actionsPendingToClose.add(action);
            }
        }
        
        percentBenefit = percentBenefit.add(lastSellBenefit).divide(maxAmountInvested, 5, RoundingMode.HALF_DOWN).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_DOWN);
        benefit = benefit.add(lastSellBenefit);
    }

    public BigDecimal getAmountInvested() {
        Optional<BigDecimal> value = this.actions.parallelStream().map(x -> InvestmentActionEnum.BUY.equals(x.getAction()) ? x.getAmountInvested() : BigDecimal.ZERO).reduce( (x, y) -> y = y.add(x));
        
        if(value.isPresent()){
            return value.get();
        }else{
            return BigDecimal.ZERO;
        }
    }
    
    public BigDecimal getProfitability() {
        BigDecimal amountInvested = getAmountInvested();
        if(amountInvested.compareTo(BigDecimal.ZERO) != 0){
            return getBenefit().divide(amountInvested, 5, RoundingMode.HALF_DOWN);
        }else{
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getCurrentAmountInvested(){
        if(actionsPendingToClose == null || actionsPendingToClose.isEmpty()){
            return getAmountInvested();
        }else{
            return actionsPendingToClose.getAmountInvested();
        }
    }
    
    public BigDecimal getPercentBenefitSellingWith(StockPrice stock) {
        updateDataIfRequired();
       if(actionsPendingToClose == null || actionsPendingToClose.isEmpty()){
           return getPercentBenefit();
       }else{
           add(new InvestmentAction(stock, InvestmentActionEnum.SELL, actionsPendingToClose.getCurrentShares()));
           updateDataIfRequired();
           return percentBenefit;
       }
    }
    
    public Integer getCurrentShares() {
        if(actionsPendingToClose == null || actionsPendingToClose.isEmpty()){
            return getActions().stream().map(x -> x.getSharesAmount()).reduce( (x,  y) -> y = x + y ).get();
        }else{
            return actionsPendingToClose.getCurrentShares();
        }
    }

    public boolean isEmpty(){
        return actions.isEmpty();
    }
   
    public List<InvestmentAction> getActions() {
        return this.actions;
    }
   
    public BigDecimal getMaximumAmountInvestedInOneTradeSerie(){
        updateDataIfRequired();
        return maxAmountInvested;
    }

    public BigDecimal getPercentBenefit() {
        updateDataIfRequired();
        return this.percentBenefit;
    }
    
    public BigDecimal getBenefit() {
        updateDataIfRequired();
        return this.benefit;
    }

    public InvestmentActions getActionsPendingToClose() {
        updateDataIfRequired();
        return this.actionsPendingToClose;
    }

    private void updateDataIfRequired() {
        if(!benefitsUpdated){
            updateBenefit();
        }
    }

    public InvestmentAction getAction(Instant instant) {
        if(actions.stream().anyMatch( x -> x.getInstant().equals(instant))){
            return actions.stream().filter(x -> x.getInstant().equals(instant)).iterator().next();
        }
        return null;
    }
}
