package com.unisoft.algotrader.config;

import com.unisoft.algotrader.model.clock.Clock;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.provider.execution.simulation.SimulationExecutor;
import com.unisoft.algotrader.trading.InstrumentDataManager;
import com.unisoft.algotrader.trading.OrderManager;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by alex on 7/20/15.
 */
public class SimulationExecutorProvider implements Provider<SimulationExecutor> {

    private ProviderManager providerManager;
    private OrderManager orderManager;
    private InstrumentDataManager instrumentDataManager;
    private Clock clock;

    @Inject
    public SimulationExecutorProvider(ProviderManager providerManager, OrderManager orderManager, InstrumentDataManager instrumentDataManager, Clock clock){
        this.providerManager = providerManager;
        this.orderManager = orderManager;
        this.instrumentDataManager = instrumentDataManager;
        this.clock = clock;
    }

    @Override
    public SimulationExecutor get() {
        return new SimulationExecutor(providerManager, orderManager, instrumentDataManager, clock);
    }
}
