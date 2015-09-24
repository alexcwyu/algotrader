package com.unisoft.algotrader.utils.threading.disruptor.perf;

import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.unisoft.algotrader.utils.threading.disruptor.dsl.MultiEventDisruptor;

import java.util.concurrent.*;

/**
 * Created by alex on 6/12/15.
 */
public class DisruptorIntegrationDSLTest {

    public static final int RUNS = 10;

    private static final int NUM_PUBLISHERS = 1;
    private static final int NUM_PROCESSERS= 3;
    private static final int BUFFER_SIZE = 1024 * 64;
    private static final long ITERATIONS = 1000L * 1000L * 180L;

    private final ExecutorService executor = Executors.newFixedThreadPool(NUM_PUBLISHERS + NUM_PROCESSERS, DaemonThreadFactory.INSTANCE);
    private final CyclicBarrier cyclicBarrier = new CyclicBarrier(NUM_PUBLISHERS + 1);

    public long runDisruptorPass()throws Exception{
        final CountDownLatch latch = new CountDownLatch(1);

        TestDataEventProcessor processor1 = new TestDataEventProcessor("processor1", ITERATIONS,
                ((testData, count) -> testData.map.size() == 1 && testData.map.get("publisher") == count),
                new TestDataConsumer("processor1"));

        TestDataEventProcessor processor2 = new TestDataEventProcessor("processor2", ITERATIONS,
                ((testData, count) -> testData.map.size() == 1 && testData.map.get("publisher") == count),
                new TestDataConsumer("processor2"));

        TestDataEventProcessor processor3 = new TestDataEventProcessor("processor3", ITERATIONS,
                ((testData, count) -> testData.map.size() == 2 && testData.map.get("publisher") == count && testData.map.get("processor1") == count&& testData.map.get("processor2") == count),
                new TestDataConsumer("processor3"));

        MultiEventDisruptor<TestData> disruptor = new MultiEventDisruptor(ProducerType.SINGLE, TestData.FACTORY, BUFFER_SIZE, executor);
//        disruptor.handleEventsWith(processor1, processor2).then(processor3);

        Future<?>[] futures = new Future[NUM_PUBLISHERS];
        DataPublisher<TestData> testDataPublisher = new DataPublisher(cyclicBarrier, disruptor.getRingBuffer(), ITERATIONS, "publisher", new TestDataConsumer("publisher"));
        futures[0] = executor.submit(testDataPublisher);


        processor1.reset(latch);
        processor2.reset(latch);
        processor3.reset(latch);

        disruptor.start();

        long start = System.currentTimeMillis();
        cyclicBarrier.await();

        for (int i = 0; i < NUM_PUBLISHERS; i++)
        {
            futures[i].get();
        }

        latch.await();

        long opsPerSecond = (ITERATIONS * 1000L) / (System.currentTimeMillis() - start);

        disruptor.shutdown();
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
        new DisruptorIntegrationDSLTest().testImplementations();
    }

}
