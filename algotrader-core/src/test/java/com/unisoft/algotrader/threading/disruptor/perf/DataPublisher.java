package com.unisoft.algotrader.threading.disruptor.perf;

import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.model.event.Event;

import java.util.concurrent.CyclicBarrier;
import java.util.function.BiConsumer;

/**
 * Created by alex on 6/13/15.
 */
class DataPublisher<T extends Event> implements Runnable {
    private final CyclicBarrier cyclicBarrier;
    private final RingBuffer<T> ringBuffer;
    private final String name;
    private final BiConsumer<T, Long> processFunc;

    private final long iterations;

    public DataPublisher(final CyclicBarrier cyclicBarrier, final RingBuffer<T> ringBuffer, final long iterations,
                         final String name,
                         final BiConsumer<T, Long> processFunc) {
        this.cyclicBarrier = cyclicBarrier;
        this.ringBuffer = ringBuffer;
        this.iterations = iterations;
        this.name = name;
        this.processFunc = processFunc;
    }

    @Override
    public void run() {
        try {
            cyclicBarrier.await();

            for (long i = 0; i < iterations; i++) {
                long sequence = ringBuffer.next();
                T event = ringBuffer.get(sequence);
                //event.reset();
                //processFunc.accept(event, i);
                ringBuffer.publish(sequence);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
