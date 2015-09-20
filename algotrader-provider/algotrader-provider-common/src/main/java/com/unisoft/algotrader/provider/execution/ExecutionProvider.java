package com.unisoft.algotrader.provider.execution;

import com.unisoft.algotrader.model.event.execution.OrderEventHandler;
import com.unisoft.algotrader.provider.Provider;

/**
 * Created by alex on 5/17/15.
 */
public interface ExecutionProvider extends Provider, OrderEventHandler {
}
