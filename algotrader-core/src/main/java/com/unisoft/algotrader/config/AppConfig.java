package com.unisoft.algotrader.config;

import com.unisoft.algotrader.model.clock.Clock;
import com.unisoft.algotrader.model.event.bus.EventBusManager;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.persistence.TradingDataStore;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.trading.InstrumentDataManager;
import com.unisoft.algotrader.trading.OrderManager;
import com.unisoft.algotrader.trading.StrategyManager;

import javax.inject.Inject;
import java.util.function.Supplier;

/**
 * Created by alex on 7/16/15.
 */
public class AppConfig {

    @Inject Clock clock;
    @Inject RefDataStore refDataStore;
    @Inject TradingDataStore tradingDataStore;
    @Inject OrderManager orderManager;
    @Inject ProviderManager providerManager;
    @Inject StrategyManager strategyManager;
    @Inject EventBusManager eventBusManager;
    @Inject InstrumentDataManager instrumentDataManager;

    public Clock getClock() {
        return clock;
    }

    public RefDataStore getRefDataStore() {
        return refDataStore;
    }

    public TradingDataStore getTradingDataStore() {
        return tradingDataStore;
    }

    public OrderManager getOrderManager() {
        return orderManager;
    }

    public ProviderManager getProviderManager() {
        return providerManager;
    }

    public StrategyManager getStrategyManager() {
        return strategyManager;
    }

    public EventBusManager getEventBusManager() {
        return eventBusManager;
    }

    public InstrumentDataManager getInstrumentDataManager() {
        return instrumentDataManager;
    }


}
