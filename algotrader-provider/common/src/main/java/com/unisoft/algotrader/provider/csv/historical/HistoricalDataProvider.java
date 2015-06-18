package com.unisoft.algotrader.provider.csv.historical;

import com.unisoft.algotrader.provider.SubscriptionKey;
import com.unisoft.algotrader.provider.csv.DataProvider;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alex on 5/17/15.
 */
public interface HistoricalDataProvider extends DataProvider {

    public final static SimpleDateFormat YYYYMMDD_FORMAT = new SimpleDateFormat("yyyyMMdd");

    default void subscribe(SubscriptionKey subscriptionKey, int from_yyyyMMdd, int to_yyyyMMdd) {
        try {
            subscribe(subscriptionKey, YYYYMMDD_FORMAT.parse(Integer.toString(from_yyyyMMdd)), YYYYMMDD_FORMAT.parse(Integer.toString(to_yyyyMMdd)));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void subscribe(SubscriptionKey subscriptionKey, Date fromDate, Date toDate);

}
