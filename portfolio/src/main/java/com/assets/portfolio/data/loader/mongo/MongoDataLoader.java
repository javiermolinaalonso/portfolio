package com.assets.portfolio.data.loader.mongo;

import com.assets.entities.StockPrice;
import com.assets.portfolio.data.loader.DataLoader;
import com.assets.statistic.list.StockList;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.types.Decimal128;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;

public class MongoDataLoader implements DataLoader {

    public static final String DAILY_QUOTES = "daily_quotes";
    private final DataLoader defaultLoader;
    private final MongoClient mongo;
    private final String database;

    public MongoDataLoader(DataLoader defaultLoader, MongoClient mongo, String database) {
        this.defaultLoader = defaultLoader;
        this.mongo = mongo;
        this.database = database;
    }


    @Override
    public Map<String, StockList> loadData() {
        return null;
    }

    @Override
    public StockList loadData(String ticker) {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("ticker", ticker);
        return loadData(ticker, searchQuery);
    }

    private StockList loadData(String ticker, BasicDBObject searchQuery) {
        FindIterable<Document> values = mongo.getDatabase(database).getCollection(DAILY_QUOTES).find(searchQuery);
        if (values.first() == null) {
            final StockList stockPrices = defaultLoader.loadData(ticker);
            stockPrices.stream()
                    .map(stockPrice -> new Document("ticker", stockPrice.getTicker())
                            .append("date", new Date(LocalDateTime.ofInstant(stockPrice.getInstant(), ZoneId.systemDefault()).withHour(0).withMinute(0).withSecond(0).withNano(0).toInstant(ZoneOffset.UTC).toEpochMilli()))
                            .append("value", stockPrice.getValue()))
                    .forEach(d -> mongo.getDatabase(database).getCollection(DAILY_QUOTES).insertOne(d));
            values = mongo.getDatabase(database).getCollection(DAILY_QUOTES).find(searchQuery);
        }
        final MongoCursor<StockPrice> stockPrices = values.map(this::buildStockPrice).iterator();
        StockList stockList = new StockList(ticker);
        while(stockPrices.hasNext()) {
            stockList.add(stockPrices.next());
        }
        return stockList;
    }

    private StockPrice buildStockPrice(Document value) {
        final Date date = (Date) value.get("date");
        final BigDecimal price = ((Decimal128) value.get("value")).bigDecimalValue();
        return new StockPrice((String) value.get("ticker"), date.toInstant(), price);
    }

    @Override
    public void setDataFile(String dataFile) {

    }
}
