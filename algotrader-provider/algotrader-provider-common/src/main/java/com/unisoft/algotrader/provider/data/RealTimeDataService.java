package com.unisoft.algotrader.provider.data;

/**
 * Created by alex on 7/18/15.
 */
public interface RealTimeDataService {

    boolean subscribeRealTimeData(SubscriptionKey subscriptionKey, Subscriber subscriber);

    boolean unSubscribeRealTimeData(SubscriptionKey subscriptionKey, Subscriber subscriber);
}
