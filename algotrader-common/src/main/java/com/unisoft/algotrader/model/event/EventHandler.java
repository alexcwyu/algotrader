package com.unisoft.algotrader.model.event;

/**
 * Created by alex on 4/16/15.
 */
public interface EventHandler extends com.lmax.disruptor.EventHandler<Event>{

    default void onEvent(Event event){
        event.on(this);
    }

    default void onEvent(Event event, long sequence, boolean endOfBatch) throws Exception{
        onEvent(event);
    }
}

