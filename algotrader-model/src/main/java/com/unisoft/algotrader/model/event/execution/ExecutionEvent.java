package com.unisoft.algotrader.model.event.execution;

import com.unisoft.algotrader.model.event.Event;

/**
 * Created by alex on 9/20/15.
 */
public abstract class ExecutionEvent <E extends ExecutionEvent<? super E>> implements Event<ExecutionEventHandler, E> {
}
