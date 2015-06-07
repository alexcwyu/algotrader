package com.unisoft.algotrader;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.unisoft.algotrader.core.*;
import com.unisoft.algotrader.event.EventBusManager;
import com.unisoft.algotrader.event.data.MarketDataContainer;
import com.unisoft.algotrader.event.execution.ExecutionReport;
import com.unisoft.algotrader.event.execution.Order;
import com.unisoft.algotrader.order.OrderManager;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.provider.data.BarFactory;
import com.unisoft.algotrader.provider.data.historical.DummyDataProvider;
import com.unisoft.algotrader.provider.execution.SimulationExecutor;
import com.unisoft.algotrader.strategy.Strategy;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by alex on 5/18/15.
 */
public class Demo {

    static class CountDownStrategy extends Strategy{
        private CountDownLatch latch;
        private int exp;
        private int count = 0;
        public CountDownStrategy(String strategyId, Portfolio portfolio, CountDownLatch latch, int exp, RingBuffer... providers){
            super(strategyId, portfolio, providers);
            this.latch = latch;
            this.exp = exp;
        }

        private boolean ordered = false;

        @Override
        public void onMarketDataContainer(MarketDataContainer data){
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
            super.onExecutionReport(executionReport);
            if (executionReport.ordStatus == OrdStatus.Filled){
                this.ordered = false;
            }
            count ++;
            if (count==exp){
                latch.countDown();
            }
        }

    }

    public static void main(String [] args) throws Exception{
        DummyDataProvider provider = new DummyDataProvider(10);

        BarFactory barFactory = new BarFactory(EventBusManager.INSTANCE.rawMarketDataRB, EventBusManager.INSTANCE.marketDataRB);

        //InstrumentDataManager instrumentDataManager = InstrumentDataManager.INSTANCE;

        Account account = new Account("","", Currency.HKD, 100000);
        Portfolio portfolio = new Portfolio("PID1");
        CountDownLatch latch = new CountDownLatch(1);
        Strategy strategy = new CountDownStrategy("Sid", portfolio, latch, 10);

        SimulationExecutor simulationExecutor = new SimulationExecutor(OrderManager.INSTANCE, EventBusManager.INSTANCE.marketDataRB);
        ProviderManager.INSTANCE.registerExecutionProvider(simulationExecutor);

        ExecutorService executor = Executors.newFixedThreadPool(8, DaemonThreadFactory.INSTANCE);

        executor.submit(barFactory);
        executor.submit(simulationExecutor);
        //executor.submit(instrumentDataManager);
        //executor.submit(strategy);


        provider.subscribe("HSI", 20110101, 20141231);

        latch.await();

        System.out.println("done");
    }
}
