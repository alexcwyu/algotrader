package com.unisoft.algotrader.event;

/**
 * Created by alex on 4/12/15.
 */
public interface Event<T extends EventHandler, E extends Event<T, ?>> {
    public void on(T handler);

    public void reset();

    public void copy(E event);
}
