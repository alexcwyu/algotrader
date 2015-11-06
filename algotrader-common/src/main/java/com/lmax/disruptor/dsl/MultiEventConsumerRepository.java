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
package com.lmax.disruptor.dsl;

import com.lmax.disruptor.*;
import com.unisoft.algotrader.utils.threading.disruptor.MultiEventProcessor;

import java.util.*;

/**
 * Provides a repository mechanism to associate {@link EventHandler}s with {@link EventProcessor}s
 *
 * @param <T> the type of the {@link EventHandler}
 */
class MultiEventConsumerRepository<T> extends ConsumerRepository<T>
{

    private final MultiEventDisruptor<T> disruptor;

    public MultiEventConsumerRepository(MultiEventDisruptor<T> disruptor){
        this.disruptor = disruptor;
    }

    public Sequence getSequenceFor(final EventHandler<T> handler)
    {
        EventProcessor ep = getEventProcessorFor(handler);
        if (ep instanceof MultiEventProcessor)
            return ((MultiEventProcessor) ep).getSequence(disruptor.getRingBuffer());
        return ep.getSequence();
    }

}
