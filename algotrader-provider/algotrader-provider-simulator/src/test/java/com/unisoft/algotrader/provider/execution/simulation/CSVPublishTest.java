package com.unisoft.algotrader.provider.execution.simulation;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.multi.MultiEventProcessor;
import com.lmax.disruptor.multi.NoWaitStrategy;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.unisoft.algotrader.event.SampleEventFactory;
import com.unisoft.algotrader.event.bus.RingBufferMarketDataEventBus;
import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.MarketDataContainer;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.model.trading.Account;
import com.unisoft.algotrader.model.trading.Portfolio;
import com.unisoft.algotrader.persistence.InMemoryTradingDataStore;
import com.unisoft.algotrader.persistence.TradingDataStore;
import com.unisoft.algotrader.provider.ProviderId;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.provider.data.DummyDataProvider;
import com.unisoft.algotrader.provider.data.HistoricalSubscriptionKey;
import com.unisoft.algotrader.trading.Strategy;
import com.unisoft.algotrader.utils.DateHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by alex on 6/8/15.
 */
public class CSVPublishTest {


    private static Instrument testInstrument1 = SampleEventFactory.TEST_HKD_INSTRUMENT;
    private static Instrument testInstrument2 = SampleEventFactory.TEST_USD_INSTRUMENT;

    private static final Logger LOG = LogManager.getLogger(CSVPublishTest.class);

    static class CountDownStrategy extends Strategy {
        private CountDownLatch latch;
        private int exp1;
        private int count1 = 0;
        private int exp2;
        private int count2 = 0;

        public CountDownStrategy(int strategyId, TradingDataStore tradingDataStore, int portfolioId, CountDownLatch latch, int exp1, int exp2){
            super(strategyId, tradingDataStore, portfolioId);
            this.latch = latch;
            this.exp1 = exp1;
            this.exp2 = exp2;
        }

//        @Override
//        public void onMarketDataContainer(MarketDataContainer data){
//            LOG.info("stgId {}, onMarketDataContainer {}", strategyId, data);
//            if (data.instId == testInstrument1.getInstId()){
//                count1++;
//            }
//            else if (data.instId == testInstrument2.getInstId()){
//                count2++;
//            }
//            if (count1>=exp1 && count2>=exp2){
//                latch.countDown();
//            }
//        }

        @Override
        public void onBar(Bar bar) {
            LOG.info("stgId {}, onBar {}", strategyId, bar);
            if (bar.instId == testInstrument1.getInstId()){
                count1++;
            }
            else if (bar.instId == testInstrument2.getInstId()){
                count2++;
            }
            if (count1>=exp1 && count2>=exp2){
                latch.countDown();
            }
        }
    }


    public static void main(String [] args) throws Exception {

        RingBuffer<MarketDataContainer> marketDataRB1
                = RingBuffer.createSingleProducer(MarketDataContainer.FACTORY, 1024, new NoWaitStrategy());
        RingBuffer<MarketDataContainer> marketDataRB2
                = RingBuffer.createSingleProducer(MarketDataContainer.FACTORY, 1024, new NoWaitStrategy());

        ProviderManager providerManager = new ProviderManager();
        DummyDataProvider provider1 = new DummyDataProvider(ProviderId.Dummy, providerManager, new RingBufferMarketDataEventBus(marketDataRB1));
        DummyDataProvider provider2 = new DummyDataProvider(ProviderId.Dummy1, providerManager, new RingBufferMarketDataEventBus(marketDataRB2));

        Portfolio portfolio = new Portfolio(1, Account.DEFAULT.accountId());
        InMemoryTradingDataStore tradingDataStore = new InMemoryTradingDataStore();

        tradingDataStore.savePortfolio(portfolio);


        CountDownLatch latch = new CountDownLatch(5);

        ExecutorService executor = Executors.newFixedThreadPool(8, DaemonThreadFactory.INSTANCE);

        MultiEventProcessor ep1 = new MultiEventProcessor();
        MultiEventProcessor ep2 = new MultiEventProcessor();
        MultiEventProcessor ep3 = new MultiEventProcessor();
        MultiEventProcessor ep4 = new MultiEventProcessor();
        MultiEventProcessor ep5 = new MultiEventProcessor();

        Strategy s1 = new CountDownStrategy(1, tradingDataStore, portfolio.portfolioId(), latch, 10, 10);
        Strategy s2 = new CountDownStrategy(2, tradingDataStore, portfolio.portfolioId(), latch, 10, 10);
        Strategy s3 = new CountDownStrategy(3, tradingDataStore, portfolio.portfolioId(), latch, 10, 10);
        Strategy s4 = new CountDownStrategy(4, tradingDataStore, portfolio.portfolioId(), latch, 10, 10);
        Strategy s5 = new CountDownStrategy(5, tradingDataStore, portfolio.portfolioId(), latch, 10, 10);

        ep1.add(marketDataRB1, s1);
        ep2.add(marketDataRB1, s2);
        ep3.add(marketDataRB1, s3);
        ep4.add(marketDataRB1, s4);
        ep5.add(marketDataRB1, s5);


        ep1.add(marketDataRB2, s1);
        ep2.add(marketDataRB2, s2);
        ep3.add(marketDataRB2, s3);
        ep4.add(marketDataRB2, s4);
        ep5.add(marketDataRB2, s5);


        executor.submit(ep1);
        executor.submit(ep2);
        executor.submit(ep3);
        executor.submit(ep4);
        executor.submit(ep5);

        LOG.info("sleep");
        Thread.sleep(5000);

        LOG.info("start");
        provider1.subscribeHistoricalData(HistoricalSubscriptionKey.createDailySubscriptionKey(provider1.providerId().id, testInstrument1.getInstId(),
                DateHelper.fromYYYYMMDD(20110101).getTime(),
                DateHelper.fromYYYYMMDD(20110111).getTime()));

        provider2.subscribeHistoricalData(HistoricalSubscriptionKey.createDailySubscriptionKey(provider2.providerId().id, testInstrument2.getInstId(),
                DateHelper.fromYYYYMMDD(20120101).getTime(),
                DateHelper.fromYYYYMMDD(20120111).getTime()));

        latch.await();

        LOG.info("done");
    }
}
