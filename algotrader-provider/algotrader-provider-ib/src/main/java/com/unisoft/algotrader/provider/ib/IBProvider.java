package com.unisoft.algotrader.provider.ib;

import com.unisoft.algotrader.model.event.EventBus;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.provider.Subscriber;
import com.unisoft.algotrader.provider.SubscriptionKey;
import com.unisoft.algotrader.provider.execution.ExecutionProvider;
import com.unisoft.algotrader.provider.historical.HistoricalDataProvider;
import com.unisoft.algotrader.provider.realtime.RealTimeDataProvider;

import java.util.Date;

/**
 * Created by alex on 6/20/15.
 */
public class IBProvider implements RealTimeDataProvider, HistoricalDataProvider, ExecutionProvider{

    @Override
    public void subscribe(EventBus.MarketDataEventBus eventBus, SubscriptionKey subscriptionKey, int from_yyyyMMdd, int to_yyyyMMdd) {

    }

    @Override
    public void subscribe(EventBus.MarketDataEventBus eventBus, SubscriptionKey subscriptionKey, Date fromDate, Date toDate) {

    }

    @Override
    public void onOrder(Order order) {

    }

    @Override
    public void subscribe(SubscriptionKey subscriptionKey, Subscriber subscriber) {

    }

    @Override
    public void connect() {

    }

    @Override
    public boolean connected() {
        return false;
    }

    @Override
    public void disconnect() {

    }

    @Override
    public String providerId() {
        return null;
    }
}
