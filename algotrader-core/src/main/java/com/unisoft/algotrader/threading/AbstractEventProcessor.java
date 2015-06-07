package com.unisoft.algotrader.threading;

import com.lmax.disruptor.*;
import com.unisoft.algotrader.event.Event;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Arrays.fill;

/**
 * Created by alex on 4/12/15.
 */
public abstract class AbstractEventProcessor implements EventProcessor, EventHandler<Event> {
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final RingBuffer<Event>[] providers;
    private final SequenceBarrier[] barriers;
    private final Sequence[] sequences;
    private final Queue<Event> queue;
    private final MultiBufferWaitStrategy waitStrategy;
    private long count;

    public AbstractEventProcessor(MultiBufferWaitStrategy waitStrategy, Queue queue, RingBuffer... providers) {

        this.providers = providers;
        this.barriers = new SequenceBarrier[providers.length];
        this.queue = queue;
        this.waitStrategy = waitStrategy;

        this.sequences = new Sequence[providers.length];
        for (int i = 0; i < providers.length; i++) {
            barriers[i] = providers[i].newBarrier();
            sequences[i] = new Sequence(-1);
            providers[i].addGatingSequences(sequences[i]);
        }
    }

    public AbstractEventProcessor(RingBuffer[] providers,
            SequenceBarrier[] barriers, Queue queue, MultiBufferWaitStrategy waitStrategy) {
        if (providers.length != barriers.length) {
            throw new IllegalArgumentException();
        }

        this.providers = providers;
        this.barriers = barriers;
        this.queue = queue;
        this.waitStrategy = waitStrategy;

        this.sequences = new Sequence[providers.length];
        for (int i = 0; i < sequences.length; i++) {
            sequences[i] = new Sequence(-1);
            providers[i].addGatingSequences(sequences[i]);
        }
    }

    @Override
    public void run() {
        if (!isRunning.compareAndSet(false, true)) {
            throw new RuntimeException("Already running");
        }

        for (SequenceBarrier barrier : barriers) {
            barrier.clearAlert();
        }

        final int barrierLength = barriers.length;
        final long[] lastConsumed = new long[barrierLength];
        final long[] expNextSeq = new long[barrierLength];
        fill(lastConsumed, -1L);

        while (true) {
            try {
                long batchCount = 0;

                // queue event
                if (queue != null) {
                    while (queue.peek() != null) {
                        batchCount++;
                        onEvent(queue.poll());
                    }
                }

                //RB event
                for (int i = 0; i < barrierLength; i++) {

                    Sequence sequence = sequences[i];
                    expNextSeq[i] = sequence.get() + 1L;
                    //long nextSequence = sequence.get() + 1L;
                    //long available = barriers[i].waitFor(nextSequence);

                    long available = barriers[i].waitFor(-1);

                    long previous = sequence.get();

                    for (long l = previous + 1; l <= available; l++) {
                        onEvent(providers[i].get(l));
                    }

                    sequence.set(available);

                    batchCount += (available - previous);
                }
                count += batchCount;

                if (batchCount == 0)
                    if (waitStrategy == null)
                        Thread.yield();
                    else
                        waitStrategy.waitNext(expNextSeq, barriers, queue);

            } catch (AlertException e) {
                if (!isRunning()) {
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }


    @Override
    public Sequence getSequence()
        {
            throw new UnsupportedOperationException();
        }

    public long getCount()
    {
        return count;
    }

    public Sequence[] getSequences()
    {
        return sequences;
    }

    @Override
    public void halt()
    {
        isRunning.set(false);
        barriers[0].alert();
    }

    @Override
    public boolean isRunning()
    {
        return isRunning.get();
    }
}
