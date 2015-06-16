package com.unisoft.algotrader.threading.disruptor.dsl;

import com.lmax.disruptor.LifecycleAware;
import com.unisoft.algotrader.event.Event;
import com.unisoft.algotrader.threading.MultiEventProcessor;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by alex on 6/11/15.
 */
public class DelayEventProcessor extends MultiEventProcessor implements LifecycleAware {
    private final AtomicBoolean readyToProcessEvent = new AtomicBoolean(false);
    private volatile boolean stopped = false;
    private final CyclicBarrier barrier;

    public DelayEventProcessor(CyclicBarrier barrier)
    {
        this.barrier = barrier;
    }

    public DelayEventProcessor()
    {
        this(new CyclicBarrier(2));
    }


    @Override
    public void onEvent(Event event) {
        try {
            Thread.sleep(1000);
        }
        catch (Exception e){}
    }


    public void processEvent()
    {
        waitForAndSetFlag(true);
    }

    public void stopWaiting()
    {
        stopped = true;
    }

    private void waitForAndSetFlag(final boolean newValue)
    {
        while (!stopped && !Thread.currentThread().isInterrupted() &&
                !readyToProcessEvent.compareAndSet(!newValue, newValue))
        {
            Thread.yield();
        }
    }

    @Override
    public void onStart()
    {
        try
        {
            barrier.await();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
        catch (BrokenBarrierException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onShutdown()
    {
    }

    public void awaitStart() throws InterruptedException, BrokenBarrierException
    {
        barrier.await();
    }
}
