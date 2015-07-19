package com.unisoft.algotrader.provider.ib;

import com.unisoft.algotrader.model.event.data.MarketDataContainer;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.provider.data.*;
import com.unisoft.algotrader.provider.execution.ExecutionProvider;

import java.util.List;

/**
 * Created by alex on 6/20/15.
 */
public class IBProvider implements RealTimeDataProvider, HistoricalDataProvider, ExecutionProvider{

    @Override
    public boolean subscribeRealTimeData(SubscriptionKey subscriptionKey, Subscriber subscriber){
        return false;
    }

    @Override
    public boolean unSubscribeRealTimeData(SubscriptionKey subscriptionKey, Subscriber subscriber){

        return false;
    }

    @Override
    public List<MarketDataContainer> loadHistoricalData(HistoricalSubscriptionKey subscriptionKey) {
        return null;
    }

    @Override
    public boolean subscribeHistoricalData(HistoricalSubscriptionKey subscriptionKey, Subscriber subscriber) {
        return false;
    }

    @Override
    public void onOrder(Order order) {

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
