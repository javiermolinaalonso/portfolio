package com.assets.portfolio.data.loader.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.assets.entities.StockPrice;
import com.assets.statistic.list.StockList;
import org.apache.log4j.Logger;

import com.assets.portfolio.data.exceptions.DataLoaderEmptyFileException;
import com.assets.portfolio.data.exceptions.DataLoaderFileNotFoundException;
import com.assets.portfolio.data.exceptions.DataLoaderIOException;
import com.assets.portfolio.data.loader.DataLoader;

public class DataLoaderCsv implements DataLoader {
    
    //TODO Use properties to format date
    private static final String DEFAULT_DATE_FORMAT = "yyyyMMdd";

    private static final String READ_CSV_SEPARATOR = ",";
    
    private final Logger logger = Logger.getLogger(DataLoaderCsv.class);
    private final SimpleDateFormat dateFormatter;
    
    private String dataFile;
    private Map<String, StockList> data;
    
    public DataLoaderCsv() {
        dateFormatter = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        data = new HashMap<String, StockList>();
    }
    
    public DataLoaderCsv(String dataFile){
        this();
        setDataFile(dataFile);
    }
    
    @Override
    public Map<String, StockList> loadData() {
        if(dataFile == null){
            throw new DataLoaderEmptyFileException();
        }
        File directory = new File(dataFile);

        int amount = directory.listFiles() == null ? 1 : directory.listFiles().length;

        for(int i = 0; i < amount; i++){
            File file;
            if(directory.isDirectory()) {
                file = directory.listFiles()[i];
            }else{
                file = directory;
            }
            String ticker = file.getName().split("_")[1].split("\\.")[0].toUpperCase();
            try {
                data.put(ticker, loadList(ticker, file.getAbsolutePath()));
            }catch(DataLoaderFileNotFoundException de){
                logger.error(String.format("The file with ticker %s not found", ticker));
            }catch(DataLoaderIOException dle){
                logger.error(String.format("An IO error occurred while reading file %s", ticker));
            }
        }

        return data;
    }

    private StockList loadList(String ticker, String file) {
        StockList prices = new StockList(ticker);
        for(String[] value : readCsv(file)){
            try {
                prices.add(new StockPrice(ticker, Instant.ofEpochMilli(dateFormatter.parse(value[0]).getTime()), new BigDecimal(value[2])));
            } catch (ParseException e) {
                logger.error(String.format("Error parsing the date %s with ticker %s", value[0], ticker), e);
            }
        }
        return prices;
    }

    private List<String[]> readCsv(String file) {
        List<String[]> list = new ArrayList<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(new File(file)));
        } catch (FileNotFoundException e) {
            throw new DataLoaderFileNotFoundException();
        }
        try {
            String line = br.readLine();
            while (line != null) {
                list.add(line.split(READ_CSV_SEPARATOR));
                line = br.readLine();
            }
            br.close();
        }catch(IOException io){
            throw new DataLoaderIOException();
        }
        return list;
    }
    
    @Override
    public StockList loadData(String ticker) {
        return data.get(ticker);
    }

    @Override
    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }

}
