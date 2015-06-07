package com.unisoft.algotrader.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by alex on 5/31/15.
 */
public class PerformanceTest {

    private Portfolio portfolio;
    private Performance performance;


    @Before
    public void setup() {
        portfolio = mock(Portfolio.class);
        performance = new Performance(portfolio, null);
    }

    @Test
    public void test_high_equity() {
        when(portfolio.totalEquity()).thenReturn(1000.0);
        performance.valueChanged(0);
        assertEquals(1000.0, performance.getHighEquity(), 0.0);

        when(portfolio.totalEquity()).thenReturn(1100.0);
        performance.valueChanged(1);
        assertEquals(1100.0, performance.getHighEquity(), 0.0);

        when(portfolio.totalEquity()).thenReturn(1200.0);
        performance.valueChanged(2);
        assertEquals(1200.0, performance.getHighEquity(), 0.0);

        when(portfolio.totalEquity()).thenReturn(950.0);
        performance.valueChanged(3);
        assertEquals(1200.0, performance.getHighEquity(), 0.0);

        when(portfolio.totalEquity()).thenReturn(1050.0);
        performance.valueChanged(4);
        assertEquals(1200.0, performance.getHighEquity(), 0.0);

        when(portfolio.totalEquity()).thenReturn(1600.0);
        performance.valueChanged(5);
        assertEquals(1600.0, performance.getHighEquity(), 0.0);

        when(portfolio.totalEquity()).thenReturn(500.0);
        performance.valueChanged(6);
        assertEquals(1600.0, performance.getHighEquity(), 0.0);
    }

    @Test
    public void test_low_equity(){
        when(portfolio.totalEquity()).thenReturn(1000.0);
        performance.valueChanged(0);
        assertEquals(1000.0, performance.getLowEquity(), 0.0);

        when(portfolio.totalEquity()).thenReturn(1100.0);
        performance.valueChanged(1);
        assertEquals(1100.0, performance.getLowEquity(), 0.0);

        when(portfolio.totalEquity()).thenReturn(1200.0);
        performance.valueChanged(2);
        assertEquals(1200.0, performance.getLowEquity(), 0.0);

        when(portfolio.totalEquity()).thenReturn(950.0);
        performance.valueChanged(3);
        assertEquals(950.0, performance.getLowEquity(), 0.0);

        when(portfolio.totalEquity()).thenReturn(1050.0);
        performance.valueChanged(4);
        assertEquals(950.0, performance.getLowEquity(), 0.0);
    }

    @Test
    public void test_equity(){
        when(portfolio.totalEquity()).thenReturn(1000.0);
        performance.valueChanged(0);
        assertEquals(1000.0, performance.getEquity(), 0.0);

        when(portfolio.totalEquity()).thenReturn(1100.0);
        performance.valueChanged(1);
        assertEquals(1100.0, performance.getEquity(), 0.0);

        when(portfolio.totalEquity()).thenReturn(1200.0);
        performance.valueChanged(2);
        assertEquals(1200.0, performance.getEquity(), 0.0);

        when(portfolio.totalEquity()).thenReturn(950.0);
        performance.valueChanged(3);
        assertEquals(950.0, performance.getEquity(), 0.0);

        when(portfolio.totalEquity()).thenReturn(1050.0);
        performance.valueChanged(4);
        assertEquals(1050.0, performance.getEquity(), 0.0);


        assertEquals(1000.0, performance.getEquitySeries().getByIdx(0), 0.0);
        assertEquals(1100.0, performance.getEquitySeries().getByIdx(1), 0.0);
        assertEquals(1200.0, performance.getEquitySeries().getByIdx(2), 0.0);
        assertEquals(950.0, performance.getEquitySeries().getByIdx(3), 0.0);
        assertEquals(1050.0, performance.getEquitySeries().getByIdx(4), 0.0);

    }

    @Test
    public void test_pnl(){
        when(portfolio.totalEquity()).thenReturn(1000.0);
        performance.valueChanged(0);
        assertEquals(0.0, performance.getPnl(), 0.0);

        when(portfolio.totalEquity()).thenReturn(1100.0);
        performance.valueChanged(1);
        assertEquals(100.0, performance.getPnl(), 0.0);

        when(portfolio.totalEquity()).thenReturn(1200.0);
        performance.valueChanged(2);
        assertEquals(100.0, performance.getPnl(), 0.0);

        when(portfolio.totalEquity()).thenReturn(950.0);
        performance.valueChanged(3);
        assertEquals(-250.0, performance.getPnl(), 0.0);

        when(portfolio.totalEquity()).thenReturn(1050.0);
        performance.valueChanged(4);
        assertEquals(100.0, performance.getPnl(), 0.0);

        assertEquals(100.0, performance.getPnlSeries().getByIdx(0), 0.0);
        assertEquals(100.0, performance.getPnlSeries().getByIdx(1), 0.0);
        assertEquals(-250, performance.getPnlSeries().getByIdx(2), 0.0);
        assertEquals(100, performance.getPnlSeries().getByIdx(3), 0.0);
    }

