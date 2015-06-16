package com.unisoft.algotrader.threading.disruptor.waitstrategy;

import com.lmax.disruptor.AlertException;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.WaitStrategy;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WaitStrategyTestUtil
{
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    static void assertWaitForWithDelayOf(long sleepTimeMillis, WaitStrategy waitStrategy)
            throws InterruptedException, BrokenBarrierException, AlertException, TimeoutException
    {
        SequenceUpdater sequenceUpdater = new SequenceUpdater(sleepTimeMillis, waitStrategy);
        EXECUTOR.execute(sequenceUpdater);
        sequenceUpdater.waitForStartup();
        Sequence cursor = new Sequence(0);
        long sequence = waitStrategy.waitFor(0, cursor, sequenceUpdater.sequence, new DummySequenceBarrier(sequenceUpdater));

        assertThat(sequence, is(0L));
    }
}
