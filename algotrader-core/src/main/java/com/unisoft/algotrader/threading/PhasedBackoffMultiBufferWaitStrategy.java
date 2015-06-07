package com.unisoft.algotrader.threading;


import com.lmax.disruptor.AlertException;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.TimeoutException;

import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * Created by alex on 4/12/15.
 */
public final class PhasedBackoffMultiBufferWaitStrategy implements MultiBufferWaitStrategy {
    private static final int SPIN_TRIES = 10000;
    private final long spinTimeoutNanos;
    private final long yieldTimeoutNanos;
    private final MultiBufferWaitStrategy fallbackStrategy;

    public PhasedBackoffMultiBufferWaitStrategy(long spinTimeout,
                                                long yieldTimeout,
                                                TimeUnit units,
                                                MultiBufferWaitStrategy fallbackStrategy) {
        this.spinTimeoutNanos = units.toNanos(spinTimeout);
        this.yieldTimeoutNanos = spinTimeoutNanos + units.toNanos(yieldTimeout);
        this.fallbackStrategy = fallbackStrategy;
    }

    /**
     * Block with wait/notifyAll semantics
     */
    public static PhasedBackoffMultiBufferWaitStrategy withLock(long spinTimeout,
                                                     long yieldTimeout,
                                                     TimeUnit units) {
        return new PhasedBackoffMultiBufferWaitStrategy(spinTimeout, yieldTimeout,
                units, new BlockingMultiBufferWaitStrategy());
    }

    /**
     * Block with wait/notifyAll semantics
     */
    public static PhasedBackoffMultiBufferWaitStrategy withLiteLock(long spinTimeout,
                                                         long yieldTimeout,
                                                         TimeUnit units) {
        return new PhasedBackoffMultiBufferWaitStrategy(spinTimeout, yieldTimeout,
                units, new LiteBlockingMultiBufferWaitStrategy());
    }

    /**
     * Block by sleeping in a loop
     */
    public static PhasedBackoffMultiBufferWaitStrategy withSleep(long spinTimeout,
                                                      long yieldTimeout,
                                                      TimeUnit units) {
        return new PhasedBackoffMultiBufferWaitStrategy(spinTimeout, yieldTimeout,
                units, new SleepingMultiBufferWaitStrategy(0));
    }

    @Override
    public void waitNext(long[] sequences, SequenceBarrier[] barriers, Queue queue) throws AlertException, InterruptedException, TimeoutException {
        long availableSequence;
        long startTime = 0;
        int counter = SPIN_TRIES;

        do {
            if (hasNext(sequences, barriers, queue)) {
                return;
            }

            if (0 == --counter) {
                if (0 == startTime) {
                    startTime = System.nanoTime();
                } else {
                    long timeDelta = System.nanoTime() - startTime;
                    if (timeDelta > yieldTimeoutNanos) {
                        fallbackStrategy.waitNext(sequences, barriers, queue);
                        return;
                    } else if (timeDelta > spinTimeoutNanos) {
                        Thread.yield();
                    }
                }
                counter = SPIN_TRIES;
            }
        }
        while (true);
    }

    @Override
    public void signalAllWhenBlocking(){
        fallbackStrategy.signalAllWhenBlocking();
    }
}

