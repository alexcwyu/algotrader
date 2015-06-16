package com.unisoft.algotrader.threading.disruptor.perf;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.unisoft.algotrader.event.Event;
import com.unisoft.algotrader.event.data.*;
import com.unisoft.algotrader.threading.MultiEventProcessor;
import com.unisoft.algotrader.threading.disruptor.waitstrategy.DataPublisher;
import com.unisoft.algotrader.threading.disruptor.waitstrategy.MultiBufferWaitStrategy;
import com.unisoft.algotrader.threading.disruptor.waitstrategy.NoWaitStrategy;

import java.util.concurrent.*;

/**
 * Created by alex on 4/12/15.
 */
public class MultiBufferBatchEventProcessorPrefTest {

    public static final int RUNS = 100;
    private static final int NUM_PUBLISHERS = 3;
    private static final int NUM_PROCESSERS= 1;
    private static final int BUFFER_SIZE = 1024 * 64;
    private static final long ITERATIONS = 1000L * 1000L * 180L;
    private final ExecutorService executor = Executors.newFixedThreadPool(NUM_PUBLISHERS + NUM_PROCESSERS, DaemonThreadFactory.INSTANCE);
    private final CyclicBarrier cyclicBarrier = new CyclicBarrier(NUM_PUBLISHERS + 1);

    private final RingBuffer<Bar> barRB = RingBuffer.createMultiProducer(Bar.FACTORY, BUFFER_SIZE, new NoWaitStrategy());
    private final RingBuffer<Quote> quoteRB = RingBuffer.createMultiProducer(Quote.FACTORY, BUFFER_SIZE, new NoWaitStrategy());
    private final RingBuffer<Trade> tradeRB = RingBuffer.createMultiProducer(Trade.FACTORY, BUFFER_SIZE, new NoWaitStrategy());

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
        private long count = 0;

        MarketMarketDataEventProcessor(String name, long expected, RingBuffer[] providers,
                                       SequenceBarrier[] barriers, MultiBufferWaitStrategy waitStrategy){
            super(providers, barriers, null, waitStrategy);
            this.name = name;
            this.expected = expected;
        }

        public void reset(final CountDownLatch latch)
        {
            this.latch = latch;
            this.count = expected;
        }

        @Override
        public void onEvent(Event event) {
            if (--count == 0) {
                latch.countDown();
            }
        }

        public void onBar(Bar event) {
        }

        public void onQuote(Quote event) {
        }

        public void onTrade(Trade event) {
        }
    }

    MarketMarketDataEventProcessor processor = new MarketMarketDataEventProcessor("processor", ITERATIONS *3, new RingBuffer[]{barRB, quoteRB, tradeRB}
            , new SequenceBarrier[]{barBarrier, quoteBarrier, tradeBarrier}, new NoWaitStrategy());

    {
        barRB.addGatingSequences(processor.getSequences()[0]);
        quoteRB.addGatingSequences(processor.getSequences()[1]);
        tradeRB.addGatingSequences(processor.getSequences()[2]);
    }

    public long runDisruptorPass()throws Exception{
        final CountDownLatch latch = new CountDownLatch(NUM_PROCESSERS);
        Future<?>[] futures = new Future[NUM_PUBLISHERS];
        futures[0] = executor.submit(barDataPublisher);
        futures[1] = executor.submit(quoteDataPublisher);
        futures[2] = executor.submit(tradeDataPublisher);

        processor.reset(latch);
        executor.submit(processor);


        long start = System.currentTimeMillis();
        cyclicBarrier.await();

        for (int i = 0; i < NUM_PUBLISHERS; i++)
        {
            futures[i].get();
        }

        latch.await();

        long opsPerSecond = (ITERATIONS * NUM_PUBLISHERS * 1000L) / (System.currentTimeMillis() - start);
        processor.halt();

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
        new MultiBufferBatchEventProcessorPrefTest().testImplementations();
    }
}
