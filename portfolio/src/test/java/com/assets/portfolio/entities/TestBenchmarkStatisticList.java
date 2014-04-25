package com.assets.portfolio.entities;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import com.assets.portfolio.correlation.entities.FactoryStatisticList;
import com.assets.portfolio.correlation.entities.StatisticList;
import com.assets.portfolio.correlation.entities.enums.StatisticListType;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

@BenchmarkOptions(benchmarkRounds=100, warmupRounds=10)
public class TestBenchmarkStatisticList {

    private static final int MAX_VALUES = 1000000;

    @Rule
    public TestRule benchmark = new BenchmarkRule();
    
    private static BigDecimal[] array = new BigDecimal[MAX_VALUES]; 
    private static BigDecimal result;
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        Random r = new Random(System.currentTimeMillis());
        for(int i = 0; i < MAX_VALUES; i++){
            array[i] = new BigDecimal(r.nextDouble());
        }
        result = FactoryStatisticList.getStatisticList(new ArrayList<BigDecimal>(Arrays.asList(array)), StatisticListType.LAMBDA).getMean();
    }

    @Before
    public void setUp() {
        
    }
    
    @Test
    public void arrayListSingleThread() throws Exception {
        runTest(FactoryStatisticList.getStatisticList(new ArrayList<BigDecimal>(Arrays.asList(array)), StatisticListType.LAMBDA));
    }

    @Test
    public void linkedListSingleThread() throws Exception {
        runTest(FactoryStatisticList.getStatisticList(new LinkedList<BigDecimal>(Arrays.asList(array)), StatisticListType.LAMBDA));
    }

    @Test
    public void vectorSingleThread() throws Exception {
        runTest(FactoryStatisticList.getStatisticList(new Vector<BigDecimal>(Arrays.asList(array)), StatisticListType.LAMBDA));
    }
    
    @Test
    public void arrayListMultiThread() throws Exception {
        runTest(FactoryStatisticList.getStatisticList(new ArrayList<BigDecimal>(Arrays.asList(array)), StatisticListType.LAMBDA));
    }

    @Test
    public void linkedListMultiThread() throws Exception {
        runTest(FactoryStatisticList.getStatisticList(new LinkedList<BigDecimal>(Arrays.asList(array)), StatisticListType.LAMBDA));
    }

    @Test
    public void vectorMultiThread() throws Exception {
        runTest(FactoryStatisticList.getStatisticList(new Vector<BigDecimal>(Arrays.asList(array)), StatisticListType.LAMBDA));
    }
    
    public void runTest(StatisticList<BigDecimal> sList) {
        BigDecimal r = sList.getMean();
        assertTrue(result.equals(r));
    }

}
