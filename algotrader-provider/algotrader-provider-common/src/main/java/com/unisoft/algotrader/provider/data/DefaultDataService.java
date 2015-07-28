package com.unisoft.algotrader.provider.data;

import com.google.common.collect.Sets;
import com.unisoft.algotrader.model.event.data.MarketDataContainer;
import com.unisoft.algotrader.provider.ProviderManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by alex on 18/7/15.
 */
@Singleton
public class DefaultDataService implements DataService {

    private final ProviderManager providerManager;

    private Lock lock = new ReentrantLock();

    private Set<SubscriptionKey> subscriptions = Sets.newHashSet();

    @Inject
    public DefaultDataService(ProviderManager providerManager){
        this.providerManager = providerManager;
    }

    @Override
    public List<MarketDataContainer> loadHistoricalData(HistoricalSubscriptionKey subscriptionKey) {
        HistoricalDataProvider historicalDataProvider = providerManager.getHistoricalDataProvider(subscriptionKey.providerId);
        if (historicalDataProvider != null){
            return historicalDataProvider.loadHistoricalData(subscriptionKey);
        }
        return null;
    }

    @Override
    public boolean subscribeHistoricalData(HistoricalSubscriptionKey subscriptionKey) {
        HistoricalDataProvider historicalDataProvider = providerManager.getHistoricalDataProvider(subscriptionKey.providerId);
        if (historicalDataProvider != null){
            historicalDataProvider.subscribeHistoricalData(subscriptionKey);
            return true;
        }
        return false;
    }

    @Override
    public boolean subscribeRealTimeData(SubscriptionKey subscriptionKey) {
        lock.lock();
        try {
            RealTimeDataProvider provider = providerManager.getRealTimeDataProvider(subscriptionKey.providerId);
            if (provider != null) {
                provider.subscribeRealTimeData(subscriptionKey);
                return true;
            }

        }
        finally {
            lock.unlock();
        }
        return false;
    }

    @Override
    public boolean unSubscribeRealTimeData(SubscriptionKey subscriptionKey) {
        lock.lock();
        try {

            RealTimeDataProvider provider = providerManager.getRealTimeDataProvider(subscriptionKey.providerId);
            if (provider != null) {
                provider.unSubscribeRealTimeData(subscriptionKey);
                return true;
            }

        }finally {
            lock.unlock();
        }
        return false;
    }
}
