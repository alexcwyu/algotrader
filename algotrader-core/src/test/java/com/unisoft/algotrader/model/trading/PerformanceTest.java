package com.unisoft.algotrader.model.trading;

import com.unisoft.algotrader.model.refdata.Currency;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Created by alex on 5/31/15.
 */
public class PerformanceTest {

    private PortfolioProcessor processor;
    private Performance performance;


    @Before
    public void setup() {
        Account account = new Account("TestAccount", "", Currency.USD, 100000);
        AccountManager.INSTANCE.add(account);
        Portfolio portfolio = new Portfolio("Test", "TestAccount");
        processor = spy(new PortfolioProcessor(portfolio));
        performance = portfolio.getPerformance();

    }

    @Test
    public void test_high_equity() {
        when(processor.totalEquity()).thenReturn(1000.0);
        processor.updatePerformance(0);
        assertEquals(1000.0, performance.getHighEquity(), 0.0);

        when(processor.totalEquity()).thenReturn(1100.0);
        processor.updatePerformance(1);
        assertEquals(1100.0, performance.getHighEquity(), 0.0);

        when(processor.totalEquity()).thenReturn(1200.0);
        processor.updatePerformance(2);
        assertEquals(1200.0, performance.getHighEquity(), 0.0);

        when(processor.totalEquity()).thenReturn(950.0);
        processor.updatePerformance(3);
        assertEquals(1200.0, performance.getHighEquity(), 0.0);

        when(processor.totalEquity()).thenReturn(1050.0);
        processor.updatePerformance(4);
        assertEquals(1200.0, performance.getHighEquity(), 0.0);

        when(processor.totalEquity()).thenReturn(1600.0);
        processor.updatePerformance(5);
        assertEquals(1600.0, performance.getHighEquity(), 0.0);

        when(processor.totalEquity()).thenReturn(500.0);
        processor.updatePerformance(6);
        assertEquals(1600.0, performance.getHighEquity(), 0.0);
    }

    @Test
    public void test_low_equity(){
        when(processor.totalEquity()).thenReturn(1000.0);
        processor.updatePerformance(0);
        assertEquals(1000.0, performance.getLowEquity(), 0.0);

        when(processor.totalEquity()).thenReturn(1100.0);
        processor.updatePerformance(1);
        assertEquals(1100.0, performance.getLowEquity(), 0.0);

        when(processor.totalEquity()).thenReturn(1200.0);
        processor.updatePerformance(2);
        assertEquals(1200.0, performance.getLowEquity(), 0.0);

        when(processor.totalEquity()).thenReturn(950.0);
        processor.updatePerformance(3);
        assertEquals(950.0, performance.getLowEquity(), 0.0);

        when(processor.totalEquity()).thenReturn(1050.0);
        processor.updatePerformance(4);
        assertEquals(950.0, performance.getLowEquity(), 0.0);
    }

    @Test
    public void test_equity(){
        when(processor.totalEquity()).thenReturn(1000.0);
        processor.updatePerformance(0);
        assertEquals(1000.0, performance.getEquity(), 0.0);

        when(processor.totalEquity()).thenReturn(1100.0);
        processor.updatePerformance(1);
        assertEquals(1100.0, performance.getEquity(), 0.0);

        when(processor.totalEquity()).thenReturn(1200.0);
        processor.updatePerformance(2);
        assertEquals(1200.0, performance.getEquity(), 0.0);

        when(processor.totalEquity()).thenReturn(950.0);
        processor.updatePerformance(3);
        assertEquals(950.0, performance.getEquity(), 0.0);

        when(processor.totalEquity()).thenReturn(1050.0);
        processor.updatePerformance(4);
        assertEquals(1050.0, performance.getEquity(), 0.0);


        assertEquals(1000.0, performance.getEquitySeries().getByIdx(0), 0.0);
        assertEquals(1100.0, performance.getEquitySeries().getByIdx(1), 0.0);
        assertEquals(1200.0, performance.getEquitySeries().getByIdx(2), 0.0);
        assertEquals(950.0, performance.getEquitySeries().getByIdx(3), 0.0);
        assertEquals(1050.0, performance.getEquitySeries().getByIdx(4), 0.0);

    }

