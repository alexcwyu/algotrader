package com.unisoft.algotrader.provider;

import com.unisoft.algotrader.model.event.EventBus;
import com.unisoft.algotrader.model.event.data.MarketDataContainer;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by alex on 18/7/15.
 *
 */
public interface DataService {
    List<MarketDataContainer> loadHistoricalData(HistoricalSubscriptionKey subscriptionKey);

    boolean subscribeRealTimeData(SubscriptionKey subscriptionKey, Subscriber subscriber);

    boolean unSubscribeRealTimeData(SubscriptionKey subscriptionKey, Subscriber subscriber);
}
