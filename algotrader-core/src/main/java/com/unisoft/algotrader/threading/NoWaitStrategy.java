package com.unisoft.algotrader.threading;

import com.lmax.disruptor.AlertException;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.TimeoutException;

import java.util.Queue;

/**
 * Created by alex on 4/12/15.
 */
public class NoWaitStrategy implements MultiBufferWaitStrategy {
    @Override
    public void waitNext(long[] sequences, SequenceBarrier[] barriers, Queue queue) throws AlertException, InterruptedException, TimeoutException {

    }

    @Override
    public void signalAllWhenBlocking() {

    }
}
