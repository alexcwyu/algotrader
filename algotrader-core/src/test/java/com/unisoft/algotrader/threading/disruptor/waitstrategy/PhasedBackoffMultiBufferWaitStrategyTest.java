package com.unisoft.algotrader.threading.disruptor.waitstrategy;

import org.junit.Test;

import static com.unisoft.algotrader.threading.disruptor.waitstrategy.WaitStrategyTestUtil.assertWaitForWithDelayOf;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by alex on 6/10/15.
 */
public class PhasedBackoffMultiBufferWaitStrategyTest {
    @Test
    public void shouldHandleImmediateSequenceChange() throws Exception
    {
        assertWaitForWithDelayOf(0, PhasedBackoffMultiBufferWaitStrategy.withLock(1, 1, MILLISECONDS));
        assertWaitForWithDelayOf(0, PhasedBackoffMultiBufferWaitStrategy.withSleep(1, 1, MILLISECONDS));
    }

    @Test
    public void shouldHandleSequenceChangeWithOneMillisecondDelay() throws Exception
    {
        assertWaitForWithDelayOf(1, PhasedBackoffMultiBufferWaitStrategy.withLock(1, 1, MILLISECONDS));
        assertWaitForWithDelayOf(1, PhasedBackoffMultiBufferWaitStrategy.withSleep(1, 1, MILLISECONDS));
    }

    @Test
    public void shouldHandleSequenceChangeWithTwoMillisecondDelay() throws Exception
    {
        assertWaitForWithDelayOf(2, PhasedBackoffMultiBufferWaitStrategy.withLock(1, 1, MILLISECONDS));
        assertWaitForWithDelayOf(2, PhasedBackoffMultiBufferWaitStrategy.withSleep(1, 1, MILLISECONDS));
    }

    @Test
    public void shouldHandleSequenceChangeWithTenMillisecondDelay() throws Exception
    {
        assertWaitForWithDelayOf(10, PhasedBackoffMultiBufferWaitStrategy.withLock(1, 1, MILLISECONDS));
        assertWaitForWithDelayOf(10, PhasedBackoffMultiBufferWaitStrategy.withSleep(1, 1, MILLISECONDS));
    }
}
