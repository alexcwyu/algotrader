package com.unisoft.algotrader.demo;

import com.unisoft.algotrader.event.EventBusManager;
import com.unisoft.algotrader.model.refdata.Currency;
import com.unisoft.algotrader.model.series.TimeSeriesHelper;
import com.unisoft.algotrader.model.trading.Performance;
import com.unisoft.algotrader.persistence.InMemoryTradingDataStore;
import com.unisoft.algotrader.persistence.TradingDataStore;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.provider.historical.DummyDataProvider;
import com.unisoft.algotrader.trading.OrderManager;
import com.unisoft.algotrader.trading.Strategy;
import com.unisoft.algotrader.trading.StrategyManager;
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

        ProviderManager providerManager = new ProviderManager();
        StrategyManager strategyManager = new StrategyManager();
        EventBusManager eventBusManager = new EventBusManager();
        OrderManager orderManager = new OrderManager(providerManager, strategyManager, eventBusManager);

        TradingDataStore tradingDataStore = new InMemoryTradingDataStore();
        CountDownLatch latch = new CountDownLatch(1);
        Strategy strategy = new CountDownStrategy(orderManager, "Sid", tradingDataStore, latch, 20, eventBusManager.marketDataRB);

        BackTester backTester = new BackTester(providerManager, orderManager, strategyManager, eventBusManager, strategy, provider, Currency.HKD, 100000, Sample.testInstrument, 20110101, 20110111);

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
