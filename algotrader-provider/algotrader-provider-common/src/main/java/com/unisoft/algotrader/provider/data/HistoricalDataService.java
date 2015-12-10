package com.unisoft.algotrader.provider.data;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.event.data.MarketDataContainer;

import java.util.List;
import java.util.Map;

/**
 * Created by alex on 7/18/15.
 */
public interface HistoricalDataService {


    default Map<HistoricalSubscriptionKey, List<MarketDataContainer>> loadAllHistoricalData(HistoricalSubscriptionKey ... subscriptionKeys){
        Map<HistoricalSubscriptionKey, List<MarketDataContainer>> result = Maps.newHashMap();
        for (HistoricalSubscriptionKey subscriptionKey: subscriptionKeys){
            result.put(subscriptionKey, loadHistoricalData(subscriptionKey));
        }
        return result;
    }



    List<MarketDataContainer> loadHistoricalData(HistoricalSubscriptionKey subscriptionKey);

    boolean subscribeHistoricalData(HistoricalSubscriptionKey subscriptionKey);

}
