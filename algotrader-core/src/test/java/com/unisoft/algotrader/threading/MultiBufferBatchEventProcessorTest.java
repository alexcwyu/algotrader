package com.unisoft.algotrader.threading;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.unisoft.algotrader.event.Event;
import com.unisoft.algotrader.event.data.*;
import com.unisoft.algotrader.threading.disruptor.waitstrategy.DataPublisher;
import com.unisoft.algotrader.threading.disruptor.waitstrategy.MultiBufferWaitStrategy;
import com.unisoft.algotrader.threading.disruptor.waitstrategy.NoWaitStrategy;
import com.unisoft.algotrader.threading.disruptor.waitstrategy.YieldMultiBufferWaitStrategy;

import java.util.concurrent.*;

/**
 * Created by alex on 4/12/15.
 */
public class MultiBufferBatchEventProcessorTest {

    public static final int RUNS = 100;
    private static final int NUM_PUBLISHERS = 3;
    private static final int NUM_PROCESSERS= 3;
    private static final int BUFFER_SIZE = 1024 * 8;
    private static final long ITERATIONS = 1000L * 1000L * 100L ;
    private final ExecutorService executor = Executors.newFixedThreadPool(NUM_PUBLISHERS + NUM_PROCESSERS, DaemonThreadFactory.INSTANCE);
    private final CyclicBarrier cyclicBarrier = new CyclicBarrier(NUM_PUBLISHERS + 1);

    private final RingBuffer<Bar> barRB = RingBuffer.createSingleProducer(Bar.FACTORY, BUFFER_SIZE, new NoWaitStrategy());
    private final RingBuffer<Quote> quoteRB = RingBuffer.createSingleProducer(Quote.FACTORY, BUFFER_SIZE, new NoWaitStrategy());
    private final RingBuffer<Trade> tradeRB = RingBuffer.createSingleProducer(Trade.FACTORY, BUFFER_SIZE, new NoWaitStrategy());

    private final SequenceBarrier barBarrier = barRB.newBarrier();
    private final SequenceBarrier quoteBarrier = quoteRB.newBarrier();
    private final SequenceBarrier tradeBarrier = tradeRB.newBarrier();

    private final DataPublisher<Bar> barDataPublisher = new DataPublisher(cyclicBarrier,
            barRB,
            ITERATIONS);
    private final DataPublisher<Quote> quoteDataPublisher = new DataPublisher(cyclicBarrier,
            quoteRB,
            ITERATIONS);
    private final DataPublisher<Trade> tradeDataPublisher = new DataPublisher(cyclicBarrier,
            tradeRB,
            ITERATIONS);

    static class MarketMarketDataEventProcessor extends MultiEventProcessor implements MarketDataHandler {

        private final String name;
        private final long expected;

        private CountDownLatch latch;
        private long totalCount = 0;
        private long barCount = 0;
        private long quoteCount = 0;
        private long tradeCount = 0;


        MarketMarketDataEventProcessor(String name, long expected, RingBuffer[] providers,
                                       SequenceBarrier[] barriers, MultiBufferWaitStrategy waitStrategy){
            super(providers, barriers, null, waitStrategy);
            this.name = name;
            this.expected = expected;
        }

        public void reset(final CountDownLatch latch)
        {
            this.latch = latch;
            this.totalCount = 0;
            this.barCount = 0;
            this.quoteCount = 0;
            this.tradeCount = 0;
        }

        @Override
        public void onEvent(Event event) {
            totalCount++;
            event.on(this);
            if (totalCount >=expected) {
                //System.out.println(name+", barCount="+barCount+", quoteCount="+quoteCount+", tradeCount="+tradeCount);
                latch.countDown();
            }
        }

        public void onBar(Bar event) {
            //assert event.dateTime == barCount;
            barCount++;
        }

        public void onQuote(Quote event) {
            //assert event.dateTime == quoteCount;
            quoteCount++;
        }

        public void onTrade(Trade event) {
            //assert event.dateTime == tradeCount;
            tradeCount++;
        }

        @Override
        public void onMarketDataContainer(MarketDataContainer data) {

        }
    }

    MarketMarketDataEventProcessor barQuoteProcessor = new MarketMarketDataEventProcessor("barQuoteProcessor", ITERATIONS *2, new RingBuffer[]{barRB, quoteRB}
            , new SequenceBarrier[]{barBarrier, quoteBarrier}, new YieldMultiBufferWaitStrategy());
    MarketMarketDataEventProcessor barTradeProcessor = new MarketMarketDataEventProcessor("barTradeProcessor", ITERATIONS *2, new RingBuffer[]{barRB, tradeRB}
            , new SequenceBarrier[]{barBarrier, tradeBarrier}, new YieldMultiBufferWaitStrategy());
    MarketMarketDataEventProcessor quoteTradeProcessor = new MarketMarketDataEventProcessor("quoteTradeProcessor", ITERATIONS *2, new RingBuffer[]{quoteRB, tradeRB}
            , new SequenceBarrier[]{quoteBarrier, tradeBarrier}, new YieldMultiBufferWaitStrategy());

    {
        barRB.addGatingSequences(barQuoteProcessor.getSequences()[0], barTradeProcessor.getSequences()[0]);
        quoteRB.addGatingSequences(barQuoteProcessor.getSequences()[1], quoteTradeProcessor.getSequences()[0]);
        tradeRB.addGatingSequences(barTradeProcessor.getSequences()[1], quoteTradeProcessor.getSequences()[1]);
    }

    public long runDisruptorPass()throws Exception{
        final CountDownLatch latch = new CountDownLatch(NUM_PROCESSERS);
        Future<?>[] futures = new Future[NUM_PUBLISHERS];
        futures[0] = executor.submit(barDataPublisher);
        futures[1] = executor.submit(quoteDataPublisher);
        futures[2] = executor.submit(tradeDataPublisher);

        barQuoteProcessor.reset(latch);
        barTradeProcessor.reset(latch);
        quoteTradeProcessor.reset(latch);
        executor.submit(barQuoteProcessor);
        executor.submit(barTradeProcessor);
        executor.submit(quoteTradeProcessor);


        long start = System.currentTimeMillis();
        cyclicBarrier.await();

        for (int i = 0; i < NUM_PUBLISHERS; i++)
        {
            futures[i].get();
        }

        latch.await();

        long opsPerSecond = (ITERATIONS * 3 * 1000L) / (System.currentTimeMillis() - start);
        barQuoteProcessor.halt();
        barTradeProcessor.halt();
        quoteTradeProcessor.halt();

        return opsPerSecond;
    }

    protected void testImplementations()
            throws Exception
    {
        long[] disruptorOps = new long[RUNS];

        System.out.println("Starting Disruptor tests");
        for (int i = 0; i < RUNS; i++)
        {
            System.gc();
            disruptorOps[i] = runDisruptorPass();
            System.out.format("Run %d, Disruptor=%,d ops/sec%n", i, Long.valueOf(disruptorOps[i]));
        }
    }

    public static void main(String[] args) throws Exception
    {
        new MultiBufferBatchEventProcessorTest().testImplementations();
    }
}
