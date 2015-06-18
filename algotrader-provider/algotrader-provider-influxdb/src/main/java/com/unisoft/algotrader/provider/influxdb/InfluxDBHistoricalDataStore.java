package com.unisoft.algotrader.provider.influxdb;

import com.unisoft.algotrader.event.EventBus;
import com.unisoft.algotrader.event.data.Bar;
import com.unisoft.algotrader.event.data.Quote;
import com.unisoft.algotrader.event.data.Trade;
import com.unisoft.algotrader.provider.SubscriptionKey;
import com.unisoft.algotrader.provider.csv.DataStore;
import com.unisoft.algotrader.provider.csv.historical.HistoricalDataProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by alex on 6/19/15.
 */
public class InfluxDBHistoricalDataStore implements DataStore, HistoricalDataProvider {

    private static final Logger LOG = LogManager.getLogger(InfluxDBHistoricalDataStore.class);

    private AtomicBoolean connected = new AtomicBoolean(false);

    /// PROVIDER
    @Override
    public String providerId() {
        return "InfluxDB";
    }

    @Override
    public boolean connected() {
        return connected.get();
    }

    @Override
    public void connect(){
        if (connected.compareAndSet(false, true)){
            //connect
        }
    }

    @Override
    public void disconnect(){
        if (connected.compareAndSet(true, false)){
            //close
        }
    }

    /// DATASTORE
    @Override
    public void onBar(Bar bar) {
    }

    @Override
    public void onQuote(Quote quote) {
    }

    @Override
    public void onTrade(Trade trade) {
    }

    /// PROVIDER
    @Override
    public void subscribe(EventBus.MarketDataEventBus eventBus, SubscriptionKey subscriptionKey, Date fromDate, Date toDate) {

    }
}
