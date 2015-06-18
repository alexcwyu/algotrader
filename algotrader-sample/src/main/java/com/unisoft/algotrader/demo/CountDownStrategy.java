package com.unisoft.algotrader.demo;

import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.core.OrdStatus;
import com.unisoft.algotrader.core.OrdType;
import com.unisoft.algotrader.core.Side;
import com.unisoft.algotrader.event.data.Bar;
import com.unisoft.algotrader.event.data.MarketDataContainer;
import com.unisoft.algotrader.event.execution.ExecutionReport;
import com.unisoft.algotrader.event.execution.Order;
import com.unisoft.algotrader.order.OrderManager;
import com.unisoft.algotrader.strategy.Strategy;
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
    private int count = 0;

    public CountDownStrategy(String strategyId, CountDownLatch latch, int exp, RingBuffer... providers) {
        super(strategyId, providers);
        this.latch = latch;
        this.exp = exp;
    }

    private boolean ordered = false;


    @Override
    public void onBar(Bar bar) {
        LOG.info("onBar");
        if (!ordered) {
            ordered = true;
            Order order = new Order();
            order.dateTime = bar.dateTime;
            order.orderId = orderId++;
            order.instId = bar.instId;
            //order.limitPrice = bar.close;
            order.strategyId = this.strategyId;
            order.execProviderId = "Simulated";
            order.portfolioId = portfolio.portfolioId;
            order.ordType = OrdType.Market;
            order.side = Side.Buy;
            order.ordQty = 500;
            OrderManager.INSTANCE.onOrder(order);
            LOG.info("CountDownStrategy send order");
        }
    }

    @Override
    public void onMarketDataContainer(MarketDataContainer data) {
        LOG.info("onMarketDataContainer");
        if (!ordered) {
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
            ordered = true;
        }
    }

    public void onExecutionReport(ExecutionReport executionReport) {
        LOG.info("onExecutionReport = {}", executionReport);
        super.onExecutionReport(executionReport);
        if (executionReport.ordStatus == OrdStatus.Filled) {
            this.ordered = false;
        }
        count++;
        if (count == exp) {
            latch.countDown();
        }
    }

}
