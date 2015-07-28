package com.unisoft.algotrader.provider.ib;

import ch.aonyx.broker.ib.api.*;
import ch.aonyx.broker.ib.api.net.ConnectionCallback;
import ch.aonyx.broker.ib.api.net.ConnectionException;
import ch.aonyx.broker.ib.api.net.ConnectionParameters;
import com.unisoft.algotrader.model.event.data.MarketDataContainer;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.provider.data.HistoricalDataProvider;
import com.unisoft.algotrader.provider.data.HistoricalSubscriptionKey;
import com.unisoft.algotrader.provider.data.RealTimeDataProvider;
import com.unisoft.algotrader.provider.data.SubscriptionKey;
import com.unisoft.algotrader.provider.execution.ExecutionProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by alex on 6/20/15.
 */
@Singleton
public class IBProvider implements RealTimeDataProvider, HistoricalDataProvider, ExecutionProvider, ConnectionCallback {

    private static final Logger LOG = LogManager.getLogger(IBProvider.class);

    public static final String PROVIDER_ID = "IB";

    private final RefDataStore refDataStore;
    private final IBConfig config;

    private ConnectionParameters connectionParameters;
    private NeoIbApiClient apiClient;
    private Session session;

    @Inject
    public IBProvider(ProviderManager providerManager, RefDataStore refDataStore, IBConfig config){
        this.refDataStore = refDataStore;
        this.config = config;
        providerManager.addExecutionProvider(this);
        providerManager.addHistoricalDataProvider(this);
        providerManager.addRealTimeDataProvider(this);
        apiClient = new NeoIbApiClient(new IBClientCallback());
        connectionParameters = new ConnectionParameters();

    }

    @Override
    public void onSuccess(Session session) {
        this.session = session;
        session.start();
    }

    @Override
    public void onFailure(ConnectionException exception) {
        LOG.error("failure due to exception: ", exception);
    }

    @Override
    public boolean subscribeRealTimeData(SubscriptionKey subscriptionKey){
        return false;
    }

    @Override
    public boolean unSubscribeRealTimeData(SubscriptionKey subscriptionKey){
        return false;
    }

    @Override
    public List<MarketDataContainer> loadHistoricalData(HistoricalSubscriptionKey subscriptionKey) {
        return null;
    }

    @Override
    public boolean subscribeHistoricalData(HistoricalSubscriptionKey subscriptionKey) {
        return false;
    }

    @Override
    public void onOrder(Order order) {

    }


    //TODO
    public void request(SimpleRequest request){
        session.request(request);
    }

    public void orderRequest(OrderRequest request){
        session.orderRequest(request);
    }

    public void subscribe(SubscriptionRequest request){
        session.subscribe(request);
    }

    public void unsubscribe(UnsubscriptionRequest request){
        session.unsubscribe(request);
    }

    public <E extends Event> void registerListener(EventListener<E> listener){
        session.registerListener(listener);
    }

    public <E extends Event> void unregisterListener(EventListener<E> listener){
        session.unregisterListener(listener);
    }


    @Override
    public void connect() {
        apiClient.connect(connectionParameters, this);
    }

    @Override
    public boolean connected() {
        return session != null && session.isStarted();
    }

    @Override
    public void disconnect() {
        session.stop();
        apiClient.disconnect();
    }

    @Override
    public String providerId() {
        return PROVIDER_ID;
    }
}
