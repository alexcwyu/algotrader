package com.unisoft.algotrader.threading.disruptor.waitstrategy;

import com.lmax.disruptor.AlertException;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.TimeoutException;

import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by alex on 4/12/15.
 */
public class TimeoutBlockingMultiBufferWaitStrategy implements MultiBufferWaitStrategy {
    private final Lock lock = new ReentrantLock();
    private final Condition processorNotifyCondition = lock.newCondition();
    private final long timeoutInNanos;

    public TimeoutBlockingMultiBufferWaitStrategy(final long timeout, final TimeUnit units)
    {
        timeoutInNanos = units.toNanos(timeout);
    }

    @Override
    public void waitNext(long[] sequences, SequenceBarrier[] barriers, Queue queue)throws AlertException, InterruptedException, TimeoutException{
        long nanos = timeoutInNanos;
        if (!hasNext(sequences,  barriers, queue)){
            lock.lock();
            try
            {

                while (!hasNext(sequences,  barriers, queue))
                {

                    final int barrierLength = barriers.length;
                    for (int i = 0; i < barrierLength; i++)
                        barriers[i].checkAlert();

                    nanos = processorNotifyCondition.awaitNanos(nanos);

                    if (nanos <= 0)
                    {
                        throw TimeoutException.INSTANCE;
                    }
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
