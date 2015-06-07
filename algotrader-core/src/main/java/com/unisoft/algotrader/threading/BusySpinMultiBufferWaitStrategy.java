package com.unisoft.algotrader.threading;

import com.lmax.disruptor.AlertException;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.SequenceBarrier;

import java.util.Queue;

/**
 * Created by alex on 4/12/15.
 */
public class BusySpinMultiBufferWaitStrategy implements MultiBufferWaitStrategy {

    @Override
    public void waitNext(long[] sequences, SequenceBarrier[] barriers, Queue queue)throws AlertException, InterruptedException{
        while (!hasNext(sequences,  barriers, queue)){
            final int barrierLength = barriers.length;
            for (int i = 0; i < barrierLength; i++)
                barriers[i].checkAlert();
        }
    }

    @Override
    public void signalAllWhenBlocking()
    {
    }
}
