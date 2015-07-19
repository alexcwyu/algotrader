package com.unisoft.algotrader.config;

import com.unisoft.algotrader.model.clock.Clock;
import com.unisoft.algotrader.provider.execution.simulation.SimulationExecutor;
import com.unisoft.algotrader.trading.InstrumentDataManager;
import com.unisoft.algotrader.trading.OrderManager;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by alex on 7/20/15.
 */
public class SimulationExecutorProvider implements Provider<SimulationExecutor> {

    private OrderManager orderManager;
    private InstrumentDataManager instrumentDataManager;
    private Clock clock;

    @Inject
    public SimulationExecutorProvider(OrderManager orderManager, InstrumentDataManager instrumentDataManager, Clock clock){
        this.orderManager = orderManager;
        this.instrumentDataManager = instrumentDataManager;
        this.clock = clock;
    }

    @Override
    public SimulationExecutor get() {
        return new SimulationExecutor(orderManager,instrumentDataManager,clock);
    }
}