    @Test
    public void test_pnl(){
        when(processor.totalEquity()).thenReturn(1000.0);
        processor.updatePerformance(0);
        assertEquals(0.0, performance.getPnl(), 0.0);

        when(processor.totalEquity()).thenReturn(1100.0);
        processor.updatePerformance(1);
        assertEquals(100.0, performance.getPnl(), 0.0);

        when(processor.totalEquity()).thenReturn(1200.0);
        processor.updatePerformance(2);
        assertEquals(100.0, performance.getPnl(), 0.0);

        when(processor.totalEquity()).thenReturn(950.0);
        processor.updatePerformance(3);
        assertEquals(-250.0, performance.getPnl(), 0.0);

        when(processor.totalEquity()).thenReturn(1050.0);
        processor.updatePerformance(4);
        assertEquals(100.0, performance.getPnl(), 0.0);

        assertEquals(100.0, performance.getPnlSeries().getByIdx(0), 0.0);
        assertEquals(100.0, performance.getPnlSeries().getByIdx(1), 0.0);
        assertEquals(-250, performance.getPnlSeries().getByIdx(2), 0.0);
        assertEquals(100, performance.getPnlSeries().getByIdx(3), 0.0);
    }

    @Test
    public void test_drawdown_and_drawdown_pct(){

        long count = 0;
        when(processor.totalEquity()).thenReturn(1000.0);
        processor.updatePerformance(count++);
        assertEquals(0.0, performance.getDrawdown(), 0.0);
        assertEquals(0.0, performance.getDrawdownPercent(), 0.0);

        when(processor.totalEquity()).thenReturn(1100.0);
        processor.updatePerformance(count++);
        assertEquals(0.0, performance.getDrawdown(), 0.0);
        assertEquals(0.0, performance.getDrawdownPercent(), 0.0);

        when(processor.totalEquity()).thenReturn(1050.0);
        processor.updatePerformance(count++);
        assertEquals(-50, performance.getDrawdown(), 0.0);
        assertEquals(Math.abs(-50.0/1100.0), performance.getDrawdownPercent(), 0.0);

        when(processor.totalEquity()).thenReturn(950.0);
        processor.updatePerformance(count++);
        assertEquals(-150, performance.getDrawdown(), 0.0);
        assertEquals(Math.abs(-150.0/1100.0), performance.getDrawdownPercent(), 0.0);

        when(processor.totalEquity()).thenReturn(750.0);
        processor.updatePerformance(count++);
        assertEquals((750.0 - 1100), performance.getDrawdown(), 0.0);
        assertEquals(Math.abs((750.0 - 1100.0)/1100.0), performance.getDrawdownPercent(), 0.0);

        when(processor.totalEquity()).thenReturn(1200.0);
        processor.updatePerformance(count++);
        assertEquals(0.0, performance.getDrawdown(), 0.0);
        assertEquals(0.0, performance.getDrawdownPercent(), 0.0);

        when(processor.totalEquity()).thenReturn(1350.0);
        processor.updatePerformance(count++);
        assertEquals(0.0, performance.getDrawdown(), 0.0);
        assertEquals(0.0, performance.getDrawdownPercent(), 0.0);

        when(processor.totalEquity()).thenReturn(650.0);
        processor.updatePerformance(count++);
        assertEquals((650.0 - 1350), performance.getDrawdown(), 0.0);
        assertEquals(Math.abs((650.0 - 1350.0)/1350.0), performance.getDrawdownPercent(), 0.0);

        when(processor.totalEquity()).thenReturn(750.0);
        processor.updatePerformance(count++);
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
        when(processor.totalEquity()).thenReturn(1000.0);
        processor.updatePerformance(count++);
        assertEquals(0.0, performance.getCurrentDrawdown(), 0.0);

        when(processor.totalEquity()).thenReturn(1100.0);
        processor.updatePerformance(count++);
        assertEquals(0.0, performance.getCurrentDrawdown(), 0.0);

        when(processor.totalEquity()).thenReturn(550.0);
        processor.updatePerformance(count++);
        assertEquals(0.0, performance.getCurrentDrawdown(), 0.0);

        when(processor.totalEquity()).thenReturn(850.0);
        processor.updatePerformance(count++);
        assertEquals((1.0 - 850.0 / 1100.0), performance.getCurrentDrawdown(), 0.0);

        when(processor.totalEquity()).thenReturn(950.0);
        processor.updatePerformance(count++);
        assertEquals((1.0 - 950.0 / 1100.0), performance.getCurrentDrawdown(), 0.0);

        when(processor.totalEquity()).thenReturn(1200.0);
        processor.updatePerformance(count++);
        assertEquals(0.0, performance.getCurrentDrawdown(), 0.0);

        when(processor.totalEquity()).thenReturn(1350.0);
        processor.updatePerformance(count++);
        assertEquals(0.0, performance.getCurrentDrawdown(), 0.0);

        when(processor.totalEquity()).thenReturn(1350.0);
        processor.updatePerformance(count++);
        assertEquals(0.0, performance.getCurrentDrawdown(), 0.0);

        when(processor.totalEquity()).thenReturn(650.0);
        processor.updatePerformance(count++);
        assertEquals(0, performance.getCurrentDrawdown(), 0.0);

        when(processor.totalEquity()).thenReturn(850.0);
        processor.updatePerformance(count++);
        assertEquals((1.0 - 850.0 / 1350.0), performance.getCurrentDrawdown(), 0.0);

        when(processor.totalEquity()).thenReturn(750.0);
        processor.updatePerformance(count++);
        assertEquals((1.0 - 750.0 / 1350.0), performance.getCurrentDrawdown(), 0.0);



    }

