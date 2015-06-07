package com.unisoft.algotrader.threading;

import com.lmax.disruptor.*;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.unisoft.algotrader.event.Event;
import com.unisoft.algotrader.event.data.*;

import java.util.concurrent.*;

/**
 * Created by alex on 4/12/15.
 */
public class MultiBufferBatchEventProcessorTest {

    public static final int RUNS = 7;
    private static final int NUM_PUBLISHERS = 3;
    private static final int BUFFER_SIZE = 1024 * 64;
    private static final long ITERATIONS = 180L * 1000 * 1000;
    private final ExecutorService executor = Executors.newFixedThreadPool(NUM_PUBLISHERS + 1, DaemonThreadFactory.INSTANCE);
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

    static class MarketMarketDataEventProcessor extends AbstractEventProcessor implements MarketDataHandler {

        int barCount= 0;
        int quoteCount= 0;
        int tradeCount= 0;
        long count = 0;
        long total = ITERATIONS + ITERATIONS + ITERATIONS;
        CountDownLatch latch;

        MarketMarketDataEventProcessor(RingBuffer[] providers,
                                       SequenceBarrier[] barriers, MultiBufferWaitStrategy waitStrategy){
            super(providers, barriers, null, waitStrategy);
        }

        public void reset(final CountDownLatch latch)
        {
            this.latch = latch;
            this.barCount= 0;
            this.quoteCount= 0;
            this.tradeCount= 0;
            this.count= 0;
        }

        @Override
        public void onEvent(Event event) {

//            event.on(this);
//            if (barCount >= ITERATIONS && quoteCount >= ITERATIONS && tradeCount >= ITERATIONS)
//                latch.countDown();
           Class clazz = event.getClass();
            count ++;
            if (clazz == Bar.class){
                onBar((Bar) event);
            }
            else if (clazz == Quote.class){
                onQuote((Quote) event);
            }
            else if (clazz == Trade.class){
                onTrade((Trade) event);
            }
            if (count >=ITERATIONS)
                latch.countDown();
        }

        public void onBar(Bar event) {
            barCount++;
           // System.out.println("bar="+ event.dateTime+","+ barCount);
        }

        public void onQuote(Quote event) {
            quoteCount++;
           // System.out.println("quote="+ event.dateTime+","+  quoteCount);
        }

        public void onTrade(Trade event) {
            tradeCount++;
           // System.out.println("trade="+event.dateTime+","+  tradeCount);
        }

        @Override
        public void onMarketDataContainer(MarketDataContainer data) {

        }
    }

    final CountDownLatch latch = new CountDownLatch(1);
    MarketMarketDataEventProcessor batchEventProcessor = new MarketMarketDataEventProcessor(new RingBuffer[]{barRB, quoteRB, tradeRB}
            , new SequenceBarrier[]{barBarrier, quoteBarrier, tradeBarrier}, new YieldMultiBufferWaitStrategy());


    public long runDisruptorPass()throws Exception{
        final CountDownLatch latch = new CountDownLatch(1);
        batchEventProcessor.reset(latch);
        executor.submit(batchEventProcessor);

        Future<?>[] futures = new Future[NUM_PUBLISHERS];
        futures[0] = executor.submit(barDataPublisher);
        futures[1] = executor.submit(quoteDataPublisher);
        futures[2] = executor.submit(tradeDataPublisher);


        long start = System.currentTimeMillis();
        cyclicBarrier.await();

        for (int i = 0; i < NUM_PUBLISHERS; i++)
        {
            futures[i].get();
        }

        latch.await();

        long opsPerSecond = (ITERATIONS * 1000L * 3) / (System.currentTimeMillis() - start);
        batchEventProcessor.halt();

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
