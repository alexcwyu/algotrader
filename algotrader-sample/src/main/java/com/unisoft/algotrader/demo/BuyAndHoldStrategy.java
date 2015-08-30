package com.unisoft.algotrader.demo;

import com.google.common.collect.Lists;
import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.config.AppConfig;
import com.unisoft.algotrader.event.SampleEventFactory;
import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.MarketDataContainer;
import com.unisoft.algotrader.model.event.data.Quote;
import com.unisoft.algotrader.model.event.data.Trade;
import com.unisoft.algotrader.model.event.execution.ExecutionReport;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.trading.OrdType;
import com.unisoft.algotrader.model.trading.Portfolio;
import com.unisoft.algotrader.model.trading.Side;
import com.unisoft.algotrader.persistence.TradingDataStore;
import com.unisoft.algotrader.trading.OrderManager;
import com.unisoft.algotrader.trading.Strategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by alex on 6/7/15.
 */
public class BuyAndHoldStrategy extends Strategy {

    private static final Logger LOG = LogManager.getLogger(BuyAndHoldStrategy.class);

    private final List<Order> orders = Lists.newArrayList();
    private final List<ExecutionReport> executionReports = Lists.newArrayList();
    private final OrderManager orderManager;

    public Order longOrder;

    public BuyAndHoldStrategy(AppConfig config, Portfolio portfolio){
        this(config.getOrderManager(), config.getTradingDataStore(), portfolio.getPortfolioId(), config.getEventBusManager().marketDataRB);
    }

    public BuyAndHoldStrategy(OrderManager orderManager, TradingDataStore tradingDataStore, String portfolioId, RingBuffer<MarketDataContainer> rb){
        super("BuyAndHoldStrategy", tradingDataStore, portfolioId, rb);
        this.orderManager = orderManager;
    }


    @Override
    public void onBar(Bar bar) {
        LOG.info("onBar {}", bar);
        createAndSendMarketOrder(bar.instId);
    }

    @Override
    public void onQuote(Quote quote) {
        LOG.info("onQuote {}", quote);
        createAndSendMarketOrder(quote.instId);
    }

    @Override
    public void onTrade(Trade trade) {
        LOG.info("onTrade {}", trade);
        createAndSendMarketOrder(trade.instId);
    }

    private void createAndSendMarketOrder(long instId){
        if (longOrder == null){
            longOrder = SampleEventFactory.createOrder(instId, Side.Buy, OrdType.Market, 9000, 0.0);
            sendOrder(longOrder);
        }
    }


    @Override
    public void onExecutionReport(ExecutionReport executionReport) {
        LOG.info("onExecutionReport {}", executionReport);
        executionReports.add(executionReport);
    }

    @Override
    public void onNewOrderRequest(Order order) {
        LOG.info("onNewOrderRequest {}", order);
        orders.add(order);
    }

    public void sendOrder(Order order){
        order.portfolioId = portfolio.getPortfolioId();
        order.strategyId = strategyId;
        orderManager.onNewOrderRequest(order);
    }

    public List<Order> getOrders(){
        return orders;
    }

    public List<ExecutionReport> getExecutionReports(){
        return executionReports;
    }

}
