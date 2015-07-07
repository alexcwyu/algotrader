package com.unisoft.algotrader.threading;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.threading.disruptor.waitstrategy.DataPublisher;

import java.util.concurrent.*;

/**
 * Created by alex on 4/12/15.
 */
public class BatchEventProcessorTest {

    public static final int RUNS = 7;
    private static final int NUM_PUBLISHERS = 1;
    private static final int BUFFER_SIZE = 1024 * 8;
    private static final long ITERATIONS = 1000L * 1000L * 100L;
    private final ExecutorService executor = Executors.newFixedThreadPool(NUM_PUBLISHERS + 2, DaemonThreadFactory.INSTANCE);
    private final CyclicBarrier cyclicBarrier = new CyclicBarrier(NUM_PUBLISHERS + 1);

    private final RingBuffer<Bar> barRB = RingBuffer.createSingleProducer(Bar::new, BUFFER_SIZE, new YieldingWaitStrategy());
    private final SequenceBarrier barBarrier = barRB.newBarrier();

    private final DataPublisher<Bar> barDataPublisher = new DataPublisher(cyclicBarrier,
            barRB,
            ITERATIONS);

    static class BarEventHandler implements com.lmax.disruptor.EventHandler<Bar>{

        private final String name;

        private long count = 0;
        private CountDownLatch latch;

        public BarEventHandler(String name){
            this.name = name;
        }

        @Override
        public void onEvent(Bar event, long sequence, boolean endOfBatch) throws Exception {

            count++;
            if (count >=ITERATIONS)
                latch.countDown();
        }

        public void reset(CountDownLatch latch){
            count = 0;
            this.latch = latch;
        }
    }

    private final BarEventHandler eventHandler1 = new BarEventHandler("Test 1");
    private final BarEventHandler eventHandler2 = new BarEventHandler("Test 2");
    private final BatchEventProcessor<Bar> barProcessor1 =
            new BatchEventProcessor<>(barRB, barBarrier, eventHandler1);
    private final BatchEventProcessor<Bar> barProcessor2 =
            new BatchEventProcessor<>(barRB, barBarrier, eventHandler2);

    {
        barRB.addGatingSequences(barProcessor1.getSequence(), barProcessor2.getSequence());
    }


    public long runDisruptorPass()throws Exception{
        CountDownLatch latch = new CountDownLatch(2);

        Future<?>[] futures = new Future[NUM_PUBLISHERS];
        futures[0] = executor.submit(barDataPublisher);

        eventHandler1.reset(latch);
        executor.submit(barProcessor1);

        eventHandler2.reset(latch);
        executor.submit(barProcessor2);

        long start = System.currentTimeMillis();
        cyclicBarrier.await();

        for (int i = 0; i < NUM_PUBLISHERS; i++)
        {
            futures[i].get();
        }

        latch.await();

        long opsPerSecond = (ITERATIONS * 1000L) / (System.currentTimeMillis() - start);
        barProcessor1.halt();
        barProcessor2.halt();

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
        new BatchEventProcessorTest().testImplementations();
    }
}
