package com.unisoft.algotrader.utils.threading.disruptor.waitstrategy;

import com.lmax.disruptor.TimeoutException;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.unisoft.algotrader.utils.threading.disruptor.waitstrategy.WaitStrategyTestUtil.assertWaitForWithDelayOf;

/**
 * Created by alex on 6/10/15.
 */
public class TimeoutBlockingMultiBufferWaitStrategyTest {
    @Test(expected= TimeoutException.class)
    public void shouldThrowTimeoutException() throws Exception
    {
        assertWaitForWithDelayOf(10000, new TimeoutBlockingMultiBufferWaitStrategy(1, TimeUnit.SECONDS));
    }


    @Test
    public void shouldWaitForValue() throws Exception
    {
        assertWaitForWithDelayOf(100, new TimeoutBlockingMultiBufferWaitStrategy(500, TimeUnit.MILLISECONDS));
    }
}
