package com.unisoft.algotrader.demo;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.unisoft.algotrader.config.*;
import com.unisoft.algotrader.model.event.bus.EventBusManager;
import com.unisoft.algotrader.model.event.data.MarketDataContainer;
import com.unisoft.algotrader.model.refdata.Currency;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.model.trading.Account;
import com.unisoft.algotrader.model.trading.Performance;
import com.unisoft.algotrader.model.trading.Portfolio;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.persistence.TradingDataStore;
import com.unisoft.algotrader.provider.ProviderId;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.provider.config.DataServiceConfigModule;
import com.unisoft.algotrader.provider.data.*;
import com.unisoft.algotrader.trading.InstrumentDataManager;
import com.unisoft.algotrader.trading.PortfolioProcessor;
import com.unisoft.algotrader.trading.Strategy;
import com.unisoft.algotrader.trading.StrategyManager;
import com.unisoft.algotrader.utils.DateHelper;

import java.util.List;
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
    private DataService dataService;

    private StrategyManager strategyManager;
    private ProviderManager providerManager;
    private RefDataStore refDataStore;
    private EventBusManager eventBusManager;
    private TradingDataStore tradingDataStore;
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
        this.dataService = appConfig.getDataService();
        this.strategyManager = appConfig.getStrategyManager();
        this.providerManager = appConfig.getProviderManager();
        this.refDataStore = appConfig.getRefDataStore();
        this.eventBusManager = appConfig.getEventBusManager();
        this.instrumentDataManager = appConfig.getInstrumentDataManager();
        this.strategy = strategyManager.get(backTestConfig.strategyId);
        this.tradingDataStore = appConfig.getTradingDataStore();
    }


    public void printPerformance(Performance performance){


        System.out.println(performance);
//        System.out.println(performance.pnl());
//        System.out.println(performance.equity());
//        System.out.println(performance.coreEquity());
//        System.out.println(performance.highEquity());
//        System.out.println(performance.lowEquity());
//        System.out.println(performance.drawdown());
//        System.out.println(performance.drawdownPercent());
//        System.out.println(performance.currentDrawdown());
//        System.out.println(performance.currentRunUp());
    }

    @Override
    public void run() {

        Portfolio portfolio = this.tradingDataStore.getPortfolio(backTestConfig.portfolioId);

        Account account = this.tradingDataStore.getAccount(portfolio.accountId());

        PortfolioProcessor portfolioProcessor = new PortfolioProcessor(appConfig, portfolio, account);

        appConfig.getPortfolioProcessorManager().register(portfolioProcessor);

        Performance performance = portfolio.performance();

        printPerformance(performance);

        List<MarketDataContainer> list = loadData();


        for (MarketDataContainer data : list) {
            instrumentDataManager.onMarketDataContainer(data);
            portfolioProcessor.onMarketDataContainer(data);
            strategy.onMarketDataContainer(data);
        }
        printPerformance(performance);

        System.out.println("Done!");
    }


//
//    public void createProcessor(){
//
//        MultiEventProcessor processor = new MultiEventProcessor();
//        Sequence seq = processor.add(eventBusManager.getMarketDataRB(), new AggregratedEventHandler(instrumentDataManager, strategy));
//        eventBusManager.getMarketDataRB().addGatingSequences(seq);
//
//        executorService.submit(processor);
//
//    }

    public List<MarketDataContainer> loadData(){
        HistoricalSubscriptionKey[] subscriptionKeys = new HistoricalSubscriptionKey[backTestConfig.instIds.length];
        int i =0;
        for (long instId : backTestConfig.instIds) {
            subscriptionKeys[i++] = HistoricalSubscriptionKey.createDailySubscriptionKey(
                    backTestConfig.marketDataProviderId,
                    instId,
                    backTestConfig.fromDate,
                    backTestConfig.toDate);
        }
        return HistoricalDataUtils.sortedHistoricalData(dataService.loadAllHistoricalData(subscriptionKeys));
    }


    public static void main(String [] args) throws Exception{

        Injector injector = Guice.createInjector(new BackTestingConfigModule(), new SampleAppConfigModule(), new ServiceConfigModule(), new DefaultEventBusConfigModule(), new DataServiceConfigModule());
        AppConfig appConfig = injector.getInstance(AppConfig.class);

        StrategyManager strategyManager = appConfig.getStrategyManager();
        RefDataStore refDataStore = appConfig.getRefDataStore();

        CountDownLatch latch = new CountDownLatch(1);

        Strategy strategy = new CountDownStrategy(appConfig.getOrderManager(),
                strategyManager.nextStrategyId(),
                Portfolio.TEST_HKD_PORTFOLIO_ID, ProviderId.Simulation.id, appConfig.getTradingDataStore(), latch, 20);

        strategyManager.register(strategy);

        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange("0005.HK", HKEX.getExchId());

        BackTestConfig backTestConfig = new BackTestConfig(ProviderId.Yahoo.id,
                strategy.strategyId,
                DateHelper.fromYYYYMMDD(20100101), DateHelper.fromYYYYMMDD(20160101),
                Account.TEST_HKD_ACCOUNT_ID, Currency.HKD, 1000000, Portfolio.TEST_HKD_PORTFOLIO_ID,
                instrument.getInstId());
        ExecutorService executor = Executors.newFixedThreadPool(2, DaemonThreadFactory.INSTANCE);

        BackTester backTester = new BackTester(appConfig, backTestConfig, executor, latch);
        backTester.run();
    }
}

