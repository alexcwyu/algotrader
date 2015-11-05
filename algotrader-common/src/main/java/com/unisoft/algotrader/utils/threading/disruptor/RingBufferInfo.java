package com.unisoft.algotrader.utils.threading.disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceBarrier;

public class RingBufferInfo<T> {
    final RingBuffer<T> provider;
    final SequenceBarrier barriers;
    final Sequence sequence;
    final EventHandler<? super T> eventHandler;

    public RingBufferInfo(RingBuffer<T> provider, SequenceBarrier barriers, Sequence sequence, EventHandler<? super T> eventHandler) {
        this.provider = provider;
        this.barriers = barriers;
        this.sequence = sequence;
        this.eventHandler = eventHandler;
    }


}
