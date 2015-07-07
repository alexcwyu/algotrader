package com.unisoft.algotrader.threading;

import com.lmax.disruptor.*;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.threading.disruptor.waitstrategy.MultiBufferWaitStrategy;
import com.unisoft.algotrader.threading.disruptor.waitstrategy.NoWaitStrategy;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Arrays.fill;

/**
 * Created by alex on 4/12/15.
 */
public abstract class MultiEventProcessor implements EventProcessor, EventHandler<Event>, LifecycleAware{
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private RingBuffer<Event>[] providers = null;
    private SequenceBarrier[] barriers = null;
    private Sequence[] sequences = null;
    private final Queue<Event> queue;
    private final MultiBufferWaitStrategy waitStrategy;
    private long count;


    public MultiEventProcessor() {
        this(new NoWaitStrategy());
    }

    public MultiEventProcessor(MultiBufferWaitStrategy waitStrategy) {
        this.queue = null;
        this.waitStrategy = waitStrategy;
    }


    public MultiEventProcessor(MultiBufferWaitStrategy waitStrategy, Queue queue, RingBuffer... providers) {

        this.providers = providers;
        this.barriers = new SequenceBarrier[providers.length];
        this.queue = queue;
        this.waitStrategy = waitStrategy;

        this.sequences = new Sequence[providers.length];
        for (int i = 0; i < providers.length; i++) {
            barriers[i] = providers[i].newBarrier();
            sequences[i] = new Sequence(Sequencer.INITIAL_CURSOR_VALUE);
            //providers[i].addGatingSequences(sequences[i]);
        }
    }

    public MultiEventProcessor(RingBuffer[] providers,
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

        onStart();

        final int barrierLength = barriers.length;
        final long[] lastConsumed = new long[barrierLength];
        final long[] expNextSeq = new long[barrierLength];
        fill(lastConsumed, -1L);
        try {
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
                        long previous = sequence.get();
                        expNextSeq[i] = previous + 1L;
                        long available = barriers[i].waitFor(expNextSeq[i]);

                        if (available > previous) {
                            for (long l = previous + 1; l <= available; l++) {
                                onEvent(providers[i].get(l));
                            }
                            sequence.set(available);
                            batchCount += (available - previous);
                        }
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
        }finally {
            onShutdown();
        }
    }


    @Override
    public Sequence getSequence() {
        throw new UnsupportedOperationException();
    }

    public Sequence getSequence(RingBuffer buffer){
        for (int i = 0; i < providers.length; i++) {
            if (providers[i] == buffer){
                return sequences[i];
            }
        }
        throw new RuntimeException("buffer not found="+buffer);

    }

    public long getCount()
    {
        return count;
    }

    public Sequence[] getSequences()
    {
        return sequences;
    }

    public Sequence add(RingBuffer provider, SequenceBarrier barrier){

        Sequence sequence = new Sequence(Sequencer.INITIAL_CURSOR_VALUE);
        if (this.providers != null) {
            RingBuffer<Event>[] newProviders = new RingBuffer[this.providers.length + 1];
            SequenceBarrier[] newBarriers = new SequenceBarrier[this.barriers.length + 1];
            Sequence[] newSequences = new Sequence[this.sequences.length + 1];

            System.arraycopy(this.providers, 0, newProviders, 0, this.sequences.length);
            System.arraycopy(this.barriers, 0, newBarriers, 0, this.sequences.length);
            System.arraycopy(this.sequences, 0, newSequences, 0, this.sequences.length);

            newProviders[newProviders.length - 1] = provider;
            newBarriers[newBarriers.length - 1] = barrier;
            newSequences[newSequences.length - 1] = sequence;

            this.providers = newProviders;
            this.barriers = newBarriers;
            this.sequences = newSequences;
        }
        else{

            this.providers = new RingBuffer[]{provider};
            this.barriers = new SequenceBarrier[]{barrier};
            this.sequences = new Sequence[]{sequence};
        }

        return sequence;
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

    @Override
    public void onStart()
    {
    }

    @Override
    public void onShutdown()
    {
    }
}

