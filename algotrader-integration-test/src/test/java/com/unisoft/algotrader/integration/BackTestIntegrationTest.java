package com.unisoft.algotrader.integration;

import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.model.event.data.MarketDataContainer;
import com.unisoft.algotrader.model.refdata.Currency;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.model.trading.Account;
import com.unisoft.algotrader.model.trading.Portfolio;
import com.unisoft.algotrader.provider.InstrumentDataManager;
import com.unisoft.algotrader.provider.execution.simulation.SimulationExecutor;
import com.unisoft.algotrader.refdata.AccountManager;
import com.unisoft.algotrader.refdata.InstrumentManager;
import com.unisoft.algotrader.strategy.StrategyManager;
import com.unisoft.algotrader.trading.OrderManager;
import com.unisoft.algotrader.trading.PortfolioManager;
import com.unisoft.algotrader.trading.PortfolioProcessor;
import com.unisoft.algotrader.utils.threading.disruptor.waitstrategy.NoWaitStrategy;
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

        Instrument testInstrument = InstrumentManager.INSTANCE.createStock("TestInst", "TestExch", Currency.USD.getCcyId());

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

        account = new Account("Test Account", "Test Account", Currency.USD, 1000000);

        AccountManager.INSTANCE.add(account);
        portfolio = new Portfolio("Test Portfolio", account.getAccountId());
        portfolioProcessor = new PortfolioProcessor(portfolio, marketDataRB);
        PortfolioManager.INSTANCE.add(portfolio);

        orderManager = new OrderManager();

        strategy = new BuyAndHoldStrategy(orderManager, portfolio.getPortfolioId(), marketDataRB);


        simulationExecutor = new SimulationExecutor(orderManager, instrumentDataManager, marketDataRB);

        dataPublisher = new DataPublisher(marketDataRB);

        PortfolioManager.INSTANCE.add(portfolio);
        StrategyManager.INSTANCE.register(strategy);

        executor.submit(strategy);
        executor.submit(portfolioProcessor);
        executor.submit(simulationExecutor);
        executor.submit(instrumentDataManager);

        assertEquals(0.0, portfolioProcessor.positionValue(), 0.0);

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
