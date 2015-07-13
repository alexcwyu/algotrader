package com.unisoft.algotrader.utils.threading.disruptor;

/**
 * Created by alex on 4/12/15.
 */
public interface EventHandler<T> {
    public void onEvent(T event);
}
