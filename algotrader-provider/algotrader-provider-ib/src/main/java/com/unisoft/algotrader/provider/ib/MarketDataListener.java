package com.unisoft.algotrader.provider.ib;

import ch.aonyx.broker.ib.api.Id;
import com.unisoft.algotrader.provider.data.SubscriptionKey;

/**
 * Created by alex on 7/27/15.
 */
public abstract class MarketDataListener {

    public final IBProvider ibProvider;

    public MarketDataListener(IBProvider ibProvider){
        this.ibProvider = ibProvider;
    }
    public void publish(
            final Id id,
            final long dateTime,
            final double open,
            final double high,
            final double low,
            final double close,
            final long volume){
        SubscriptionKey subscriptionKey = ibProvider.getSubscriptionKey(id);
        ibProvider.marketDataEventBus.publishBar(subscriptionKey.instId, subscriptionKey.barSize, dateTime,
                open, high, low, close, volume, 0);

    }

    public void publish(
            final Id id,
            final String dateTimeStr,
            final double open,
            final double high,
            final double low,
            final double close,
            final long volume){
        long dateTime = IBUtils.convertDate(dateTimeStr);
        publish(id, dateTime, open, high, low, close, volume);

    }
}
