package com.unisoft.algotrader.demo;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.unisoft.algotrader.config.AppConfig;
import com.unisoft.algotrader.config.SampleAppConfigModule;
import com.unisoft.algotrader.config.ServiceConfigModule;
import com.unisoft.algotrader.event.bus.RingBufferMarketDataEventBus;
import com.unisoft.algotrader.model.series.TimeSeriesHelper;
import com.unisoft.algotrader.model.trading.Account;
import com.unisoft.algotrader.model.trading.Performance;
import com.unisoft.algotrader.model.trading.Portfolio;
import com.unisoft.algotrader.persistence.TradingDataStore;
import com.unisoft.algotrader.provider.config.DataServiceConfigModule;
import com.unisoft.algotrader.provider.data.DummyDataProvider;
import com.unisoft.algotrader.trading.Strategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CountDownLatch;

/**
 * Created by alex on 6/16/15.
 */
public class BackTesterMain {

    private static final Logger LOG = LogManager.getLogger(BackTesterMain.class);

    public static void main(String [] args) throws Exception{


        Injector injector = Guice.createInjector(new SampleAppConfigModule(), new ServiceConfigModule(), new DataServiceConfigModule());
        AppConfig appConfig = injector.getInstance(AppConfig.class);
        TradingDataStore tradingDataStore = appConfig.getTradingDataStore();



        DummyDataProvider provider = new DummyDataProvider(appConfig.getProviderManager(), new RingBufferMarketDataEventBus(appConfig.getEventBusManager().getMarketDataRB()));

        Account account = Account.DEFAULT;
        Portfolio portfolio = new Portfolio(1, account.accountId());
        tradingDataStore.savePortfolio(portfolio);

        CountDownLatch latch = new CountDownLatch(1);
        Strategy strategy = new CountDownStrategy(appConfig.getOrderManager(), 1, 1, appConfig.getTradingDataStore(), latch, 20);


        BackTester backTester = new BackTester(appConfig, strategy, provider, portfolio, SampleEventFactory.TEST_HKD_INSTRUMENT, 20110101, 20110111);

        backTester.run();

        latch.await();
        logPerformance(backTester.getPerformance());

        LOG.info("done");
    }

    private static void logPerformance(Performance performance){

        LOG.info("Performance:\nEquity\n{}\nPnl\n{}\nDrawdown\n{}"
                , TimeSeriesHelper.print(performance.equitySeries())
                , TimeSeriesHelper.print(performance.pnlSeries())
                , TimeSeriesHelper.print(performance.drawdownSeries()));
    }
}
