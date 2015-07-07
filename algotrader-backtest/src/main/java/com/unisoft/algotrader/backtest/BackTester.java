package com.unisoft.algotrader.backtest;

import com.lmax.disruptor.util.DaemonThreadFactory;
import com.unisoft.algotrader.core.*;
import com.unisoft.algotrader.event.EventBusManager;
import com.unisoft.algotrader.event.data.RingBufferMarketDataEventBus;
import com.unisoft.algotrader.model.refdata.Currency;
import com.unisoft.algotrader.order.OrderManager;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.provider.SubscriptionKey;
import com.unisoft.algotrader.provider.csv.BarFactory;
import com.unisoft.algotrader.provider.csv.InstrumentDataManager;
import com.unisoft.algotrader.provider.csv.historical.HistoricalDataProvider;
import com.unisoft.algotrader.provider.execution.SimulationExecutor;
import com.unisoft.algotrader.provider.execution.Simulator;
import com.unisoft.algotrader.strategy.Strategy;
import com.unisoft.algotrader.strategy.StrategyManager;
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

    private final Account account;
    private final Portfolio portfolio;
    private final Strategy strategy;

    private final HistoricalDataProvider dataProvider;
    private final SimulationExecutor simulationExecutor;
    private final BarFactory barFactory;
    private final Simulator simulator;

    private final int fromDate;
    private final int toDate;

    private final ExecutorService executor = Executors.newFixedThreadPool(2, DaemonThreadFactory.INSTANCE);

    public BackTester(Strategy strategy, HistoricalDataProvider provider, Currency currency, double initialValue, Instrument instrument, int fromDate, int toDate){
        this.instrument = instrument;
        InstrumentManager.INSTANCE.add(instrument);

        this.account = new Account("TestAccount", "TestAccount", currency, initialValue);

        AccountManager.INSTANCE.add(account);
        this.portfolio = new Portfolio("TestPortfolio", account.getAccountId());
        PortfolioManager.INSTANCE.add(portfolio);

        this.strategy = strategy;
        this.strategy.setPortfolio(portfolio);
        StrategyManager.INSTANCE.register(strategy);

        this.simulationExecutor = new SimulationExecutor(OrderManager.INSTANCE, InstrumentDataManager.INSTANCE, EventBusManager.INSTANCE.marketDataRB);
        ProviderManager.INSTANCE.registerExecutionProvider(simulationExecutor);

        this.barFactory = new BarFactory(EventBusManager.INSTANCE.rawMarketDataRB, EventBusManager.INSTANCE.marketDataRB);
        this.simulator = new Simulator(simulationExecutor, EventBusManager.INSTANCE.marketDataRB, strategy);

        this.dataProvider = provider;

        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public BackTester(Strategy strategy, HistoricalDataProvider provider, Account account, Portfolio portfolio, Instrument instrument, int fromDate, int toDate){
        this.instrument = instrument;
        InstrumentManager.INSTANCE.add(instrument);


        this.account = account;
        this.portfolio = portfolio;
        PortfolioManager.INSTANCE.add(portfolio);

        this.strategy =strategy;
        this.strategy.setPortfolio(portfolio);
        StrategyManager.INSTANCE.register(strategy);

        this.simulationExecutor = new SimulationExecutor(OrderManager.INSTANCE, InstrumentDataManager.INSTANCE, EventBusManager.INSTANCE.marketDataRB);
        ProviderManager.INSTANCE.registerExecutionProvider(simulationExecutor);

        this.barFactory = new BarFactory(EventBusManager.INSTANCE.rawMarketDataRB, EventBusManager.INSTANCE.marketDataRB);
        this.simulator = new Simulator(simulationExecutor, EventBusManager.INSTANCE.marketDataRB, strategy);


        this.dataProvider = provider;

        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public void run(){
        executor.submit(barFactory);
        executor.submit(simulator);
        dataProvider.subscribe(new RingBufferMarketDataEventBus(EventBusManager.INSTANCE.rawMarketDataRB), SubscriptionKey.createDailySubscriptionKey(instrument.getInstId()), fromDate, toDate);
    }

    public Performance getPerformance(){
        return portfolio.getPerformance();
    }

}
