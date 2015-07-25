package com.unisoft.algotrader.config;

import com.unisoft.algotrader.model.clock.Clock;
import com.unisoft.algotrader.model.clock.SimulationClock;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.persistence.SampleInMemoryRefDataStore;
import com.unisoft.algotrader.persistence.SampleInMemoryTradingDataStore;
import com.unisoft.algotrader.persistence.TradingDataStore;
import com.unisoft.algotrader.utils.config.BaseConfigModule;

/**
 * Created by alex on 7/16/15.
 */
public class SampleAppConfigModule extends BaseConfigModule {

    protected void configure() {
        super.configure();
        bind(Clock.class).to(SimulationClock.class).asEagerSingleton();
        bind(RefDataStore.class).to(SampleInMemoryRefDataStore.class).asEagerSingleton();
        bind(TradingDataStore.class).to(SampleInMemoryTradingDataStore.class).asEagerSingleton();

    }
}
