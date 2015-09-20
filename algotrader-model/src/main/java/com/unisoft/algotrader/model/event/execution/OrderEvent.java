package com.unisoft.algotrader.model.event.execution;

import com.unisoft.algotrader.model.event.Event;

/**
 * Created by alex on 9/20/15.
 */
public abstract class OrderEvent<E extends OrderEvent<? super E>> implements Event<OrderEventHandler, E> {
}
