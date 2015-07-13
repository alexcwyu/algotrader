package com.unisoft.algotrader.utils.threading.disruptor.waitstrategy;

import org.junit.Test;

import static com.unisoft.algotrader.utils.threading.disruptor.waitstrategy.WaitStrategyTestUtil.assertWaitForWithDelayOf;

/**
 * Created by alex on 6/10/15.
 */
public class BusySpinMultiBufferWaitStrategyTest {
    @Test
    public void shouldWaitForValue() throws Exception
    {
        assertWaitForWithDelayOf(100, new BusySpinMultiBufferWaitStrategy());
    }
}
