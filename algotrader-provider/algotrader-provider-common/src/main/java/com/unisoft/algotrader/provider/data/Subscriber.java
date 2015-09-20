package com.unisoft.algotrader.provider.data;

import com.unisoft.algotrader.model.event.bus.MarketDataEventBus;

/**
 * Created by alex on 18/7/15.
 */
public class Subscriber {
    public final MarketDataEventBus marketDataEventBus;

    public Subscriber(MarketDataEventBus marketDataEventBus){
        this.marketDataEventBus = marketDataEventBus;
    }
}
