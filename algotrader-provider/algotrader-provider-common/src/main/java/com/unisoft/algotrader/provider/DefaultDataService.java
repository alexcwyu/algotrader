package com.unisoft.algotrader.provider;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.event.data.MarketDataContainer;
import com.unisoft.algotrader.provider.realtime.RealTimeDataProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

    private Map<SubscriptionKey, Set<Subscriber>> subscriptions = Maps.newHashMap();

    @Inject
    public DefaultDataService(ProviderManager providerManager){
        this.providerManager = providerManager;
    }

    @Override
    public List<MarketDataContainer> loadHistoricalData(HistoricalSubscriptionKey subscriptionKey) {
        return null;
    }

    @Override
    public boolean subscribeRealTimeData(SubscriptionKey subscriptionKey, Subscriber subscriber) {
        lock.lock();
        try{
            Set<Subscriber> subscribers = subscriptions.get(subscriptionKey);
            if (subscribers == null){
                subscribers = new HashSet<>();
                subscriptions.put(subscriptionKey, subscribers);
            }
            if(subscribers.add(subscriber)){
                RealTimeDataProvider provider = providerManager.getRealTimeDataProvider(subscriptionKey.providerId);
                if (provider != null){
                    provider.subscribe(subscriptionKey, subscriber);
                    return true;
                }
            }
        }
        finally {
            lock.unlock();
        }
        return false;
    }

    @Override
    public boolean unSubscribeRealTimeData(SubscriptionKey subscriptionKey, Subscriber subscriber) {
        lock.lock();
        try{
            Set<Subscriber> subscribers = subscriptions.get(subscriptionKey);
            if (subscribers != null){
                subscribers = new HashSet<>();
                return subscribers.remove(subscriber);
            }
        }finally {
            lock.unlock();
        }
        return false;
    }
}
