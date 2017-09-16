package com.assets.portfolio.data.loader.yahoo;

import java.util.List;

public class YahooChartElement {
    private Object meta;
    private List<Integer> timestamp;
    private YahooIndicators indicators;

    public YahooChartElement() {
    }

    public Object getMeta() {
        return meta;
    }

    public void setMeta(Object meta) {
        this.meta = meta;
    }

    public List<Integer> getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(List<Integer> timestamp) {
        this.timestamp = timestamp;
    }

    public YahooIndicators getIndicators() {
        return indicators;
    }

    public void setIndicators(YahooIndicators indicators) {
        this.indicators = indicators;
    }
}
