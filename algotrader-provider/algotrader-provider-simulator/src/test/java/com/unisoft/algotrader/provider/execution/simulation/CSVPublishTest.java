package com.unisoft.algotrader.provider.execution.simulation;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.unisoft.algotrader.event.SampleEventFactory;
import com.unisoft.algotrader.event.bus.RingBufferMarketDataEventBus;
import com.unisoft.algotrader.model.event.data.MarketDataContainer;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.model.trading.Portfolio;
import com.unisoft.algotrader.persistence.InMemoryTradingDataStore;
import com.unisoft.algotrader.persistence.TradingDataStore;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.provider.data.DummyDataProvider;
import com.unisoft.algotrader.provider.data.HistoricalSubscriptionKey;
import com.unisoft.algotrader.trading.Strategy;
import com.unisoft.algotrader.utils.DateHelper;
import com.unisoft.algotrader.utils.threading.disruptor.MultiEventProcessor;
import com.unisoft.algotrader.utils.threading.disruptor.waitstrategy.NoWaitStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by alex on 6/8/15.
 */
public class CSVPublishTest {


    private static Instrument testInstrument = SampleEventFactory.TEST_HKD_INSTRUMENT;

    private static final Logger LOG = LogManager.getLogger(CSVPublishTest.class);

    static class CountDownStrategy extends Strategy {
        private CountDownLatch latch;
        private int exp;
        private int count = 0;

        public CountDownStrategy(int strategyId, TradingDataStore tradingDataStore, int portfolioId, CountDownLatch latch, int exp){
            super(strategyId, tradingDataStore, portfolioId);
            this.latch = latch;
            this.exp = exp;
        }

        @Override
        public void onMarketDataContainer(MarketDataContainer data){
            LOG.info("stgId {}, onMarketDataContainer {}", strategyId, data);
            count ++;
            if (count==exp){
                latch.countDown();
            }
        }
    }


    public static void main(String [] args) throws Exception {

        RingBuffer<MarketDataContainer> marketDataRB
                = RingBuffer.createSingleProducer(MarketDataContainer.FACTORY, 1024, new NoWaitStrategy());

        ProviderManager providerManager = new ProviderManager();
        DummyDataProvider provider = new DummyDataProvider(providerManager, new RingBufferMarketDataEventBus(marketDataRB));

        Portfolio portfolio = new Portfolio(1, TradingDataStore.DEFAULT_ACCOUNT.accountId());
        InMemoryTradingDataStore tradingDataStore = new InMemoryTradingDataStore();

        tradingDataStore.savePortfolio(portfolio);


        CountDownLatch latch = new CountDownLatch(5);

        ExecutorService executor = Executors.newFixedThreadPool(8, DaemonThreadFactory.INSTANCE);

//        MultiEventProcessor ep1 = new MultiEventProcessor( new CountDownStrategy(1, tradingDataStore, portfolio.portfolioId(), latch, 10), new NoWaitStrategy(), marketDataRB);
//        MultiEventProcessor ep2 = new MultiEventProcessor( new CountDownStrategy(2, tradingDataStore, portfolio.portfolioId(), latch, 10), new NoWaitStrategy(), marketDataRB);
//        MultiEventProcessor ep3 = new MultiEventProcessor( new CountDownStrategy(3, tradingDataStore, portfolio.portfolioId(), latch, 10), new NoWaitStrategy(), marketDataRB);
//        MultiEventProcessor ep4 = new MultiEventProcessor( new CountDownStrategy(4, tradingDataStore, portfolio.portfolioId(), latch, 10), new NoWaitStrategy(), marketDataRB);
//        MultiEventProcessor ep5 = new MultiEventProcessor( new CountDownStrategy(5, tradingDataStore, portfolio.portfolioId(), latch, 10), new NoWaitStrategy(), marketDataRB);
//
//        executor.submit(ep1);
//        executor.submit(ep2);
//        executor.submit(ep3);
//        executor.submit(ep4);
//        executor.submit(ep5);
//
//        LOG.info("sleep");
//        Thread.sleep(5000);
//
//        LOG.info("start");
//        provider.subscribeHistoricalData(HistoricalSubscriptionKey.createDailySubscriptionKey(provider.providerId().id, testInstrument.getInstId(),
//                DateHelper.fromYYYYMMDD(20110101).getTime(),
//                DateHelper.fromYYYYMMDD(20110111).getTime()));
//
//        latch.await();
//
//        LOG.info("done");
    }
}
