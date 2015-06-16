package com.unisoft.algotrader.threading.disruptor.waitstrategy;

import com.lmax.disruptor.AlertException;
import com.lmax.disruptor.SequenceBarrier;

import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by alex on 4/12/15.
 */
public class BlockingMultiBufferWaitStrategy implements MultiBufferWaitStrategy {


    private final Lock lock = new ReentrantLock();
    private final Condition processorNotifyCondition = lock.newCondition();

    @Override
    public void waitNext(long[] sequences, SequenceBarrier[] barriers, Queue queue)throws AlertException, InterruptedException{
        if (!hasNext(sequences,  barriers, queue)){
            lock.lock();
            try
            {
                while (!hasNext(sequences,  barriers, queue))
                {
                    for (SequenceBarrier barrier : barriers) {
                        barrier.checkAlert();
                    }
                    processorNotifyCondition.await();
                }
            }
            finally
            {
                lock.unlock();
            }
        }
    }


    @Override
    public void signalAllWhenBlocking()
    {
        lock.lock();
        try
        {
            processorNotifyCondition.signalAll();
        }
        finally
        {
            lock.unlock();
        }
    }
}
