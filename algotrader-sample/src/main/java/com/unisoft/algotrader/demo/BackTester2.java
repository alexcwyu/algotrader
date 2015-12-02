package com.unisoft.algotrader.demo;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.multi.MultiEventProcessor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.unisoft.algotrader.config.*;
import com.unisoft.algotrader.model.event.bus.EventBusManager;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.model.trading.Portfolio;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.Provider;
import com.unisoft.algotrader.provider.ProviderId;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.provider.config.DataServiceConfigModule;
import com.unisoft.algotrader.provider.data.HistoricalDataProvider;
import com.unisoft.algotrader.provider.data.HistoricalSubscriptionKey;
import com.unisoft.algotrader.trading.InstrumentDataManager;
import com.unisoft.algotrader.trading.Strategy;
import com.unisoft.algotrader.trading.StrategyManager;
import com.unisoft.algotrader.utils.DateHelper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.unisoft.algotrader.model.refdata.Exchange.HKEX;

/**
 * Created by alex on 11/30/15.
 */
public class BackTester2 {


    public static void main(String [] args) throws Exception{

        Injector injector = Guice.createInjector(new BackTestingConfigModule(), new SampleAppConfigModule(), new ServiceConfigModule(), new DefaultEventBusConfigModule(), new DataServiceConfigModule());
        AppConfig appConfig = injector.getInstance(AppConfig.class);

        final ExecutorService executor = Executors.newFixedThreadPool(2, DaemonThreadFactory.INSTANCE);

        StrategyManager strategyManager = appConfig.getStrategyManager();
        ProviderManager providerManager = appConfig.getProviderManager();

        //DataService dataService = injector.getInstance(DataService.class);
        RefDataStore refDataStore = injector.getInstance(RefDataStore.class);
        EventBusManager eventBusManager = injector.getInstance(EventBusManager.class);


        CountDownLatch latch = new CountDownLatch(1);

        /**
         * setup strategy....this should be register before....or via service lookup mechanism
         */
        Strategy strategy = new CountDownStrategy(appConfig.getOrderManager(),
                strategyManager.nextStrategyId(),
                Portfolio.DEFAULT_PORTFOLIO_ID, ProviderId.Simulation.id, appConfig.getTradingDataStore(), latch, 20);

        strategyManager.register(strategy);

        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange("0005.HK", HKEX.getExchId());

        /**
         * init backtest config
         */
        BackTestConfig backTestConfig = new BackTestConfig(ProviderId.Yahoo.id,
                strategy.strategyId, DateHelper.fromYYYYMMDD(20100101), DateHelper.fromYYYYMMDD(20160101), instrument.getInstId());

        Provider executionDataProvider = providerManager.getProvider(backTestConfig.executionDataProviderId);
        Provider marketDataProvider = providerManager.getProvider(backTestConfig.marketDataProviderId);
        Strategy backTestStrategy = strategyManager.get(backTestConfig.strategyId);


        /**
         * wire up backtest
         */

        InstrumentDataManager instrumentDataManager = appConfig.getInstrumentDataManager();

        SequenceBarrier instDataProcessorSB = eventBusManager.getMarketDataRB().newBarrier();
        MultiEventProcessor instDataProcessor = new MultiEventProcessor();
        Sequence instDataProcessorSeq = instDataProcessor.add(eventBusManager.getMarketDataRB(), instDataProcessorSB, instrumentDataManager);


        SequenceBarrier strategySB = eventBusManager.getMarketDataRB().newBarrier(instDataProcessorSeq);
        MultiEventProcessor strategyProcessor = new MultiEventProcessor();
        Sequence strategySeq = strategyProcessor.add(eventBusManager.getMarketDataRB(), strategySB, strategy);
        eventBusManager.getMarketDataRB().addGatingSequences(strategySeq);

        executor.submit(instDataProcessor);
        executor.submit(strategyProcessor);



        /**
         * subscribe historical data
         */

        for (long instId : backTestConfig.instIds) {

            HistoricalSubscriptionKey subscriptionKey = HistoricalSubscriptionKey.createDailySubscriptionKey(
                    backTestConfig.marketDataProviderId,
                    instId,
                    backTestConfig.fromDate,
                    backTestConfig.toDate);
            HistoricalDataProvider provider = providerManager.getHistoricalDataProvider(ProviderId.Yahoo.id);
            provider.subscribeHistoricalData(subscriptionKey);
        }

        latch.await();
        System.out.println(marketDataProvider);
    }
}
