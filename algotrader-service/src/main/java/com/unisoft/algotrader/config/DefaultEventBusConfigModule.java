package com.unisoft.algotrader.config;

import com.google.inject.Provides;
import com.unisoft.algotrader.model.event.EventBusManager;
import com.unisoft.algotrader.event.RingBufferMarketDataEventBus;
import com.unisoft.algotrader.model.event.EventBus;
import com.unisoft.algotrader.utils.config.BaseConfigModule;

import javax.inject.Inject;

/**
 * Created by alex on 7/29/15.
 */
public class DefaultEventBusConfigModule extends BaseConfigModule {

    @Provides
    @Inject
    EventBus.MarketDataEventBus provideEventBus(EventBusManager eventBusManager){
        return new RingBufferMarketDataEventBus(eventBusManager.marketDataRB);
    }

}
