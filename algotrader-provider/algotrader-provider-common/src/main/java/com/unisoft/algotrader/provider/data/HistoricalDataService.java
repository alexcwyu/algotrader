package com.unisoft.algotrader.provider.data;

import com.unisoft.algotrader.model.event.data.MarketDataContainer;

import java.util.List;

/**
 * Created by alex on 7/18/15.
 */
public interface HistoricalDataService {

    List<MarketDataContainer> loadHistoricalData(HistoricalSubscriptionKey subscriptionKey);

    boolean subscribeHistoricalData(HistoricalSubscriptionKey subscriptionKey, Subscriber subscriber);

}
