package com.unisoft.algotrader.demo;

import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.event.SampleEventFactory;
import com.unisoft.algotrader.model.clock.Clock;
import com.unisoft.algotrader.model.event.data.MarketDataContainer;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.model.trading.Account;
import com.unisoft.algotrader.model.trading.Portfolio;
import com.unisoft.algotrader.persistence.InMemoryTradingDataStore;
import com.unisoft.algotrader.persistence.SampleInMemoryRefDataStore;
import com.unisoft.algotrader.persistence.TradingDataStore;
import com.unisoft.algotrader.provider.InstrumentDataManager;
import com.unisoft.algotrader.provider.execution.simulation.SimulationExecutor;
import com.unisoft.algotrader.trading.OrderManager;
import com.unisoft.algotrader.trading.PortfolioProcessor;
import com.unisoft.algotrader.trading.StrategyManager;
import com.unisoft.algotrader.utils.threading.disruptor.waitstrategy.NoWaitStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by alex on 6/7/15.
 */
public class StrategyRunner {

    private static final Logger LOG = LogManager.getLogger(StrategyRunner.class);

    public static void main(String [] args) throws Exception{

        Instrument testInstrument = SampleEventFactory.TEST_HKD_INSTRUMENT;

        ExecutorService executor;
        RingBuffer<MarketDataContainer> marketDataRB;
        Account account;
        Portfolio portfolio;
        PortfolioProcessor portfolioProcessor;
        BuyAndHoldStrategy strategy;
        OrderManager orderManager;
        SimulationExecutor simulationExecutor;
        InstrumentDataManager instrumentDataManager;
        DataPublisher dataPublisher;

        executor = Executors.newFixedThreadPool(8);

        marketDataRB
                = RingBuffer.createSingleProducer(MarketDataContainer.FACTORY, 1024, new NoWaitStrategy());

        instrumentDataManager = new InstrumentDataManager(marketDataRB);

        account = TradingDataStore.DEFAULT_ACCOUNT;

        portfolio = new Portfolio("Test Portfolio", account.getAccountId());
        portfolioProcessor = new PortfolioProcessor(portfolio, account, new SampleInMemoryRefDataStore(), Clock.CLOCK, marketDataRB);

        TradingDataStore tradingDataStore = new InMemoryTradingDataStore();
        tradingDataStore.savePortfolio(portfolio);


        orderManager = new OrderManager();

        strategy = new BuyAndHoldStrategy(orderManager, tradingDataStore, portfolio.getPortfolioId(), marketDataRB);


        simulationExecutor = new SimulationExecutor(orderManager, instrumentDataManager, marketDataRB);

        dataPublisher = new DataPublisher(marketDataRB);

        StrategyManager.INSTANCE.register(strategy);

        executor.submit(strategy);
        executor.submit(portfolioProcessor);
        executor.submit(simulationExecutor);
        executor.submit(instrumentDataManager);



        executor.submit(()->{

            for (int i =0; i< 100; i ++){
                dataPublisher.publishTrade(testInstrument.getInstId(), System.currentTimeMillis(), 100+i, 1000);
                try {
                    Thread.sleep(1000);
                }catch(Exception e){

                }
            }
        });

    }
}