    @Test
    public void test_current_run_up(){

        long count = 0;
        when(processor.totalEquity()).thenReturn(1000.0);
        processor.updatePerformance(count++);
        assertEquals(0.0, performance.getCurrentRunUp(), 0.0);

        when(processor.totalEquity()).thenReturn(1100.0);
        processor.updatePerformance(count++);
        assertEquals(0.0, performance.getCurrentRunUp(), 0.0);

        when(processor.totalEquity()).thenReturn(550.0);
        processor.updatePerformance(count++);
        assertEquals(0.0, performance.getCurrentRunUp(), 0.0);

        when(processor.totalEquity()).thenReturn(850.0);
        processor.updatePerformance(count++);
        assertEquals((850.0 / 550.0 - 1.0), performance.getCurrentRunUp(), 0.0);

        when(processor.totalEquity()).thenReturn(950.0);
        processor.updatePerformance(count++);
        assertEquals((950.0 / 550.0 - 1.0), performance.getCurrentRunUp(), 0.0);

        when(processor.totalEquity()).thenReturn(1200.0);
        processor.updatePerformance(count++);
        assertEquals((950.0 / 550.0 - 1.0), performance.getCurrentRunUp(), 0.0);

        when(processor.totalEquity()).thenReturn(1350.0);
        processor.updatePerformance(count++);
        assertEquals((950.0 / 550.0 - 1.0), performance.getCurrentRunUp(), 0.0);

        when(processor.totalEquity()).thenReturn(1350.0);
        processor.updatePerformance(count++);
        assertEquals((950.0 / 550.0 - 1.0), performance.getCurrentRunUp(), 0.0);

        when(processor.totalEquity()).thenReturn(650.0);
        processor.updatePerformance(count++);
        assertEquals(0, performance.getCurrentRunUp(), 0.0);

        when(processor.totalEquity()).thenReturn(850.0);
        processor.updatePerformance(count++);
        assertEquals((850.0 / 650.0 - 1.0), performance.getCurrentRunUp(), 0.0);

        when(processor.totalEquity()).thenReturn(750.0);
        processor.updatePerformance(count++);
        assertEquals((750.0 / 650.0 - 1.0), performance.getCurrentRunUp(), 0.0);

    }
}
