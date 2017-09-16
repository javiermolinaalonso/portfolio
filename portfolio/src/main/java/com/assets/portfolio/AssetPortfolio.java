package com.assets.portfolio;

import com.assets.options.impl.VolatilityCalculator;
import com.assets.portfolio.data.loader.DataLoader;
import com.assets.portfolio.data.loader.yahoo.YahooDataLoader;
import com.assets.portfolio.pick.PickParameters;
import com.assets.portfolio.pick.PortfolioStockPickerService;
import com.assets.statistic.list.StockList;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetPortfolio {

    public static final String SP500 = "SP500";
    public static final String ISHARES_WORLD_QUALITY_DIVIDEND = "iShares MSCI World Quality Dividend UCITS ETF";
    public static final String JAVI_PORTFOLIO = "The current portfolio of Javi";
    public static final String RANDOM = "Random";

    //Given a group of 5 stocks it will calculate the correlation, volatility, performance, sharpe, and ponderation
    public static void main(String args[]) throws Exception {
        Map<String, String[]> tickers = new HashMap<>();
        tickers.put(ISHARES_WORLD_QUALITY_DIVIDEND, new String[] {"XOM", "PG", "T", "PFE", "KO", "PM", "MRK", "INTC", "DWDP", "CSCO", "MCD", "IBM", "ABBV", "MO", "SNY", "RY", "GILD", "ALV.DE", "UL", "BAS.DE"});
        tickers.put(JAVI_PORTFOLIO, new String[] {"SBUX", "KORS", "PYPL", "ATVI", "BBVA.MC", "GAS.MC", "GRF.MC", "ENG.MC", "HO.PA", "BDL.MC"});
        tickers.put(SP500, new String[] {
                "MMM", "AYI", "ALK", "ALLE", "AAL", "AME", "AOS", "ARNC", "BA", "CHRW", "CAT", "CTAS", "CSX", "CMI", "DE", "DAL", "DOV", "ETN", "EMR", "EFX", "EXPD", "FAST", "FDX", "FLS", "FLR", "FTV", "FBHS", "GD", "GE", "GWW", "HON", "INFO", "ITW", "IR", "JEC", "JBHT", "JCI", "KSU", "LLL", "LMT", "MAS", "NLSN", "NSC", "NOC", "PCAR", "PH", "PNR", "PWR", "RTN", "RSG", "RHI", "ROK", "COL", "ROP", "LUV", "SRCL", "TXT", "TDG", "UNP", "UAL", "UPS", "URI", "UTX", "VRSK", "WM", "XYL", "APC", "ANDV", "APA", "BHGE", "COG", "CHK", "CVX", "XEC", "CXO", "COP", "DVN", "EOG", "EQT", "XOM", "HAL", "HP", "HES", "KMI", "MRO", "MPC", "NOV", "NFX", "NBL", "OXY", "OKE", "PSX", "PXD", "RRC", "SLB", "FTI", "VLO", "WMB", "AAP", "AMZN", "AZO", "BBY", "BWA", "KMX", "CCL", "CBS", "CHTR", "CMG", "COH", "CMCSA", "DHI", "DRI", "DLPH", "DISCA", "DISCK", "DISH", "DG", "DLTR", "EXPE", "FL", "F", "GPS", "GRMN", "GM", "GPC", "GT", "HBI", "HOG", "HAS", "HLT", "HD", "IPG", "KSS", "LB", "LEG", "LEN", "LKQ", "LOW", "M", "MAR", "MAT", "MCD", "MGM", "KORS", "MHK", "NWL", "NWSA", "NWS", "NKE", "JWN", "ORLY", "OMC", "RL", "PCLN", "PHM", "PVH", "ROST", "RCL", "SNI", "SIG", "SNA", "SWK", "SPLS", "SBUX", "TGT", "TIF", "TWX", "TJX", "TSCO", "TRIP", "FOXA", "FOX", "ULTA", "UA", "UAA", "VFC", "VIAB", "DIS", "WHR", "WYN", "WYNN", "YUM", "AES", "LNT", "AEE", "AEP", "AWK", "CNP", "CMS", "ED", "D", "DTE", "DUK", "EIX", "ETR", "ES", "EXC", "FE", "NEE", "NI", "NRG", "PCG", "PNW", "PPL", "PEG", "SCG", "SRE", "SO", "WEC", "XEL", "T", "CTL", "LVLT", "VZ", "APD", "ALB", "AVY", "BLL", "CF", "DWDP", "EMN", "ECL", "FMC", "FCX", "IP", "IFF", "LYB", "MLM", "MON", "MOS", "NEM", "NUE", "PKG", "PPG", "PX", "SEE", "SHW", "VMC", "WRK", "ABT", "ABBV", "AET", "A", "ALXN", "ALGN", "AGN", "ABC", "AMGN", "ANTM", "BCR", "BAX", "BDX", "BIIB", "BSX", "BMY", "CAH", "CELG", "CNC", "CERN", "CI", "COO", "DHR", "DVA", "XRAY", "EW", "EVHC", "ESRX", "GILD", "HCA", "HSIC", "HOLX", "HUM", "IDXX", "ILMN", "INCY", "ISRG", "JNJ", "LH", "LLY", "MCK", "MDT", "MRK", "MTD", "MYL", "PDCO", "PKI", "PRGO", "PFE", "DGX", "Q", "REGN", "RMD", "SYK", "TMO", "UNH", "UHS", "VAR", "VRTX", "WAT", "ZBH", "ZTS", "ARE", "AMT", "AIV", "AVB", "BXP", "CBG", "CCI", "DLR", "DRE", "EQIX", "EQR", "ESS", "EXR", "FRT", "GGP", "HCP", "HST", "IRM", "KIM", "MAC", "MAA", "PLD", "PSA", "O", "REG", "SBAC", "SPG", "SLG", "UDR", "VTR", "VNO", "HCN", "WY", "MO", "ADM", "BF.B", "CPB", "CHD", "CLX", "KO", "CL", "CAG", "STZ", "COST", "COTY", "CVS", "DPS", "EL", "GIS", "HSY", "HRL", "SJM", "K", "KMB", "KHC", "KR", "MKC", "TAP", "MDLZ", "MNST", "PEP", "PM", "PG", "SYY", "TSN", "WMT", "WBA", "AMG", "AFL", "ALL", "AXP", "AIG", "AMP", "AON", "AJG", "AIZ", "BAC", "BK", "BBT", "BRK.B", "BLK", "HRB", "BHF", "COF", "CBOE", "SCHW", "CB", "CINF", "C", "CFG", "CME", "CMA", "DFS", "ETFC", "RE", "FITB", "BEN", "GS", "HIG", "HBAN", "ICE", "IVZ", "JPM", "KEY", "LUK", "LNC", "L", "MTB", "MMC", "MET", "MCO", "MS", "NDAQ", "NAVI", "NTRS", "PBCT", "PNC", "PFG", "PGR", "PRU", "RJF", "RF", "SPGI", "STT", "STI", "SYF", "TROW", "TMK", "TRV", "USB", "UNM", "WFC", "WLTW", "XL", "ZION", "ACN", "ATVI", "ADBE", "AMD", "AKAM", "ADS", "GOOGL", "GOOG", "APH", "ADI", "ANSS", "AAPL", "AMAT", "ADSK", "ADP", "AVGO", "CA", "CSCO", "CTXS", "CTSH", "GLW", "CSRA", "DXC", "EBAY", "EA", "FFIV", "FB", "FIS", "FISV", "FLIR", "IT", "GPN", "HRS", "HPE", "HPQ", "INTC", "IBM", "INTU", "JNPR", "KLAC", "LRCX", "MA", "MCHP", "MU", "MSFT", "MSI", "NTAP", "NFLX", "NVDA", "ORCL", "PAYX", "PYPL", "QRVO", "QCOM", "RHT", "CRM", "STX", "SWKS", "SYMC", "SNPS", "TEL", "TXN", "TSS", "VRSN", "V", "WDC", "WU", "XRX", "XLNX"
        });
        tickers.put(RANDOM, new String[] {
                "MMM", "AYI", "ALK", "ALLE", "AAL", "AME", "AOS", "ARNC", "BA", "CHRW", "CAT", "CTAS", "CSX", "CMI", "DE", "DAL", "DOV", "ETN", "EMR", "EFX", "EXPD", "FAST", "FDX", "FLS", "FLR", "FTV", "FBHS", "GD", "GE", "GWW", "HON", "INFO", "ITW", "IR", "JEC", "JBHT", "JCI", "KSU"
        });

        LocalDateTime from = LocalDateTime.of(2015,1,1, 0, 0);
        LocalDateTime to = LocalDateTime.now().minusDays(1);

        DataLoader loader = new YahooDataLoader(Arrays.asList(tickers.get(ISHARES_WORLD_QUALITY_DIVIDEND)), to, from);

        final Map<String, StockList> values = loader.loadData();
        final PortfolioService portfolioService = new PortfolioService(new VolatilityCalculator());
        final Portfolio portfolio = portfolioService.buildPortfolio(values, from, to);

        portfolio.print();

        final PortfolioStockPickerService stockPickerService = new PortfolioStockPickerService(portfolioService);
        PickParameters params = PickParameters.Builder.aPickParameters()
                .withMaxStocks(10)
                .withMaxCorrelation(0.30)
                .withMaxVolatility(0.20)
                .build();

        final List<Portfolio> portfolios = stockPickerService.pick(portfolio, params);
        portfolios.forEach(Portfolio::print);
    }

}
