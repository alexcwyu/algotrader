package com.unisoft.algotrader.demo;

import com.lmax.disruptor.util.DaemonThreadFactory;
import com.unisoft.algotrader.config.AppConfig;
import com.unisoft.algotrader.event.EventBusManager;
import com.unisoft.algotrader.event.RingBufferMarketDataEventBus;
import com.unisoft.algotrader.model.clock.SimulationClock;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.model.trading.Performance;
import com.unisoft.algotrader.model.trading.Portfolio;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.persistence.TradingDataStore;
import com.unisoft.algotrader.provider.BarFactory;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.provider.data.HistoricalDataProvider;
import com.unisoft.algotrader.provider.data.HistoricalSubscriptionKey;
import com.unisoft.algotrader.provider.data.Subscriber;
import com.unisoft.algotrader.provider.execution.simulation.SimulationExecutor;
import com.unisoft.algotrader.provider.execution.simulation.Simulator;
import com.unisoft.algotrader.trading.InstrumentDataManager;
import com.unisoft.algotrader.trading.OrderManager;
import com.unisoft.algotrader.trading.Strategy;
import com.unisoft.algotrader.trading.StrategyManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by alex on 6/16/15.
 */
public class BackTester {

    private static final Logger LOG = LogManager.getLogger(BackTester.class);
    private final Instrument instrument;

    private final Portfolio portfolio;
    private final Strategy strategy;

    private final HistoricalDataProvider dataProvider;
    private final SimulationExecutor simulationExecutor;
    private final BarFactory barFactory;
    private final Simulator simulator;
    private final RefDataStore refDataStore;
    private final TradingDataStore tradingDataStore;
    private final ProviderManager providerManager;
    private final OrderManager orderManager;
    private final StrategyManager strategyManager;
    private final EventBusManager eventBusManager;
    private final int fromDate;
    private final int toDate;

    private final ExecutorService executor = Executors.newFixedThreadPool(2, DaemonThreadFactory.INSTANCE);


    public BackTester(AppConfig appConfig,
                      Strategy strategy, HistoricalDataProvider provider, Portfolio portfolio, Instrument instrument, int fromDate, int toDate){
        this(appConfig.getProviderManager(), appConfig.getOrderManager(), appConfig.getStrategyManager(), appConfig.getEventBusManager(),
                appConfig.getRefDataStore(), appConfig.getTradingDataStore(),
                strategy, provider, portfolio,
                instrument, fromDate, toDate);

    }

    public BackTester(ProviderManager providerManager, OrderManager orderManager,
                      StrategyManager strategyManager, EventBusManager eventBusManager,
                      RefDataStore refDataStore, TradingDataStore tradingDataStore,
                              Strategy strategy, HistoricalDataProvider provider, Portfolio portfolio, Instrument instrument, int fromDate, int toDate){
        this.providerManager = providerManager;
        this.orderManager = orderManager;
        this.strategyManager = strategyManager;
        this.eventBusManager = eventBusManager;
        this.refDataStore = refDataStore;
        this.tradingDataStore = tradingDataStore;

        this.instrument = instrument;
        this.refDataStore.saveInstrument(instrument);
        this.portfolio = portfolio;
        this.tradingDataStore.savePortfolio(portfolio);

        this.strategy = strategy;
        this.strategy.setPortfolio(portfolio);
        strategyManager.register(strategy);

        this.simulationExecutor = new SimulationExecutor(providerManager, orderManager, new InstrumentDataManager(eventBusManager.marketDataRB), new SimulationClock(), eventBusManager.marketDataRB);
        providerManager.addExecutionProvider(simulationExecutor);

        this.barFactory = new BarFactory(eventBusManager.rawMarketDataRB, eventBusManager.marketDataRB);
        this.simulator = new Simulator(simulationExecutor, eventBusManager.marketDataRB, strategy);

        this.dataProvider = provider;

        this.fromDate = fromDate;
        this.toDate = toDate;
    }


    public void run(){
        executor.submit(barFactory);
        executor.submit(simulator);
        dataProvider.subscribeHistoricalData(HistoricalSubscriptionKey.createDailySubscriptionKey(dataProvider.providerId(), instrument.getInstId(), fromDate, toDate), new Subscriber(new RingBufferMarketDataEventBus(eventBusManager.rawMarketDataRB)));
    }

    public Performance getPerformance(){
        return portfolio.getPerformance();
    }

}
