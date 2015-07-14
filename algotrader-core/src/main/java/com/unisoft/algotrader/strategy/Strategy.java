package com.unisoft.algotrader.strategy;

import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.event.EventBusManager;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.MarketDataHandler;
import com.unisoft.algotrader.model.event.data.Quote;
import com.unisoft.algotrader.model.event.data.Trade;
import com.unisoft.algotrader.model.event.execution.*;
import com.unisoft.algotrader.model.trading.Portfolio;
import com.unisoft.algotrader.persistence.TradingDataStore;
import com.unisoft.algotrader.utils.threading.disruptor.MultiEventProcessor;
import com.unisoft.algotrader.utils.threading.disruptor.waitstrategy.NoWaitStrategy;

/**
 * Created by alex on 5/17/15.
 */

public abstract class Strategy extends MultiEventProcessor implements MarketDataHandler, OrderHandler, ExecutionHandler {

    public int orderId = 0;
    protected final String strategyId;
    protected Portfolio portfolio;
    protected final TradingDataStore tradingDataStore;

    public Strategy(String strategyId, TradingDataStore tradingDataStore){
        this(strategyId, tradingDataStore, EventBusManager.INSTANCE.executionReportRB, EventBusManager.INSTANCE.orderStatusRB, EventBusManager.INSTANCE.marketDataRB);
    }

    public Strategy(String strategyId, TradingDataStore tradingDataStore, RingBuffer... providers){
        super(new NoWaitStrategy(),  null, providers);
        this.strategyId = strategyId;
        this.tradingDataStore = tradingDataStore;
        StrategyManager.INSTANCE.register(this);
    }

    public Strategy(String strategyId, TradingDataStore tradingDataStore, String portfolioId){
        this(strategyId, tradingDataStore, portfolioId, EventBusManager.INSTANCE.executionReportRB, EventBusManager.INSTANCE.orderStatusRB, EventBusManager.INSTANCE.marketDataRB);
    }

    public Strategy(String strategyId, TradingDataStore tradingDataStore, String portfolioId, RingBuffer... providers){
        super(new NoWaitStrategy(),  null, providers);
        this.strategyId = strategyId;
        this.tradingDataStore = tradingDataStore;
        this.portfolio = tradingDataStore.getPortfolio(portfolioId);
        StrategyManager.INSTANCE.register(this);
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
    public void onOrder(Order order) {
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
