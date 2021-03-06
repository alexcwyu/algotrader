package com.unisoft.algotrader.trading;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.event.bus.EventBusManager;
import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.MarketDataHandler;
import com.unisoft.algotrader.model.event.data.Quote;
import com.unisoft.algotrader.model.event.data.Trade;
import com.unisoft.algotrader.model.event.execution.*;
import com.unisoft.algotrader.model.trading.OrdType;
import com.unisoft.algotrader.model.trading.Portfolio;
import com.unisoft.algotrader.model.trading.Side;
import com.unisoft.algotrader.model.trading.TimeInForce;
import com.unisoft.algotrader.persistence.TradingDataStore;
import com.unisoft.algotrader.provider.ProviderId;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.provider.data.RealTimeDataProvider;
import com.unisoft.algotrader.provider.data.SubscriptionKey;
import com.unisoft.algotrader.provider.data.SubscriptionType;
import com.unisoft.algotrader.utils.threading.disruptor.MultiEventProcessor;
import com.unisoft.algotrader.utils.threading.disruptor.waitstrategy.NoWaitStrategy;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by alex on 5/17/15.
 */

public abstract class Strategy extends MultiEventProcessor implements MarketDataHandler, ExecutionEventHandler {

    protected final int strategyId;
    protected final TradingDataStore tradingDataStore;
    protected final EventBusManager eventBusManager;
    //TODO
    protected ProviderManager providerManager;
    protected Portfolio portfolio;
    protected String accountId;
    protected AtomicLong clOrderId;
    protected ProviderId execProviderId;
    protected ProviderId dataProviderId;
    protected StrategyContext strategyContext;

    protected Set<Long> instIdSet;

    @Inject
    protected ExecutorService executorService;

    protected OrderTable orderTable = new OrderTable();
    protected Set<SubscriptionKey> subscriptionKeys = Sets.newHashSet();

    public Strategy(int strategyId, TradingDataStore tradingDataStore, EventBusManager eventBusManager){
        super(new NoWaitStrategy(),  eventBusManager.getExecutionEventRB(), eventBusManager.getMarketDataRB());
        this.strategyId = strategyId;
        this.tradingDataStore = tradingDataStore;
        this.eventBusManager = null;
    }

    public Strategy(int strategyId, TradingDataStore tradingDataStore, EventBusManager eventBusManager, RingBuffer... providers){
        super(new NoWaitStrategy(),  null, providers);
        this.strategyId = strategyId;
        this.tradingDataStore = tradingDataStore;
        this.eventBusManager = eventBusManager;
    }

    public Strategy(int strategyId, TradingDataStore tradingDataStore, int portfolioId, EventBusManager eventBusManager){
        super(new NoWaitStrategy(),  eventBusManager.getExecutionEventRB(), eventBusManager.getMarketDataRB());
        this.strategyId = strategyId;
        this.tradingDataStore = tradingDataStore;
        this.portfolio = tradingDataStore.getPortfolio(portfolioId);
        this.eventBusManager = eventBusManager;
    }

    public Strategy(int strategyId, TradingDataStore tradingDataStore, int portfolioId, RingBuffer... providers){
        super(new NoWaitStrategy(),  null, providers);
        this.strategyId = strategyId;
        this.tradingDataStore = tradingDataStore;
        this.portfolio = tradingDataStore.getPortfolio(portfolioId);
        this.eventBusManager = null;
    }


    /**
     * @param strategyContext
     * initialize
     */
    public void init(StrategyContext strategyContext){
        this.strategyContext = strategyContext;
        this.portfolio = tradingDataStore.getPortfolio(strategyContext.portfolioId);
        this.accountId = portfolio.accountId();
        this.clOrderId = new AtomicLong(strategyContext.lastOrderId);
        this.execProviderId = strategyContext.execProviderId;
        this.dataProviderId = strategyContext.dataProviderId;
        this.instIdSet = strategyContext.instIdSet;
    }


    private void subscribeData(){
        for (SubscriptionType type : strategyContext.marketDataSet){
            for (long instId : strategyContext.instIdSet){
                subscriptionKeys.add(SubscriptionKey.createSubscriptionKey(dataProviderId.id, type, instId));
            }
        }

        RealTimeDataProvider dataProvider = providerManager.getRealTimeDataProvider(dataProviderId.id);

        for (SubscriptionKey subscriptionKey: subscriptionKeys){
            dataProvider.subscribeMarketData(subscriptionKey);
        }
    }

    /**
     * start strategy, subscribe data
     */
    public void start(){
        executorService.submit(this);
        subscribeData();
    }


    @Override
    public void onEvent(Event event) {
        event.on(this);
    }

    @Override
    public void onBar(Bar bar) {
    }

    @Override
    public void onQuote(Quote quote) {
    }

    @Override
    public void onTrade(Trade trade) {
    }

    @Override
    public void onExecutionReport(ExecutionReport executionReport) {
    }

    @Override
    public void onOrderCancelReject(OrderCancelReject orderCancelReject) {
    }

    @Override
    public void onExecutionEventContainer(ExecutionEventContainer executionEventContainer) {
    }

    public void setPortfolio(Portfolio portfolio){
        this.portfolio = portfolio;
    }

    protected long nextOrdId(){
        return clOrderId.incrementAndGet();
    }

    protected void newLimitOrder(long instId, Side side, double price, double qty, TimeInForce tif){
        Order order = new Order();
        order.clOrderId = nextOrdId();
        order.instId = instId;
        order.strategyId = strategyId;
        order.providerId = execProviderId.id;
        order.accountId = accountId;
        order.dateTime = System.currentTimeMillis();
        order.side= side;
        order.ordType = OrdType.Limit;
        order.ordQty=qty;
        order.limitPrice = price;
        order.tif = tif;

        orderTable.addOrUpdateOrder(order);
        eventBusManager.getOrderEventBus().placeOrder(order);
    }

    protected void newMarketOrder(long instId, Side side, double qty, TimeInForce tif){
        Order order = new Order();
        order.clOrderId = nextOrdId();
        order.instId = instId;
        order.strategyId = strategyId;
        order.providerId = execProviderId.id;
        order.accountId = accountId;
        order.dateTime = System.currentTimeMillis();
        order.side= side;
        order.ordType = OrdType.Market;
        order.ordQty=qty;
        order.tif = tif;

        orderTable.addOrUpdateOrder(order);
        eventBusManager.getOrderEventBus().placeOrder(order);

    }
}