    @Test
    public void test_drawdown_and_drawdown_pct(){

        long count = 0;
        when(portfolio.totalEquity()).thenReturn(1000.0);
        performance.valueChanged(count++);
        assertEquals(0.0, performance.getDrawdown(), 0.0);
        assertEquals(0.0, performance.getDrawdownPercent(), 0.0);

        when(portfolio.totalEquity()).thenReturn(1100.0);
        performance.valueChanged(count++);
        assertEquals(0.0, performance.getDrawdown(), 0.0);
        assertEquals(0.0, performance.getDrawdownPercent(), 0.0);

        when(portfolio.totalEquity()).thenReturn(1050.0);
        performance.valueChanged(count++);
        assertEquals(-50, performance.getDrawdown(), 0.0);
        assertEquals(Math.abs(-50.0/1100.0), performance.getDrawdownPercent(), 0.0);

        when(portfolio.totalEquity()).thenReturn(950.0);
        performance.valueChanged(count++);
        assertEquals(-150, performance.getDrawdown(), 0.0);
        assertEquals(Math.abs(-150.0/1100.0), performance.getDrawdownPercent(), 0.0);

        when(portfolio.totalEquity()).thenReturn(750.0);
        performance.valueChanged(count++);
        assertEquals((750.0 - 1100), performance.getDrawdown(), 0.0);
        assertEquals(Math.abs((750.0 - 1100.0)/1100.0), performance.getDrawdownPercent(), 0.0);

        when(portfolio.totalEquity()).thenReturn(1200.0);
        performance.valueChanged(count++);
        assertEquals(0.0, performance.getDrawdown(), 0.0);
        assertEquals(0.0, performance.getDrawdownPercent(), 0.0);

        when(portfolio.totalEquity()).thenReturn(1350.0);
        performance.valueChanged(count++);
        assertEquals(0.0, performance.getDrawdown(), 0.0);
        assertEquals(0.0, performance.getDrawdownPercent(), 0.0);

        when(portfolio.totalEquity()).thenReturn(650.0);
        performance.valueChanged(count++);
        assertEquals((650.0 - 1350), performance.getDrawdown(), 0.0);
        assertEquals(Math.abs((650.0 - 1350.0)/1350.0), performance.getDrawdownPercent(), 0.0);

        when(portfolio.totalEquity()).thenReturn(750.0);
        performance.valueChanged(count++);
        assertEquals((750 - 1350), performance.getDrawdown(), 0.0);
        assertEquals(Math.abs((750.0 - 1350.0)/1350.0), performance.getDrawdownPercent(), 0.0);


        assertEquals(0.0, performance.getDrawdownSeries().getByIdx(0), 0.0);
        assertEquals(-50, performance.getDrawdownSeries().getByIdx(1), 0.0);
        assertEquals(-150, performance.getDrawdownSeries().getByIdx(2), 0.0);
        assertEquals((750.0 - 1100), performance.getDrawdownSeries().getByIdx(3), 0.0);
        assertEquals(0.0, performance.getDrawdownSeries().getByIdx(4), 0.0);
        assertEquals(0.0, performance.getDrawdownSeries().getByIdx(5), 0.0);
        assertEquals((650.0 - 1350), performance.getDrawdownSeries().getByIdx(6), 0.0);
        assertEquals((750 - 1350), performance.getDrawdownSeries().getByIdx(7), 0.0);

        assertEquals(0.0, performance.getDrawdownPercentSeries().getByIdx(0), 0.0);
        assertEquals(Math.abs(-50.0/1100.0), performance.getDrawdownPercentSeries().getByIdx(1), 0.0);
        assertEquals(Math.abs(-150.0/1100.0), performance.getDrawdownPercentSeries().getByIdx(2), 0.0);
        assertEquals(Math.abs((750.0 - 1100.0)/1100.0), performance.getDrawdownPercentSeries().getByIdx(3), 0.0);
        assertEquals(0.0, performance.getDrawdownPercentSeries().getByIdx(4), 0.0);
        assertEquals(0.0, performance.getDrawdownPercentSeries().getByIdx(5), 0.0);
        assertEquals(Math.abs((650.0 - 1350.0)/1350.0), performance.getDrawdownPercentSeries().getByIdx(6), 0.0);
        assertEquals(Math.abs((750.0 - 1350.0)/1350.0), performance.getDrawdownPercentSeries().getByIdx(7), 0.0);
    }

