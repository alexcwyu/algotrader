package com.unisoft.algotrader.demo;

import com.unisoft.algotrader.backtest.BackTester;
import com.unisoft.algotrader.model.refdata.Currency;
import com.unisoft.algotrader.core.Performance;
import com.unisoft.algotrader.event.EventBusManager;
import com.unisoft.algotrader.provider.csv.historical.DummyDataProvider;
import com.unisoft.algotrader.series.TimeSeriesHelper;
import com.unisoft.algotrader.strategy.Strategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CountDownLatch;

/**
 * Created by alex on 6/16/15.
 */
public class BackTesterDemo {


    private static final Logger LOG = LogManager.getLogger(BackTesterDemo.class);




    public static void main(String [] args) throws Exception{
        DummyDataProvider provider = new DummyDataProvider();

        CountDownLatch latch = new CountDownLatch(1);
        Strategy strategy = new CountDownStrategy("Sid", latch, 20, EventBusManager.INSTANCE.marketDataRB);

        BackTester backTester = new BackTester(strategy, provider, Currency.HKD, 100000, Sample.testInstrument, 20110101, 20110111);

        backTester.run();

        latch.await();
        logPerformance(backTester.getPerformance());

        LOG.info("done");
    }

    private static void logPerformance(Performance performance){

        LOG.info("Performance:\nEquity\n{}\nPnl\n{}\nDrawdown\n{}"
                , TimeSeriesHelper.print(performance.getEquitySeries())
                , TimeSeriesHelper.print(performance.getPnlSeries())
                , TimeSeriesHelper.print(performance.getDrawdownSeries()));
    }
}
