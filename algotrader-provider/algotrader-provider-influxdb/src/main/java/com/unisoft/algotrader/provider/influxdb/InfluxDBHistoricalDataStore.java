package com.unisoft.algotrader.provider.influxdb;

import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.MarketDataContainer;
import com.unisoft.algotrader.model.event.data.Quote;
import com.unisoft.algotrader.model.event.data.Trade;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.provider.data.AbstractDataStoreProvider;
import com.unisoft.algotrader.provider.data.HistoricalSubscriptionKey;
import com.unisoft.algotrader.provider.data.Subscriber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by alex on 6/19/15.
 */
@Singleton
public class InfluxDBHistoricalDataStore extends AbstractDataStoreProvider{

    private static final Logger LOG = LogManager.getLogger(InfluxDBHistoricalDataStore.class);

    private AtomicBoolean connected = new AtomicBoolean(false);

    private final InfluxDBConfig config;

    public static final String PROVIDER_ID = "InfluxDB";

    @Inject
    public InfluxDBHistoricalDataStore(ProviderManager providerManager, InfluxDBConfig config){
        super(providerManager);
        this.config = config;
    }

    /// PROVIDER
    @Override
    public String providerId() {
        return PROVIDER_ID;
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
    public List<MarketDataContainer> loadHistoricalData(HistoricalSubscriptionKey subscriptionKey) {
        return null;
    }

    @Override
    public boolean subscribeHistoricalData(HistoricalSubscriptionKey subscriptionKey, Subscriber subscriber) {
        return false;
    }
}
