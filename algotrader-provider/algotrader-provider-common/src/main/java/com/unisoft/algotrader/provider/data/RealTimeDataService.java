package com.unisoft.algotrader.provider.data;

/**
 * Created by alex on 7/18/15.
 */
public interface RealTimeDataService {

    boolean subscribeMarketData(SubscriptionKey subscriptionKey);

    boolean unSubscribeMarketData(SubscriptionKey subscriptionKey);
}
