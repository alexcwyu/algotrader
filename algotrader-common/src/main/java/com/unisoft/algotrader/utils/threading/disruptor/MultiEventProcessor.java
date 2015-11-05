package com.unisoft.algotrader.utils.threading.disruptor;

import com.google.common.collect.Lists;
import com.lmax.disruptor.*;
import com.unisoft.algotrader.utils.threading.disruptor.waitstrategy.MultiBufferWaitStrategy;
import com.unisoft.algotrader.utils.threading.disruptor.waitstrategy.NoWaitStrategy;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Arrays.fill;

//import com.unisoft.algotrader.model.event.Event;
//import com.unisoft.algotrader.model.event.EventHandler;

/**
 * Created by alex on 4/12/15.
 */
public class MultiEventProcessor implements EventProcessor, LifecycleAware{
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final AtomicBoolean sealed = new AtomicBoolean(false);
    private final MultiBufferWaitStrategy waitStrategy;
    private final List<RingBufferInfo<?>> ringBufferInfos = Lists.newArrayList();

    private Queue queue;
    private long count;
    private RingBuffer [] providers;
    private SequenceBarrier [] barriers;
    private Sequence [] sequences;
    private EventHandler [] eventHandlers;

    public MultiEventProcessor() {
        this(new NoWaitStrategy());
    }

    public MultiEventProcessor(MultiBufferWaitStrategy waitStrategy) {
        this.waitStrategy = waitStrategy;
    }

    public Sequence add(RingBuffer provider, SequenceBarrier barriers, EventHandler eventHandler){
        Sequence sequence = new Sequence(Sequencer.INITIAL_CURSOR_VALUE);
        this.ringBufferInfos.add(new RingBufferInfo<>(provider, barriers, sequence, eventHandler));
        return sequence;
    }

    public Sequence add(RingBufferInfo ringBufferInfo){
        this.ringBufferInfos.add(ringBufferInfo);
        return ringBufferInfo.sequence;
    }

    public void seal(){
        if (sealed.compareAndSet(false, true)) {
            int size = ringBufferInfos.size();

            this.providers = new RingBuffer[size];
            this.barriers = new SequenceBarrier[size] ;
            this.sequences = new Sequence[size];
            this.eventHandlers = new EventHandler[size];

            for (int i =0; i < size; i ++) {
                RingBufferInfo ringBufferInfo = ringBufferInfos.get(i);
                this.providers[i]= ringBufferInfo.provider;
                this.barriers[i]= ringBufferInfo.barriers;
                this.sequences[i]= ringBufferInfo.sequence;
                this.eventHandlers[i]= ringBufferInfo.eventHandler;

                providers[i].addGatingSequences(sequences[i]);
            }
        }
    }

    @Override
    public void run() {
        if (!isRunning.compareAndSet(false, true)) {
            throw new RuntimeException("Already running");
        }

        seal();

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
//                    if (queue != null) {
//                        while (queue.peek() != null) {
//                            batchCount++;
//                            eventHandler.onEvent(queue.poll());
//                        }
//                    }

                    //RB event
                    for (int i = 0; i < barrierLength; i++) {

                        Sequence sequence = sequences[i];
                        long previous = sequence.get();
                        expNextSeq[i] = previous + 1L;
                        long available = barriers[i].waitFor(expNextSeq[i]);

                        if (available > previous) {
                            for (long l = previous + 1; l <= available; l++) {
                                eventHandlers[i].onEvent(providers[i].get(l), l, l==available);
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

    public Sequence getSequence(int i){
        return sequences[i];
    }

    public Sequence[] getSequences()
    {
        return sequences;
    }

    public long getCount()
    {
        return count;
    }

    @Override
    public void halt(){
        isRunning.set(false);
        //barriers[0].alert();
        for (SequenceBarrier barrier : barriers) {
            barrier.alert();
        }
    }

    @Override
    public boolean isRunning(){
        return isRunning.get();
    }

    @Override
    public void onStart(){
    }

    @Override
    public void onShutdown(){
    }
}

