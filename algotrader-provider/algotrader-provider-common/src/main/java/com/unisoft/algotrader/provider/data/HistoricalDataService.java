package com.unisoft.algotrader.provider.data;

import com.unisoft.algotrader.model.event.data.MarketDataContainer;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by alex on 7/18/15.
 */
public interface HistoricalDataService {

    List<MarketDataContainer> loadHistoricalData(HistoricalSubscriptionKey subscriptionKey);

    boolean subscribeHistoricalData(HistoricalSubscriptionKey subscriptionKey, Subscriber subscriber);


    public final static SimpleDateFormat YYYYMMDD_FORMAT = new SimpleDateFormat("yyyyMMdd");
//
//    default void subscribe(EventBus.MarketDataEventBus eventBus, SubscriptionKey subscriptionKey, int from_yyyyMMdd, int to_yyyyMMdd) {
//        try {
//            subscribe(eventBus, subscriptionKey, YYYYMMDD_FORMAT.parse(Integer.toString(from_yyyyMMdd)), YYYYMMDD_FORMAT.parse(Integer.toString(to_yyyyMMdd)));
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    void subscribe(EventBus.MarketDataEventBus eventBus, SubscriptionKey subscriptionKey, Date fromDate, Date toDate);
}
