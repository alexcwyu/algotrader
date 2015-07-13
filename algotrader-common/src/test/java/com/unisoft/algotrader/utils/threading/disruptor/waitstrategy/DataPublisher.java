package com.unisoft.algotrader.utils.threading.disruptor.waitstrategy;

import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.model.event.data.MarketData;

import java.util.concurrent.CyclicBarrier;

/**
 * Created by alex on 4/12/15.
 */
public class DataPublisher<T extends MarketData> implements Runnable {

    private final CyclicBarrier cyclicBarrier;
    private final RingBuffer<T> ringBuffer;

    private final long iterations;

    public DataPublisher(final CyclicBarrier cyclicBarrier, final RingBuffer<T> ringBuffer, final long iterations)
    {
        this.cyclicBarrier = cyclicBarrier;
        this.ringBuffer = ringBuffer;
        this.iterations = iterations;
    }

    @Override
    public void run()
    {
        try
        {
            cyclicBarrier.await();

            for (long i = 0; i < iterations; i++)
            {
                long sequence = ringBuffer.next();
                T event = ringBuffer.get(sequence);
                event.reset();
                //updateEvent(event);
                event.dateTime = i;
                ringBuffer.publish(sequence);
            }
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }
}
