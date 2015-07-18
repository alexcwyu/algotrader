package com.unisoft.algotrader.provider;

import com.unisoft.algotrader.model.event.EventBus;
/**
 * Created by alex on 18/7/15.
 */
public class Subscriber {
    public final EventBus.MarketDataEventBus marketDataEventBus;

    public Subscriber(EventBus.MarketDataEventBus marketDataEventBus){
        this.marketDataEventBus = marketDataEventBus;
    }
}
