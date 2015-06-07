package com.unisoft.algotrader.threading;

import com.lmax.disruptor.AlertException;
import com.lmax.disruptor.SequenceBarrier;

import java.util.Queue;

/**
 * Created by alex on 4/12/15.
 */
public class YieldMultiBufferWaitStrategy implements MultiBufferWaitStrategy {

    private static final int SPIN_TRIES = 100;

    @Override
    public void waitNext(long[] sequences, SequenceBarrier[] barriers, Queue queue)throws AlertException, InterruptedException{

        int counter = SPIN_TRIES;
        while (!hasNext(sequences,  barriers, queue)){
            counter = applyWaitMethod(barriers, counter);
        }
    }

    private int applyWaitMethod(final SequenceBarrier[] barriers, int counter)
            throws AlertException
    {
        for (SequenceBarrier barrier : barriers) {
            barrier.checkAlert();
        }

        if (0 == counter)
        {
            Thread.yield();
        }
        else
        {
            --counter;
        }

        return counter;
    }

    @Override
    public void signalAllWhenBlocking()
    {
    }
}
