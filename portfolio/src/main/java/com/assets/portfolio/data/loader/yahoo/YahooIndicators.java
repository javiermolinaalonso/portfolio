package com.assets.portfolio.data.loader.yahoo;

import java.util.List;
import java.util.Map;

public class YahooIndicators {

    private List<Map<String, List<Double>>> quote;
    private List<Map<String, List<Double>>> unadjclose;
    private List<Map<String, List<Double>>> adjclose;

    public YahooIndicators() {
    }

    public List<Map<String, List<Double>>> getQuote() {
        return quote;
    }

    public void setQuote(List<Map<String, List<Double>>> quote) {
        this.quote = quote;
    }

    public List<Map<String, List<Double>>> getUnadjclose() {
        return unadjclose;
    }

    public void setUnadjclose(List<Map<String, List<Double>>> unadjclose) {
        this.unadjclose = unadjclose;
    }

    public List<Map<String, List<Double>>> getAdjclose() {
        return adjclose;
    }

    public void setAdjclose(List<Map<String, List<Double>>> adjclose) {
        this.adjclose = adjclose;
    }
}
