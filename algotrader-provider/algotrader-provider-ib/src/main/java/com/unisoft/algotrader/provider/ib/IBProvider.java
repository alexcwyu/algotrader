package com.unisoft.algotrader.provider.ib;


import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.unisoft.algotrader.model.event.EventBusManager;
import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.DataType;
import com.unisoft.algotrader.model.event.data.MarketDataContainer;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.provider.data.*;
import com.unisoft.algotrader.provider.execution.ExecutionProvider;
import com.unisoft.algotrader.provider.ib.api.event.*;
import com.unisoft.algotrader.provider.ib.api.model.data.BookSide;
import com.unisoft.algotrader.provider.ib.api.model.data.Operation;
import com.unisoft.algotrader.provider.ib.api.model.data.TickType;
import com.unisoft.algotrader.provider.ib.api.model.fa.FinancialAdvisorDataType;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

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
    private BiMap<Long, SubscriptionKey> idSubscriptionMap = HashBiMap.create();

    private Map<Long, Order> extOrderIdOrderMap = Maps.newHashMap();
    private Map<Long, Order> orderIdOrderMap = Maps.newHashMap();
    private Map<Long, Long> orderIdMap  = Maps.newHashMap();

    private TLongObjectMap<DataRecord> dataRecords = new TLongObjectHashMap<>();
    private TLongObjectMap<DataRecord> historicalDataRecords = new TLongObjectHashMap<>();

    private AtomicInteger requestId = new AtomicInteger(1);
    private AtomicInteger orderId = null;

    @Inject
    public IBProvider(ProviderManager providerManager, IBConfig config, RefDataStore refDataStore, EventBusManager eventBusManager){
        this.refDataStore = refDataStore;
        this.config = config;
        this.eventBusManager = eventBusManager;
        providerManager.addExecutionProvider(this);
        providerManager.addHistoricalDataProvider(this);
        providerManager.addRealTimeDataProvider(this);
        this.ibSocket = new IBSocket(config, this, refDataStore);
    }

    @Override
    public void connect() {
        ibSocket.connect();
        synchronized (this) {
            while (this.orderId == null) {
                try {
                    this.wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
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
        return requestId.getAndIncrement();
    }

    private int nextOrderId(){
        return orderId.getAndIncrement();
    }

    public void onNextValidOrderIdEvent(final int nextValidOrderId){
        this.orderId = new AtomicInteger(nextValidOrderId);
        synchronized (this) {
            this.notify();
        }
    }

    @Override
    //TODO thread safe, lock
    public boolean subscribeMarketData(SubscriptionKey subscriptionKey){
        if (!subscriptionKeys.contains(subscriptionKey)) {
            long requestId = nextRequestId();
            subscriptionKeys.add(subscriptionKey);
            idSubscriptionMap.put(requestId, subscriptionKey);
            dataRecords.put(requestId, new DataRecord(subscriptionKey.instId));
            if (subscriptionKey instanceof MarketDepthSubscriptionKey) {
                ibSocket.subscribeMarketDepth(requestId, (MarketDepthSubscriptionKey) subscriptionKey);
            } else if (subscriptionKey.type == DataType.Quote || subscriptionKey.type == DataType.Trade) {
                ibSocket.subscribeMarketData(requestId, subscriptionKey, false);
            } else if (subscriptionKey.type == DataType.Bar) {
                ibSocket.subscribeRealTimeData(requestId, subscriptionKey);
            } else {
                LOG.warn("unsupported type {}", subscriptionKey);
                throw new IllegalArgumentException("unsupported type " + subscriptionKey);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean unSubscribeMarketData(SubscriptionKey subscriptionKey){
        Long requestId = idSubscriptionMap.inverse().get(subscriptionKey);
        if (requestId != null) {
            ibSocket.unsubscribeRealTimeData(requestId);
            subscriptionKeys.remove(subscriptionKey);
            idSubscriptionMap.remove(requestId);
            dataRecords.remove(requestId);
            return true;
        }
        return false;
    }

    @Override
    public List<MarketDataContainer> loadHistoricalData(HistoricalSubscriptionKey subscriptionKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean subscribeHistoricalData(HistoricalSubscriptionKey subscriptionKey) {
        long requestId = nextRequestId();
        subscriptionKeys.add(subscriptionKey);
        idSubscriptionMap.put(requestId, subscriptionKey);
        historicalDataRecords.put(requestId, new DataRecord(subscriptionKey.instId));
        ibSocket.subscribeHistoricalData(requestId, subscriptionKey);
        return true;
    }

    public boolean unSubscribeHistoricalData(HistoricalSubscriptionKey subscriptionKey){
        Long requestId = idSubscriptionMap.inverse().get(subscriptionKey);
        if (requestId != null) {
            ibSocket.unsubscribeHistoricalData(requestId);
            subscriptionKeys.remove(subscriptionKey);
            idSubscriptionMap.remove(requestId);
            historicalDataRecords.remove(requestId);
            return true;
        }
        return false;
    }

    private void requestFA(){
        ibSocket.requestFA(FinancialAdvisorDataType.GROUPS);
        ibSocket.requestFA(FinancialAdvisorDataType.PROFILE);
        ibSocket.requestFA(FinancialAdvisorDataType.ACCOUNT_ALIASES);
    }

    @Override
    public void onTickSizeEvent(TickSizeEvent tickSizeEvent) {
        super.onTickSizeEvent(tickSizeEvent);
    }

    @Override
    public void onTickPriceEvent(TickPriceEvent tickPriceEvent) {
        super.onTickPriceEvent(tickPriceEvent);
    }

    @Override
    public void onTickPriceEvent(int requestId, TickType tickType, double price, int autoExecute) {
        super.onTickPriceEvent(requestId, tickType, price, autoExecute);
    }

    @Override
    public void onTickSizeEvent(int requestId, TickType tickType, int size) {
        super.onTickSizeEvent(requestId, tickType, size);
    }

    @Override
    public void onRealTimeBarEvent(RealTimeBarEvent realTimeBarEvent) {
        super.onRealTimeBarEvent(realTimeBarEvent);
    }

    @Override
    public void onRealTimeBarEvent(int requestId, long timestamp, double open, double high, double low, double close, long volume, double weightedAveragePrice, int tradeNumber) {
        super.onRealTimeBarEvent(requestId, timestamp, open, high, low, close, volume, weightedAveragePrice, tradeNumber);
    }

    @Override
    public void onMarketDepthLevelTwoUpdateEvent(MarketDepthLevelTwoUpdateEvent marketDepthLevelTwoUpdateEvent) {
        super.onMarketDepthLevelTwoUpdateEvent(marketDepthLevelTwoUpdateEvent);
    }

    @Override
    public void onMarketDepthLevelTwoUpdateEvent(int requestId, int rowId, String marketMakerName, Operation operation, BookSide bookSide, double price, int size) {
        super.onMarketDepthLevelTwoUpdateEvent(requestId, rowId, marketMakerName, operation, bookSide, price, size);
    }

    @Override
    public void onMarketDepthUpdateEvent(int requestId, int rowId, Operation operation, BookSide bookSide, double price, int size) {
        super.onMarketDepthUpdateEvent(requestId, rowId, operation, bookSide, price, size);
    }

    @Override
    public void onMarketDepthUpdateEvent(MarketDepthUpdateEvent marketDepthUpdateEvent) {
        super.onMarketDepthUpdateEvent(marketDepthUpdateEvent);
    }

    @Override
    public void onHistoricalDataEvent(HistoricalDataEvent historicalDataEvent) {
        super.onHistoricalDataEvent(historicalDataEvent);
    }

    @Override
    public void onHistoricalDataEvent(int requestId, Bar bar) {
        super.onHistoricalDataEvent(requestId, bar);
    }

    @Override
    public void onHistoricalDataEvent(int requestId, String dateTime, double open, double high, double low, double close, int volume, int tradeNumber, double weightedAveragePrice, boolean hasGap) {
        super.onHistoricalDataEvent(requestId, dateTime, open, high, low, close, volume, tradeNumber, weightedAveragePrice, hasGap);
    }

    @Override
    public void onHistoricalDataListEvent(HistoricalDataListEvent historicalDataListEvent) {
        super.onHistoricalDataListEvent(historicalDataListEvent);
    }

    @Override
    public void onHistoricalDataListEvent(int requestId, List<Bar> bars) {
        super.onHistoricalDataListEvent(requestId, bars);
    }

    @Override
    public void onNewOrderRequest(Order order) {
        if (order.getExtOrderId() == -1) {
            order.setExtOrderId(nextOrderId());
        }
        extOrderIdOrderMap.put(order.extOrderId, order);
        orderIdOrderMap.put(order.orderId, order);
        orderIdMap.put(order.orderId, order.extOrderId);
        ibSocket.placeOrder(order);
    }

    @Override
    public void onOrderReplaceRequest(Order order){
        if (order.getExtOrderId() == -1) {
            Order existingOrder = orderIdOrderMap.get(order.orderId);
            order.setExtOrderId(existingOrder.getExtOrderId());
        }
        extOrderIdOrderMap.put(order.extOrderId, order);
        orderIdOrderMap.put(order.orderId, order);
        ibSocket.placeOrder(order);
    }

    @Override
    public void onOrderCancelRequest(Order order){
        if (order.getExtOrderId() == -1) {
            Order existingOrder = orderIdOrderMap.get(order.orderId);
            order.setExtOrderId(existingOrder.getExtOrderId());
        }
        ibSocket.cancelOrder(order.getExtOrderId());
    }
}
