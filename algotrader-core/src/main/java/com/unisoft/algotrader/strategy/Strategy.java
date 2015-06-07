package com.unisoft.algotrader.strategy;

import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.event.Event;
import com.unisoft.algotrader.event.EventBusManager;
import com.unisoft.algotrader.event.data.*;
import com.unisoft.algotrader.event.execution.*;
import com.unisoft.algotrader.core.Side;
import com.unisoft.algotrader.order.OrderManager;
import com.unisoft.algotrader.core.Portfolio;
import com.unisoft.algotrader.threading.AbstractEventProcessor;
import com.unisoft.algotrader.threading.YieldMultiBufferWaitStrategy;

/**
 * Created by alex on 5/17/15.
 */

public abstract class Strategy extends AbstractEventProcessor implements MarketDataHandler, OrderHandler, ExecutionHandler {

    public int orderId = 0;
    public String strategyId;
    public Portfolio portfolio;

    public Strategy(String strategyId, Portfolio portfolio){
        this(strategyId, portfolio, EventBusManager.INSTANCE.executionReportRB, EventBusManager.INSTANCE.orderStatusRB, EventBusManager.INSTANCE.marketDataRB);
    }

    public Strategy(String strategyId, Portfolio portfolio, RingBuffer... providers){
        super(new YieldMultiBufferWaitStrategy(),  null, providers);
        this.strategyId = strategyId;
        this.portfolio =portfolio;
        StrategyManager.INSTANCE.register(this);
    }

    @Override
    public void onEvent(Event event) {
        event.on(this);
    }

    @Override
    public void onMarketDataContainer(MarketDataContainer data){
        //portfolio.onMarketDataContainer(data);

        System.out.println("Strategy="+data);
        Order order = new Order();
        order.dateTime = data.dateTime;
        order.orderId = orderId++;
        order.instId = data.instId;
        order.limitPrice = data.bar.close;
        order.strategyId = this.strategyId;
        order.execProviderId = "Simulated";
        order.portfolioId = portfolio.portfolioId;
        order.side = Side.Buy;
        order.ordQty = 500;
        OrderManager.INSTANCE.onOrder(order);
    }

    @Override
    public void onBar(Bar bar) {
        //portfolio.onBar(bar);
    }

    @Override
    public void onQuote(Quote quote) {
        //portfolio.onQuote(quote);
    }

    @Override
    public void onTrade(Trade trade) {

        //portfolio.onTrade(trade);
    }

    @Override
    public void onExecutionReport(ExecutionReport executionReport) {

        //portfolio.onExecutionReport(executionReport);
        //System.out.println("Strategy, onExecutionReport="+executionReport);

    }

    @Override
    public void onOrder(Order order) {

        //portfolio.onOrder(order);
        System.out.println("Strategy, onOrder="+order);
    }


    @Override
    public void onOrderCancelReject(OrderCancelReject orderCancelReject) {

    }

    @Override
    public void onExecutionEventContainer(ExecutionEventContainer executionEventContainer) {

    }
}
