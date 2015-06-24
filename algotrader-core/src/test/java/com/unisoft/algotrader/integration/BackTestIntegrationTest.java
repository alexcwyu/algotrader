package com.unisoft.algotrader.integration;

import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.core.*;
import com.unisoft.algotrader.event.data.MarketDataContainer;
import com.unisoft.algotrader.order.OrderManager;
import com.unisoft.algotrader.provider.csv.InstrumentDataManager;
import com.unisoft.algotrader.provider.execution.SimulationExecutor;
import com.unisoft.algotrader.strategy.StrategyManager;
import com.unisoft.algotrader.threading.disruptor.waitstrategy.NoWaitStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

/**
 * Created by alex on 6/7/15.
 */
public class BackTestIntegrationTest {

    private static final Logger LOG = LogManager.getLogger(BackTestIntegrationTest.class);

    public static void main(String [] args) throws Exception{

        Instrument testInstrument = InstrumentManager.INSTANCE.createStock("TestInst", "TestExch", Currency.USD.ccyId);

        ExecutorService executor;
        RingBuffer<MarketDataContainer> marketDataRB;
        Account account;
        Portfolio portfolio;
        BuyAndHoldStrategy strategy;
        OrderManager orderManager;
        SimulationExecutor simulationExecutor;
        InstrumentDataManager instrumentDataManager;
        DataPublisher dataPublisher;

        executor = Executors.newFixedThreadPool(8);

        marketDataRB
                = RingBuffer.createSingleProducer(MarketDataContainer.FACTORY, 1024, new NoWaitStrategy());

        instrumentDataManager = new InstrumentDataManager(marketDataRB);

        account = new Account("Test Account", "Test Account", Currency.USD, 1000000);

        portfolio = new Portfolio("Test Portfolio", account, marketDataRB);
        PortfolioManager.INSTANCE.add(portfolio);

        orderManager = new OrderManager();

        strategy = new BuyAndHoldStrategy(orderManager, portfolio.portfolioId, marketDataRB);


        simulationExecutor = new SimulationExecutor(orderManager, instrumentDataManager, marketDataRB);

        dataPublisher = new DataPublisher(marketDataRB);

        PortfolioManager.INSTANCE.add(portfolio);
        StrategyManager.INSTANCE.register(strategy);

        executor.submit(strategy);
        executor.submit(portfolio);
        executor.submit(simulationExecutor);
        executor.submit(instrumentDataManager);

        assertEquals(0.0, portfolio.positionValue(), 0.0);

        executor.submit(()->{

            for (int i =0; i< 100; i ++){
                dataPublisher.publishTrade(testInstrument.instId, System.currentTimeMillis(), 100+i, 1000);
                try {
                    Thread.sleep(1000);
                }catch(Exception e){

                }
            }
        });

    }
}
