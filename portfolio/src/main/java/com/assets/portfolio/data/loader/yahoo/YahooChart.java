package com.assets.portfolio.data.loader.yahoo;

import java.util.List;

public class YahooChart {

    private List<YahooChartElement> result;
    private Object error;

    public YahooChart() {
    }

    public List<YahooChartElement> getResult() {
        return result;
    }

    public void setResult(List<YahooChartElement> result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}
