package com.unisoft.algotrader.data;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.unisoft.algotrader.core.Portfolio;
import com.unisoft.algotrader.core.PortfolioManager;
import com.unisoft.algotrader.core.id.InstId;
import com.unisoft.algotrader.event.data.MarketDataContainer;
import com.unisoft.algotrader.provider.SubscriptionKey;
import com.unisoft.algotrader.provider.csv.historical.DummyDataProvider;
import com.unisoft.algotrader.strategy.Strategy;
import com.unisoft.algotrader.threading.disruptor.waitstrategy.NoWaitStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by alex on 6/8/15.
 */
public class CSVPublishTest {


    private static final Logger LOG = LogManager.getLogger(CSVPublishTest.class);

    static class CountDownStrategy extends Strategy {
        private CountDownLatch latch;
        private int exp;
        private int count = 0;

        public CountDownStrategy(String strategyId, String portfolioId, CountDownLatch latch, int exp, RingBuffer... providers){
            super(strategyId, portfolioId, providers);
            this.latch = latch;
            this.exp = exp;
        }

        @Override
        public void onMarketDataContainer(MarketDataContainer data){
            LOG.info("id {}, onMarketDataContainer {}", strategyId, data);
            count ++;
            if (count==exp){
                latch.countDown();
            }
        }
    }


    public static void main(String [] args) throws Exception {

        RingBuffer<MarketDataContainer> marketDataRB
                = RingBuffer.createSingleProducer(MarketDataContainer.FACTORY, 1024, new NoWaitStrategy());

        DummyDataProvider provider = new DummyDataProvider(marketDataRB);

        Portfolio portfolio = new Portfolio("PID1");
        PortfolioManager.INSTANCE.add(portfolio);


        CountDownLatch latch = new CountDownLatch(5);
        CountDownStrategy strategy1 = new CountDownStrategy("strategy1", portfolio.portfolioId, latch, 10, marketDataRB);
        CountDownStrategy strategy2 = new CountDownStrategy("strategy2", portfolio.portfolioId, latch, 10, marketDataRB);
        CountDownStrategy strategy3 = new CountDownStrategy("strategy3", portfolio.portfolioId, latch, 10, marketDataRB);
        CountDownStrategy strategy4 = new CountDownStrategy("strategy4", portfolio.portfolioId, latch, 10, marketDataRB);
        CountDownStrategy strategy5 = new CountDownStrategy("strategy5", portfolio.portfolioId, latch, 10, marketDataRB);

        ExecutorService executor = Executors.newFixedThreadPool(8, DaemonThreadFactory.INSTANCE);
        executor.submit(strategy1);
        executor.submit(strategy2);

        executor.submit(strategy3);
        executor.submit(strategy4);
        executor.submit(strategy5);

        LOG.info("sleep");
        Thread.sleep(5000);

        LOG.info("start");
        provider.subscribe(SubscriptionKey.createDailySubscriptionKey(InstId.Builder.as().symbol("HSI").exchId("HKEX").build()), 20110101, 20141231);

        latch.await();

        LOG.info("done");
    }
}
