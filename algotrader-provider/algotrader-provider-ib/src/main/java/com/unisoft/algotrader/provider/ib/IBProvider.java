package com.unisoft.algotrader.provider.ib;


import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.event.EventBus;
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
import com.unisoft.algotrader.provider.ib.api.IBSession;
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
public class IBProvider implements RealTimeDataProvider, HistoricalDataProvider, ExecutionProvider{

    private static final Logger LOG = LogManager.getLogger(IBProvider.class);

    public static final String PROVIDER_ID = "IB";

    private final RefDataStore refDataStore;
    private final IBConfig config;
    public final EventBusManager eventBusManager;

    private IBSession session;

    private Set<SubscriptionKey> subscriptionKeys = Sets.newHashSet();
    private Map<Long, SubscriptionKey> idSubscriptionMap = Maps.newHashMap();

    @Inject
    public IBProvider(ProviderManager providerManager, IBConfig config, RefDataStore refDataStore, EventBusManager eventBusManager){
        this.refDataStore = refDataStore;
        this.config = config;
        this.eventBusManager = eventBusManager;
        providerManager.addExecutionProvider(this);
        providerManager.addHistoricalDataProvider(this);
        providerManager.addRealTimeDataProvider(this);
        session = new IBSession(config, refDataStore, eventBusManager);

    }

    public SubscriptionKey getSubscriptionKey(long id){
        return idSubscriptionMap.get(id);
    }


    @Override
    public boolean subscribeRealTimeData(SubscriptionKey subscriptionKey){
        subscriptionKeys.add(subscriptionKey);

        RealTimeBarSubscriptionRequest request = IBUtils.createRealTimeBarSubscriptionRequest(subscriptionKey, refDataStore.getInstrument(subscriptionKey.instId));

        idSubscriptionMap.put(request.getId(), subscriptionKey);
        session.subscribe(request);
        return true;
    }

    @Override
    public boolean unSubscribeRealTimeData(SubscriptionKey subscriptionKey){
        UnsubscriptionRequest request = IBUtils.createRealTimeBarUnsubscriptionRequest(refDataStore.getInstrument(subscriptionKey.instId));
        session.unsubscribe(request);
        subscriptionKeys.remove(subscriptionKey);
        return true;
    }

    @Override
    public List<MarketDataContainer> loadHistoricalData(HistoricalSubscriptionKey subscriptionKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean subscribeHistoricalData(HistoricalSubscriptionKey subscriptionKey) {
        subscriptionKeys.add(subscriptionKey);
        HistoricalDataSubscriptionRequest request = IBUtils
                .createHistoricalDataSubscriptionRequest(subscriptionKey,
                        refDataStore.getInstrument(subscriptionKey.instId));

        idSubscriptionMap.put(request.getId(), subscriptionKey);
        session.subscribe(request);
        return true;
    }

    @Override
    public void onOrder(Order order) {
        Contract contract = IBUtils.getContract(refDataStore.getInstrument(order.instId));
        session.orderRequest(new PlaceOrderRequest(IBUtils.convertOrder(order),contract));
    }


    //TODO
    public void request(SimpleRequest request){
        session.request(request);
    }

    public <E extends Event> void registerListener(EventListener<E> listener){
        session.registerListener(listener);
    }

    public <E extends Event> void unregisterListener(EventListener<E> listener){
        session.unregisterListener(listener);
    }


    @Override
    public void connect() {
        session.connect();
    }

    @Override
    public boolean connected() {
        return session != null && session.isConnected();
    }

    @Override
    public void disconnect() {
        session.disconnect();
    }

    @Override
    public String providerId() {
        return PROVIDER_ID;
    }
}
