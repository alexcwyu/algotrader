package com.unisoft.algotrader.model.event;

/**
 * Created by alex on 9/20/15.
 */
public interface ReuseableEvent<T extends EventHandler, E extends Event<T, ?>> extends Event<T, E>{

    public void reset();

    public void copy(E event);
}
