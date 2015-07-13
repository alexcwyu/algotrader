package com.unisoft.algotrader.utils.threading.disruptor.perf;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.unisoft.algotrader.utils.threading.disruptor.waitstrategy.NoWaitStrategy;

import java.util.concurrent.*;

/**
 * Created by alex on 6/12/15.
 */
public class DisruptorIntegrationTest{

    public static final int RUNS = 10;

    private static final int NUM_PUBLISHERS = 1;
    private static final int NUM_PROCESSERS= 3;
    private static final int BUFFER_SIZE = 1024 * 64;
    private static final long ITERATIONS = 1000L * 1000L * 180L;

    private final ExecutorService executor = Executors.newFixedThreadPool(NUM_PUBLISHERS + NUM_PROCESSERS, DaemonThreadFactory.INSTANCE);
    private final CyclicBarrier cyclicBarrier = new CyclicBarrier(NUM_PUBLISHERS + 1);

    private final RingBuffer<TestData> ringBuffer = RingBuffer.createSingleProducer(TestData.FACTORY, BUFFER_SIZE, new NoWaitStrategy());

    private final DataPublisher<TestData> testDataPublisher = new DataPublisher(cyclicBarrier, ringBuffer, ITERATIONS, "publisher", new TestDataConsumer("publisher"));

    private final SequenceBarrier barrier = ringBuffer.newBarrier();
    private final TestDataEventProcessor processor1 = new TestDataEventProcessor("processor1", ITERATIONS,
            ((testData, count) -> testData.map.size() == 1 && testData.map.get("publisher") == count),
            new TestDataConsumer("processor1"));

    private final TestDataEventProcessor processor2 = new TestDataEventProcessor("processor2", ITERATIONS,
            ((testData, count) -> testData.map.size() == 1 && testData.map.get("publisher") == count),
            new TestDataConsumer("processor2"));

    private final TestDataEventProcessor processor3 = new TestDataEventProcessor("processor3", ITERATIONS,
            ((testData, count) -> testData.map.size() == 2 && testData.map.get("publisher") == count && testData.map.get("processor1") == count&& testData.map.get("processor2") == count),
            new TestDataConsumer("processor3"));

    {

        Sequence sequence1 = processor1.add(ringBuffer, barrier);

        Sequence sequence2 = processor2.add(ringBuffer, barrier);

        SequenceBarrier barrier3 = ringBuffer.newBarrier(sequence1, sequence2);
        Sequence sequence3 = processor3.add(ringBuffer, barrier3);

        ringBuffer.addGatingSequences(sequence3);
    }


    public long runDisruptorPass()throws Exception{
        final CountDownLatch latch = new CountDownLatch(NUM_PROCESSERS);
        Future<?>[] futures = new Future[NUM_PUBLISHERS];
        futures[0] = executor.submit(testDataPublisher);

        processor1.reset(latch);
        processor2.reset(latch);
        processor3.reset(latch);
        executor.submit(processor1);
        executor.submit(processor2);
        executor.submit(processor3);

        long start = System.currentTimeMillis();
        cyclicBarrier.await();

        for (int i = 0; i < NUM_PUBLISHERS; i++)
        {
            futures[i].get();
        }

        latch.await();

        long opsPerSecond = (ITERATIONS * 1000L) / (System.currentTimeMillis() - start);
        processor1.halt();
        processor2.halt();
        processor3.halt();

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
        new DisruptorIntegrationTest().testImplementations();
    }

}
