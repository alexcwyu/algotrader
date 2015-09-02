package com.unisoft.algotrader.provider.ib;


import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.unisoft.algotrader.model.event.EventBusManager;
import com.unisoft.algotrader.model.event.data.MarketDataContainer;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.provider.data.HistoricalDataProvider;
import com.unisoft.algotrader.provider.data.HistoricalSubscriptionKey;
import com.unisoft.algotrader.provider.data.RealTimeDataProvider;
import com.unisoft.algotrader.provider.data.SubscriptionKey;
import com.unisoft.algotrader.provider.execution.ExecutionProvider;
import com.unisoft.algotrader.provider.ib.api.event.DefaultIBEventHandler;
import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by alex on 6/20/15.
 */
@Singleton
public class IBProvider extends DefaultIBEventHandler implements IBEventHandler, RealTimeDataProvider, HistoricalDataProvider, ExecutionProvider{

    private static final Logger LOG = LogManager.getLogger(IBProvider.class);

    public static final String PROVIDER_ID = "IB";

    private final IBConfig config;
    private final RefDataStore refDataStore;
    public final EventBusManager eventBusManager;

    private final IBSocket ibSocket;
    private Set<SubscriptionKey> subscriptionKeys = Sets.newHashSet();
    private Map<Long, SubscriptionKey> idSubscriptionMap = Maps.newHashMap();

    private int requestId = 0;

    @Inject
    public IBProvider(ProviderManager providerManager, IBConfig config, RefDataStore refDataStore, EventBusManager eventBusManager){
        this.refDataStore = refDataStore;
        this.config = config;
        this.eventBusManager = eventBusManager;
        providerManager.addExecutionProvider(this);
        providerManager.addHistoricalDataProvider(this);
        providerManager.addRealTimeDataProvider(this);
        this.ibSocket = new IBSocket(this);
    }

    @Override
    public boolean subscribeRealTimeData(SubscriptionKey subscriptionKey){
        subscriptionKey.setSubscriptionId(nextRequestId());
        subscriptionKeys.add(subscriptionKey);
        idSubscriptionMap.put(subscriptionKey.getSubscriptionId(), subscriptionKey);
        ibSocket.subscribeRealTimeData(subscriptionKey);
        return true;
    }

    @Override
    public boolean unSubscribeRealTimeData(SubscriptionKey subscriptionKey){
        subscriptionKeys.remove(subscriptionKey);
        ibSocket.unsubscribeRealTimeData(subscriptionKey);
        return true;
    }

    @Override
    public List<MarketDataContainer> loadHistoricalData(HistoricalSubscriptionKey subscriptionKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean subscribeHistoricalData(HistoricalSubscriptionKey subscriptionKey) {
        subscriptionKey.setSubscriptionId(nextRequestId());
        subscriptionKeys.add(subscriptionKey);
        idSubscriptionMap.put(subscriptionKey.getSubscriptionId(), subscriptionKey);
        ibSocket.subscribeHistoricalData(subscriptionKey);
        return true;
    }

    @Override
    public void onNewOrderRequest(Order order) {
        ibSocket.sendOrder(order);
    }

    @Override
    public void onOrderReplaceRequest(Order order){
        ibSocket.sendOrder(order);
    }

    @Override
    public void onOrderCancelRequest(Order order){
        ibSocket.cancelOrder(order.orderId);
    }

    @Override
    public void connect() {
        ibSocket.connect();
    }

    @Override
    public boolean connected() {
        return ibSocket != null && ibSocket.isConnected();
    }

    @Override
    public void disconnect() {
        ibSocket.disconnect();
    }

    @Override
    public String providerId() {
        return PROVIDER_ID;
    }

    public RefDataStore getRefDataStore() {
        return refDataStore;
    }

    public EventBusManager getEventBusManager() {
        return eventBusManager;
    }

    public IBConfig getConfig() {
        return config;
    }

    public IBSocket getIbSocket() {
        return ibSocket;
    }

    private int nextRequestId(){
        return requestId++;
    }

    public void onNextValidOrderIdEvent(final int nextValidOrderId){
        this.requestId = nextValidOrderId;
    }
}
