package com.unisoft.algotrader.config;

import com.google.inject.Provides;
import com.unisoft.algotrader.event.bus.RingBufferMarketDataEventBus;
import com.unisoft.algotrader.model.event.bus.EventBusManager;
import com.unisoft.algotrader.model.event.bus.MarketDataEventBus;
import com.unisoft.algotrader.utils.config.BaseConfigModule;

import javax.inject.Inject;

/**
 * Created by alex on 7/29/15.
 */
public class DefaultEventBusConfigModule extends BaseConfigModule {

    @Provides
    @Inject
    MarketDataEventBus provideEventBus(EventBusManager eventBusManager){
        return new RingBufferMarketDataEventBus(eventBusManager.getMarketDataRB());
    }

}
