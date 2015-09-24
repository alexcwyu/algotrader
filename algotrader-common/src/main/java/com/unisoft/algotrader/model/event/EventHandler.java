package com.unisoft.algotrader.model.event;

/**
 * Created by alex on 4/16/15.
 */
public interface EventHandler {

    default void onEvent(Event event){
        event.on(this);
    }
}

