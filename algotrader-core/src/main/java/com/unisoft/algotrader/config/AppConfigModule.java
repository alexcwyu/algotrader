package com.unisoft.algotrader.config;

import com.unisoft.algotrader.model.clock.Clock;
import com.unisoft.algotrader.model.clock.SimulationClock;
import com.unisoft.algotrader.persistence.InMemoryRefDataStore;
import com.unisoft.algotrader.persistence.InMemoryTradingDataStore;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.persistence.TradingDataStore;
import com.unisoft.algotrader.utils.config.BaseConfigModule;

/**
 * Created by alex on 7/16/15.
 */
public class AppConfigModule extends BaseConfigModule{

    protected void configure() {
        super.configure();
        bind(Clock.class).to(SimulationClock.class);
        bind(RefDataStore.class).to(InMemoryRefDataStore.class);
        bind(TradingDataStore.class).to(InMemoryTradingDataStore.class);

    }
}
