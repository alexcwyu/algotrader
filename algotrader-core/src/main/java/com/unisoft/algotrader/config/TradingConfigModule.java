package com.unisoft.algotrader.config;

import com.unisoft.algotrader.model.clock.Clock;
import com.unisoft.algotrader.model.clock.RealTimeClock;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.persistence.TradingDataStore;
import com.unisoft.algotrader.persistence.cassandra.CassandraRefDataStore;
import com.unisoft.algotrader.persistence.cassandra.CassandraTradingDataStore;
import com.unisoft.algotrader.utils.config.BaseConfigModule;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by alex on 7/16/15.
 */
public class TradingConfigModule extends BaseConfigModule {

    protected void configure() {
        super.configure();
        bind(Clock.class).to(RealTimeClock.class);
        bind(RefDataStore.class).to(CassandraRefDataStore.class);
        bind(TradingDataStore.class).to(CassandraTradingDataStore.class);
        bind(ExecutorService.class).toInstance(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
    }
}