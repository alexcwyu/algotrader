package com.unisoft.algotrader.threading;

import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.event.data.MarketData;

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

    //public void updateEvent(T event){
    //    event.dateTime = System.currentTimeMillis();
    //}

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
                //updateEvent(event);
                event.dateTime = i+1;
                ringBuffer.publish(sequence);
                //System.out.println("publishing..."+event.getClass().getSimpleName()+" "+i);
            }
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }
}
