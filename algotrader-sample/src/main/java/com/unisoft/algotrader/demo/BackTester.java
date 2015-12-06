package com.unisoft.algotrader.demo;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.multi.AggregratedEventHandler;
import com.lmax.disruptor.multi.MultiEventProcessor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.unisoft.algotrader.config.*;
import com.unisoft.algotrader.model.event.bus.EventBusManager;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.model.trading.Portfolio;
import com.unisoft.algotrader.persistence.RefDataStore;
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
public class BackTester implements Runnable{

    private final AppConfig appConfig;
    private final BackTestConfig backTestConfig;
    private final ExecutorService executorService;

    private StrategyManager strategyManager;
    private ProviderManager providerManager;
    private RefDataStore refDataStore;
    private EventBusManager eventBusManager;
    private InstrumentDataManager instrumentDataManager;
    private Strategy strategy;
    private CountDownLatch latch;

    @Inject
    public BackTester(
            AppConfig appConfig,
            BackTestConfig backTestConfig,
            ExecutorService executorService, CountDownLatch latch) {
        this.appConfig = appConfig;
        this.backTestConfig = backTestConfig;
        this.executorService = executorService;
        this.latch = latch;
        init();
    }

    public void init(){
        this.strategyManager = appConfig.getStrategyManager();
        this.providerManager = appConfig.getProviderManager();
        this.refDataStore = appConfig.getRefDataStore();
        this.eventBusManager = appConfig.getEventBusManager();
        this.instrumentDataManager = appConfig.getInstrumentDataManager();
        this.strategy = strategyManager.get(backTestConfig.strategyId);
    }


    @Override
    public void run(){
        try {
            createProcessor();

            subscribeData();

            while(!strategy.isCompleted()){
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void createProcessor(){

        MultiEventProcessor processor = new MultiEventProcessor();
        Sequence seq = processor.add(eventBusManager.getMarketDataRB(), new AggregratedEventHandler(instrumentDataManager, strategy));
        eventBusManager.getMarketDataRB().addGatingSequences(seq);

        executorService.submit(processor);

    }

    public void subscribeData(){
        for (long instId : backTestConfig.instIds) {

            HistoricalSubscriptionKey subscriptionKey = HistoricalSubscriptionKey.createDailySubscriptionKey(
                    backTestConfig.marketDataProviderId,
                    instId,
                    backTestConfig.fromDate,
                    backTestConfig.toDate);
            HistoricalDataProvider provider = providerManager.getHistoricalDataProvider(ProviderId.Yahoo.id);
            provider.subscribeHistoricalData(subscriptionKey);
        }
    }


    public static void main(String [] args) throws Exception{

        Injector injector = Guice.createInjector(new BackTestingConfigModule(), new SampleAppConfigModule(), new ServiceConfigModule(), new DefaultEventBusConfigModule(), new DataServiceConfigModule());
        AppConfig appConfig = injector.getInstance(AppConfig.class);

        StrategyManager strategyManager = appConfig.getStrategyManager();

        RefDataStore refDataStore = appConfig.getRefDataStore();


        CountDownLatch latch = new CountDownLatch(1);

        Strategy strategy = new CountDownStrategy(appConfig.getOrderManager(),
                strategyManager.nextStrategyId(),
                Portfolio.DEFAULT_PORTFOLIO_ID, ProviderId.Simulation.id, appConfig.getTradingDataStore(), latch, 20);

        strategyManager.register(strategy);

        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange("0005.HK", HKEX.getExchId());

        BackTestConfig backTestConfig = new BackTestConfig(ProviderId.Yahoo.id,
                strategy.strategyId, DateHelper.fromYYYYMMDD(20100101), DateHelper.fromYYYYMMDD(20160101), instrument.getInstId());
        ExecutorService executor = Executors.newFixedThreadPool(2, DaemonThreadFactory.INSTANCE);

        BackTester backTester = new BackTester(appConfig, backTestConfig, executor, latch);

        backTester.run();
    }
}
