package com.unisoft.algotrader.demo;

import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.MarketDataContainer;
import com.unisoft.algotrader.model.event.execution.ExecutionReport;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.trading.OrdStatus;
import com.unisoft.algotrader.model.trading.OrdType;
import com.unisoft.algotrader.model.trading.Side;
import com.unisoft.algotrader.persistence.TradingDataStore;
import com.unisoft.algotrader.provider.ProviderId;
import com.unisoft.algotrader.trading.OrderManager;
import com.unisoft.algotrader.trading.Strategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CountDownLatch;

/**
 * Created by alex on 6/16/15.
 */
class CountDownStrategy extends Strategy {

    private static final Logger LOG = LogManager.getLogger(CountDownStrategy.class);

    private CountDownLatch latch;
    private int exp;
    private int dataCount = 0;
    private int count = 0;
    private final OrderManager orderManager;
    private final int execProviderId;
    public long clOrderId = 0;
    public CountDownStrategy(OrderManager orderManager, int strategyId, int portfolioId, int execProviderId, TradingDataStore tradingDataStore, CountDownLatch latch, int exp) {
        super(strategyId, tradingDataStore, portfolioId);
        this.orderManager = orderManager;
        this.execProviderId = execProviderId;
        this.latch = latch;
        this.exp = exp;
    }

    private boolean ordered = false;


    @Override
    public void onBar(Bar bar) {
        LOG.info("onBar");
        if (!ordered) {
            sendNewOrder(bar.instId, 0);
        }
    }

    private void sendNewOrder(long instId, double price){
        LOG.info("CountDownStrategy send order");
        ordered = true;
        Order order = new Order();
        order.dateTime = System.currentTimeMillis();
        order.clOrderId = clOrderId++;
        order.instId = instId;
        //order.limitPrice = price;
        order.strategyId = this.strategyId;
        order.providerId = execProviderId;
        order.portfolioId = portfolio.portfolioId();
        order.ordType = OrdType.Market;
        order.side = Side.Buy;
        order.ordQty = 500;

        orderManager.onNewOrderRequest(order);
    }

    public void onExecutionReport(ExecutionReport executionReport) {
        LOG.info("onExecutionReport = {}", executionReport);
        super.onExecutionReport(executionReport);
        if (executionReport.ordStatus == OrdStatus.Filled) {
            this.ordered = false;
        }
        count++;
//        if (count == exp) {
//            latch.countDown();
//        }
    }

}
