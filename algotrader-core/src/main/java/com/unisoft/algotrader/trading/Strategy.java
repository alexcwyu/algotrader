package com.unisoft.algotrader.trading;

import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.event.bus.EventBusManager;
import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.MarketDataHandler;
import com.unisoft.algotrader.model.event.data.Quote;
import com.unisoft.algotrader.model.event.data.Trade;
import com.unisoft.algotrader.model.event.execution.ExecutionEventContainer;
import com.unisoft.algotrader.model.event.execution.ExecutionEventHandler;
import com.unisoft.algotrader.model.event.execution.ExecutionReport;
import com.unisoft.algotrader.model.event.execution.OrderCancelReject;
import com.unisoft.algotrader.model.trading.Portfolio;
import com.unisoft.algotrader.persistence.TradingDataStore;
import com.unisoft.algotrader.utils.threading.disruptor.MultiEventProcessor;
import com.unisoft.algotrader.utils.threading.disruptor.waitstrategy.NoWaitStrategy;

/**
 * Created by alex on 5/17/15.
 */

public abstract class Strategy extends MultiEventProcessor implements MarketDataHandler, ExecutionEventHandler {

    protected final String strategyId;
    protected Portfolio portfolio;
    protected final TradingDataStore tradingDataStore;
    protected final EventBusManager eventBusManager;

    public Strategy(String strategyId, TradingDataStore tradingDataStore, EventBusManager eventBusManager){
        super(new NoWaitStrategy(),  null, eventBusManager.getExecutionEventRB(), eventBusManager.getMarketDataRB());
        this.strategyId = strategyId;
        this.tradingDataStore = tradingDataStore;
        this.eventBusManager = null;
    }

    public Strategy(String strategyId, TradingDataStore tradingDataStore, EventBusManager eventBusManager, RingBuffer... providers){
        super(new NoWaitStrategy(),  null, providers);
        this.strategyId = strategyId;
        this.tradingDataStore = tradingDataStore;
        this.eventBusManager = eventBusManager;
    }

    public Strategy(String strategyId, TradingDataStore tradingDataStore, String portfolioId, EventBusManager eventBusManager){
        super(new NoWaitStrategy(),  null, eventBusManager.getExecutionEventRB(), eventBusManager.getMarketDataRB());
        this.strategyId = strategyId;
        this.tradingDataStore = tradingDataStore;
        this.portfolio = tradingDataStore.getPortfolio(portfolioId);
        this.eventBusManager = eventBusManager;
    }

    public Strategy(String strategyId, TradingDataStore tradingDataStore, String portfolioId, RingBuffer... providers){
        super(new NoWaitStrategy(),  null, providers);
        this.strategyId = strategyId;
        this.tradingDataStore = tradingDataStore;
        this.portfolio = tradingDataStore.getPortfolio(portfolioId);
        this.eventBusManager = null;
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
}
