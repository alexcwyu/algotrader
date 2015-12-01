package com.unisoft.algotrader.config;

import com.unisoft.algotrader.event.bus.DefaultEventBusManager;
import com.unisoft.algotrader.model.clock.Clock;
import com.unisoft.algotrader.model.clock.SimulationClock;
import com.unisoft.algotrader.model.event.bus.EventBusManager;
import com.unisoft.algotrader.persistence.InMemoryRefDataStore;
import com.unisoft.algotrader.persistence.InMemoryTradingDataStore;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.persistence.TradingDataStore;
import com.unisoft.algotrader.trading.StrategyManager;
import com.unisoft.algotrader.utils.config.BaseConfigModule;
import com.unisoft.algotrader.utils.id.AtomicIntIdSupplier;

import java.util.function.Supplier;

/**
 * Created by alex on 7/16/15.
 */
public class AppConfigModule extends BaseConfigModule{

    protected void configure() {
        super.configure();
        bind(Clock.class).to(SimulationClock.class);
        bind(RefDataStore.class).to(InMemoryRefDataStore.class);
        bind(TradingDataStore.class).to(InMemoryTradingDataStore.class);
        bind(EventBusManager.class).to(DefaultEventBusManager.class);
    }
}
