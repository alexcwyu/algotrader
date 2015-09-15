package com.unisoft.algotrader.provider.ib;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Sets;
import com.unisoft.algotrader.provider.data.HistoricalSubscriptionKey;
import com.unisoft.algotrader.provider.data.MarketDepthSubscriptionKey;
import com.unisoft.algotrader.provider.data.SubscriptionKey;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;

import java.util.Optional;
import java.util.Set;

/**
 * Created by alex on 9/16/15.
 */
public class SubscriptionRegistry {

    private Set<SubscriptionKey> subscriptionKeys = Sets.newHashSet();
    private BiMap<Long, SubscriptionKey> idSubscriptionMap = HashBiMap.create();

    private TLongObjectMap<DataRecord> marketDataRecords = new TLongObjectHashMap<>();
    private TLongObjectMap<DataRecord> marketDepthRecords = new TLongObjectHashMap<>();
    private TLongObjectMap<DataRecord> historicalDataRecords = new TLongObjectHashMap<>();

    public boolean addSubscription(long requestId, SubscriptionKey subscriptionKey){
        if (!hasSubscription(requestId)){
            subscriptionKeys.add(subscriptionKey);
            idSubscriptionMap.put(requestId, subscriptionKey);

            DataRecord dataRecord = new DataRecord(subscriptionKey.instId);
            if (subscriptionKey instanceof MarketDepthSubscriptionKey) {
                marketDepthRecords.put(requestId, dataRecord);
            } else if (subscriptionKey instanceof HistoricalSubscriptionKey) {
                historicalDataRecords.put(requestId, dataRecord);
            } else {
                marketDataRecords.put(requestId, dataRecord);
            }
            return true;
        }
        return false;
    }

    public SubscriptionKey getSubscriptionKey(long requestId){
        return idSubscriptionMap.get(requestId);
    }

    public Long getSubscriptionId(SubscriptionKey subscriptionKey){
        return idSubscriptionMap.inverse().get(subscriptionKey);
    }


    public boolean removeSubscription(long requestId){
        if (hasSubscription(requestId)){
            SubscriptionKey subscriptionKey = idSubscriptionMap.get(requestId);
            removeSubscription(requestId, subscriptionKey);
            return true;
        }
        return false;
    }

    public boolean removeSubscription(SubscriptionKey subscriptionKey){
        if (hasSubscription(subscriptionKey)){
            Long requestId = getSubscriptionId(subscriptionKey);
            if (requestId != null) {
                removeSubscription(requestId, subscriptionKey);
                return true;
            }
        }
        return false;
    }

    private void removeSubscription(Long requestId, SubscriptionKey subscriptionKey){
        idSubscriptionMap.remove(requestId);
        subscriptionKeys.remove(subscriptionKey);

        if (subscriptionKey instanceof MarketDepthSubscriptionKey) {
            marketDepthRecords.remove(requestId);
        } else if (subscriptionKey instanceof HistoricalSubscriptionKey) {
            historicalDataRecords.remove(requestId);
        } else {
            marketDataRecords.remove(requestId);
        }
    }


    public boolean hasSubscription(long requestId){
        return idSubscriptionMap.containsKey(requestId);
    }


    public boolean hasSubscription(SubscriptionKey subscriptionKey){
        return subscriptionKeys.contains(subscriptionKey);
    }

    public Optional<DataRecord> getDataRecord(long requestId){
        if (hasSubscription(requestId)){
            SubscriptionKey subscriptionKey = getSubscriptionKey(requestId);
            if (subscriptionKey instanceof MarketDepthSubscriptionKey) {
                return Optional.ofNullable(marketDepthRecords.get(requestId));
            } else if (subscriptionKey instanceof HistoricalSubscriptionKey) {
                return Optional.ofNullable(historicalDataRecords.get(requestId));
            } else {
                return Optional.ofNullable(marketDataRecords.get(requestId));
            }
        }
        return Optional.empty();
    }

    public void clear(){
        subscriptionKeys.clear();
        idSubscriptionMap.clear();
        marketDataRecords.clear();
        marketDepthRecords.clear();
        historicalDataRecords.clear();
    }
}
