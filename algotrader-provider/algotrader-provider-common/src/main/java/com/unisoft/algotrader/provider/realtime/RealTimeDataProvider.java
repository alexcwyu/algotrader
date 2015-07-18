package com.unisoft.algotrader.provider.realtime;

import com.unisoft.algotrader.provider.DataProvider;
import com.unisoft.algotrader.provider.Subscriber;
import com.unisoft.algotrader.provider.SubscriptionKey;

/**
 * Created by alex on 5/17/15.
 */
public interface RealTimeDataProvider extends DataProvider {
    void subscribe(SubscriptionKey subscriptionKey, Subscriber subscriber);
}
