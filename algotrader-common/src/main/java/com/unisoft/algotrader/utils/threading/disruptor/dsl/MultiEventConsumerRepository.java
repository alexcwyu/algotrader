/*
 * Copyright 2011 LMAX Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.unisoft.algotrader.utils.threading.disruptor.dsl;

import com.lmax.disruptor.*;
import com.unisoft.algotrader.utils.threading.disruptor.MultiEventProcessor;

import java.util.*;

/**
 * Provides a repository mechanism to associate {@link EventHandler}s with {@link EventProcessor}s
 *
 * @param <T> the type of the {@link EventHandler}
 */
class MultiEventConsumerRepository<T> implements Iterable<ConsumerInfo>
{



    private final Map<MultiEventProcessor, MultiEventProcessorInfo<T>> eventProcessorInfoByEventHandler = new IdentityHashMap<MultiEventProcessor, MultiEventProcessorInfo<T>>();
    private final Map<Sequence, ConsumerInfo> eventProcessorInfoBySequence = new IdentityHashMap<Sequence, ConsumerInfo>();
    private final Collection<ConsumerInfo> consumerInfos = new ArrayList<ConsumerInfo>();
    private final MultiEventDisruptor<T> disruptor;

    public MultiEventConsumerRepository(MultiEventDisruptor<T> disruptor){
        this.disruptor = disruptor;
    }


    public void add(final MultiEventProcessor eventprocessor,
                    final SequenceBarrier barrier,
                    final RingBuffer<T> ringBuffer)
    {
        final MultiEventProcessorInfo<T> consumerInfo = new MultiEventProcessorInfo<T>(eventprocessor, barrier, ringBuffer);
        eventProcessorInfoByEventHandler.put(eventprocessor, consumerInfo);
        eventProcessorInfoBySequence.put(eventprocessor.getSequence(ringBuffer), consumerInfo);
        consumerInfos.add(consumerInfo);
    }

    public void add(final MultiEventProcessor eventprocessor,
                    final RingBuffer<T> ringBuffer)
    {
        final MultiEventProcessorInfo<T> consumerInfo = new MultiEventProcessorInfo<T>(eventprocessor,  null, ringBuffer);
        eventProcessorInfoByEventHandler.put(eventprocessor, consumerInfo);
        eventProcessorInfoBySequence.put(eventprocessor.getSequence(ringBuffer), consumerInfo);
        consumerInfos.add(consumerInfo);
    }

    public void add(final WorkerPool<T> workerPool, final SequenceBarrier sequenceBarrier)
    {
        final WorkerPoolInfo<T> workerPoolInfo = new WorkerPoolInfo<T>(workerPool, sequenceBarrier);
        consumerInfos.add(workerPoolInfo);
        for (Sequence sequence : workerPool.getWorkerSequences())
        {
            eventProcessorInfoBySequence.put(sequence, workerPoolInfo);
        }
    }

    public Sequence[] getLastSequenceInChain(boolean includeStopped)
    {
        List<Sequence> lastSequence = new ArrayList<Sequence>();
        for (ConsumerInfo consumerInfo : consumerInfos)
        {
            if ((includeStopped || consumerInfo.isRunning()) && consumerInfo.isEndOfChain())
            {
                final Sequence[] sequences = consumerInfo.getSequences();
                Collections.addAll(lastSequence, sequences);
            }
        }

        return lastSequence.toArray(new Sequence[lastSequence.size()]);
    }

    public MultiEventProcessor getEventProcessorFor(final EventHandler<T> handler)
    {
        final MultiEventProcessorInfo<T> eventprocessorInfoMulti = getEventProcessorInfo(handler);
        if (eventprocessorInfoMulti == null)
        {
            throw new IllegalArgumentException("The event handler " + handler + " is not processing events.");
        }

        return eventprocessorInfoMulti.getEventProcessor();
    }

    public Sequence getSequenceFor(final EventHandler<T> handler)
    {
        return getEventProcessorFor(handler).getSequence(disruptor.getRingBuffer());
    }

    public void unMarkEventProcessorsAsEndOfChain(final Sequence... barrierEventProcessors)
    {
        for (Sequence barrierEventProcessor : barrierEventProcessors)
        {
            getEventProcessorInfo(barrierEventProcessor).markAsUsedInBarrier();
        }
    }

    @Override
    public Iterator<ConsumerInfo> iterator()
    {
        return consumerInfos.iterator();
    }

    public SequenceBarrier getBarrierFor(final EventHandler<T> handler)
    {
        final ConsumerInfo consumerInfo = getEventProcessorInfo(handler);
        return consumerInfo != null ? consumerInfo.getBarrier() : null;
    }

    private MultiEventProcessorInfo<T> getEventProcessorInfo(final EventHandler<T> handler)
    {
        return eventProcessorInfoByEventHandler.get(handler);
    }

    private ConsumerInfo getEventProcessorInfo(final Sequence barrierEventProcessor)
    {
        return eventProcessorInfoBySequence.get(barrierEventProcessor);
    }
}
