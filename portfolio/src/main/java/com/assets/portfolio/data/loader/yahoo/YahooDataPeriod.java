package com.assets.portfolio.data.loader.yahoo;

public enum YahooDataPeriod {
    ONE_MINUTE("1m"),
    ONE_HOUR("1h"),
    ONE_DAY("1d"),
    ONE_WEEK("1wk");

    private final String yahoovalue;
    YahooDataPeriod(String yahoovalue) {
        this.yahoovalue = yahoovalue;
    }

    public String getYahoovalue() {
        return yahoovalue;
    }
}
