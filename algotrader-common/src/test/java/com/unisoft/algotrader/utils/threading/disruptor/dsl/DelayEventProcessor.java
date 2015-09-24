package com.unisoft.algotrader.utils.threading.disruptor.dsl;

import com.lmax.disruptor.LifecycleAware;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.event.EventHandler;
import com.unisoft.algotrader.utils.threading.disruptor.MultiEventProcessor;

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

    public DelayEventProcessor(EventHandler eventHandler, CyclicBarrier barrier)
    {
        super(eventHandler);
        this.barrier = barrier;
    }

    public DelayEventProcessor(EventHandler eventHandler)
    {
        this(eventHandler, new CyclicBarrier(2));
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