    @Test
    public void test_current_drawdown(){

        long count = 0;
        when(portfolio.totalEquity()).thenReturn(1000.0);
        performance.valueChanged(count++);
        assertEquals(0.0, performance.getCurrentDrawdown(), 0.0);

        when(portfolio.totalEquity()).thenReturn(1100.0);
        performance.valueChanged(count++);
        assertEquals(0.0, performance.getCurrentDrawdown(), 0.0);

        when(portfolio.totalEquity()).thenReturn(550.0);
        performance.valueChanged(count++);
        assertEquals(0.0, performance.getCurrentDrawdown(), 0.0);

        when(portfolio.totalEquity()).thenReturn(850.0);
        performance.valueChanged(count++);
        assertEquals((1.0 - 850.0 / 1100.0), performance.getCurrentDrawdown(), 0.0);

        when(portfolio.totalEquity()).thenReturn(950.0);
        performance.valueChanged(count++);
        assertEquals((1.0 - 950.0 / 1100.0), performance.getCurrentDrawdown(), 0.0);

        when(portfolio.totalEquity()).thenReturn(1200.0);
        performance.valueChanged(count++);
        assertEquals(0.0, performance.getCurrentDrawdown(), 0.0);

        when(portfolio.totalEquity()).thenReturn(1350.0);
        performance.valueChanged(count++);
        assertEquals(0.0, performance.getCurrentDrawdown(), 0.0);

        when(portfolio.totalEquity()).thenReturn(1350.0);
        performance.valueChanged(count++);
        assertEquals(0.0, performance.getCurrentDrawdown(), 0.0);

        when(portfolio.totalEquity()).thenReturn(650.0);
        performance.valueChanged(count++);
        assertEquals(0, performance.getCurrentDrawdown(), 0.0);

        when(portfolio.totalEquity()).thenReturn(850.0);
        performance.valueChanged(count++);
        assertEquals((1.0 - 850.0 / 1350.0), performance.getCurrentDrawdown(), 0.0);

        when(portfolio.totalEquity()).thenReturn(750.0);
        performance.valueChanged(count++);
        assertEquals((1.0 - 750.0 / 1350.0), performance.getCurrentDrawdown(), 0.0);



    }

    @Test
    public void test_current_run_up(){

        long count = 0;
        when(portfolio.totalEquity()).thenReturn(1000.0);
        performance.valueChanged(count++);
        assertEquals(0.0, performance.getCurrentRunUp(), 0.0);

        when(portfolio.totalEquity()).thenReturn(1100.0);
        performance.valueChanged(count++);
        assertEquals(0.0, performance.getCurrentRunUp(), 0.0);

        when(portfolio.totalEquity()).thenReturn(550.0);
        performance.valueChanged(count++);
        assertEquals(0.0, performance.getCurrentRunUp(), 0.0);

        when(portfolio.totalEquity()).thenReturn(850.0);
        performance.valueChanged(count++);
        assertEquals((850.0 / 550.0 - 1.0), performance.getCurrentRunUp(), 0.0);

        when(portfolio.totalEquity()).thenReturn(950.0);
        performance.valueChanged(count++);
        assertEquals((950.0 / 550.0 - 1.0), performance.getCurrentRunUp(), 0.0);

        when(portfolio.totalEquity()).thenReturn(1200.0);
        performance.valueChanged(count++);
        assertEquals((950.0 / 550.0 - 1.0), performance.getCurrentRunUp(), 0.0);

        when(portfolio.totalEquity()).thenReturn(1350.0);
        performance.valueChanged(count++);
        assertEquals((950.0 / 550.0 - 1.0), performance.getCurrentRunUp(), 0.0);

        when(portfolio.totalEquity()).thenReturn(1350.0);
        performance.valueChanged(count++);
        assertEquals((950.0 / 550.0 - 1.0), performance.getCurrentRunUp(), 0.0);

        when(portfolio.totalEquity()).thenReturn(650.0);
        performance.valueChanged(count++);
        assertEquals(0, performance.getCurrentRunUp(), 0.0);

        when(portfolio.totalEquity()).thenReturn(850.0);
        performance.valueChanged(count++);
        assertEquals((850.0 / 650.0 - 1.0), performance.getCurrentRunUp(), 0.0);

        when(portfolio.totalEquity()).thenReturn(750.0);
        performance.valueChanged(count++);
        assertEquals((750.0 / 650.0 - 1.0), performance.getCurrentRunUp(), 0.0);

    }
}
