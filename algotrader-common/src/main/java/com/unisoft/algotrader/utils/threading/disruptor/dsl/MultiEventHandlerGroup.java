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

import com.lmax.disruptor.EventProcessor;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.unisoft.algotrader.utils.threading.disruptor.MultiEventProcessor;

import java.util.Arrays;

/**
 * A group of {@link EventProcessor}s used as part of the {@link Disruptor}.
 *
 * @param <T> the type of entry used by the event processors.
 */
public class MultiEventHandlerGroup<T>
{
    private final MultiEventDisruptor<T> disruptor;
    private final ConsumerRepository<T> consumerRepository;
    private final Sequence[] sequences;

    MultiEventHandlerGroup(final MultiEventDisruptor<T> disruptor,
                           final ConsumerRepository<T> consumerRepository,
                           final Sequence[] sequences)
    {
        this.disruptor = disruptor;
        this.consumerRepository = consumerRepository;
        this.sequences = Arrays.copyOf(sequences, sequences.length);
    }

    /**
     * Create a new event handler group that combines the consumers in this group with <tt>otherHandlerGroup</tt>.
     *
     * @param otherHandlerGroup the event handler group to combine.
     * @return a new MultiEventHandlerGroup combining the existing and new consumers into a single dependency group.
     */
    public MultiEventHandlerGroup<T> and(final MultiEventHandlerGroup<T> otherHandlerGroup)
    {
        final Sequence[] combinedSequences = new Sequence[this.sequences.length + otherHandlerGroup.sequences.length];
        System.arraycopy(this.sequences, 0, combinedSequences, 0, this.sequences.length);
        System.arraycopy(otherHandlerGroup.sequences, 0, combinedSequences, this.sequences.length, otherHandlerGroup.sequences.length);
        return new MultiEventHandlerGroup<T>(disruptor, consumerRepository, combinedSequences);
    }

    /**
     * Create a new event handler group that combines the handlers in this group with <tt>processors</tt>.
     *
     * @param processors the processors to combine.
     * @return a new MultiEventHandlerGroup combining the existing and new processors into a single dependency group.
     */
    public MultiEventHandlerGroup<T> and(final MultiEventProcessor... processors)
    {
        Sequence[] combinedSequences = new Sequence[sequences.length + processors.length];

        for (int i = 0; i < processors.length; i++)
        {
            consumerRepository.add(processors[i], disruptor.getRingBuffer());
            combinedSequences[i] = processors[i].getSequence(disruptor.getRingBuffer());
        }
        System.arraycopy(sequences, 0, combinedSequences, processors.length, sequences.length);

        return new MultiEventHandlerGroup<T>(disruptor, consumerRepository, combinedSequences);
    }

    /**
     * Set up batch handlers to consume events from the ring buffer. These handlers will only process events
     * after every {@link EventProcessor} in this group has processed the event.
     *
     * <p>This method is generally used as part of a chain. For example if the handler <code>A</code> must
     * process events before handler <code>B</code>:</p>
     *
     * <pre><code>dw.handleEventsWith(A).then(B);</code></pre>
     *
     * @param handlers the batch handlers that will process events.
     * @return a {@link MultiEventHandlerGroup} that can be used to set up a event processor barrier over the created event processors.
     */
    public MultiEventHandlerGroup<T> then(final MultiEventProcessor... processors)
    {
        return handleEventsWith(processors);
    }

    /**
     * Set up a worker pool to handle events from the ring buffer. The worker pool will only process events
     * after every {@link EventProcessor} in this group has processed the event. Each event will be processed
     * by one of the work handler instances.
     *
     * <p>This method is generally used as part of a chain. For example if the handler <code>A</code> must
     * process events before the worker pool with handlers <code>B, C</code>:</p>
     *
     * <pre><code>dw.handleEventsWith(A).thenHandleEventsWithWorkerPool(B, C);</code></pre>
     *
     * @param handlers the work handlers that will process events. Each work handler instance will provide an extra thread in the worker pool.
     * @return a {@link MultiEventHandlerGroup} that can be used to set up a event processor barrier over the created event processors.
     */
    public MultiEventHandlerGroup<T> thenHandleEventsWithWorkerPool(final WorkHandler<? super T>... handlers)
    {
        return handleEventsWithWorkerPool(handlers);
    }

    /**
     * Set up batch handlers to handle events from the ring buffer. These handlers will only process events
     * after every {@link EventProcessor} in this group has processed the event.
     *
     * <p>This method is generally used as part of a chain. For example if <code>A</code> must
     * process events before <code>B</code>:</p>
     *
     * <pre><code>dw.after(A).handleEventsWith(B);</code></pre>
     *
     * @param handlers the batch handlers that will process events.
     * @return a {@link MultiEventHandlerGroup} that can be used to set up a event processor barrier over the created event processors.
     */
    public MultiEventHandlerGroup<T> handleEventsWith(final MultiEventProcessor... processors)
    {
        return disruptor.createEventProcessors(sequences, processors);
    }

    /**
     * Set up a worker pool to handle events from the ring buffer. The worker pool will only process events
     * after every {@link EventProcessor} in this group has processed the event. Each event will be processed
     * by one of the work handler instances.
     *
     * <p>This method is generally used as part of a chain. For example if the handler <code>A</code> must
     * process events before the worker pool with handlers <code>B, C</code>:</p>
     *
     * <pre><code>dw.after(A).handleEventsWithWorkerPool(B, C);</code></pre>
     *
     * @param handlers the work handlers that will process events. Each work handler instance will provide an extra thread in the worker pool.
     * @return a {@link MultiEventHandlerGroup} that can be used to set up a event processor barrier over the created event processors.
     */
    public MultiEventHandlerGroup<T> handleEventsWithWorkerPool(final WorkHandler<? super T>... handlers)
    {
        return disruptor.createWorkerPool(sequences, handlers);
    }

    /**
     * Create a dependency barrier for the processors in this group.
     * This allows custom event processors to have dependencies on
     * {@link com.lmax.disruptor.BatchEventProcessor}s created by the disruptor.
     *
     * @return a {@link SequenceBarrier} including all the processors in this group.
     */
    public SequenceBarrier asSequenceBarrier()
    {
        return disruptor.getRingBuffer().newBarrier(sequences);
    }
}
