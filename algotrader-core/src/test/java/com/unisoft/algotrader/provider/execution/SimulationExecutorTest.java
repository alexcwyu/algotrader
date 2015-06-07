package com.unisoft.algotrader.provider.execution;

import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.core.OrdType;
import com.unisoft.algotrader.core.Side;
import com.unisoft.algotrader.event.data.Bar;
import com.unisoft.algotrader.event.data.MarketDataContainer;
import com.unisoft.algotrader.event.data.Quote;
import com.unisoft.algotrader.event.data.Trade;
import com.unisoft.algotrader.event.execution.ExecutionReport;
import com.unisoft.algotrader.event.execution.Order;
import com.unisoft.algotrader.order.OrderManager;
import com.unisoft.algotrader.threading.NoWaitStrategy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by alex on 6/2/15.
 */
public class SimulationExecutorTest {

    private SimulationExecutor simulationExecutor;
    private OrderManager orderManager;
    RingBuffer rb =RingBuffer.createSingleProducer(MarketDataContainer.FACTORY, 1024, new NoWaitStrategy());

    private Order order;
    private Quote quote;
    private Trade trade;
    private Bar bar;
    @Before
    public void setup(){
        orderManager = spy(new OrderManager());

        simulationExecutor = new SimulationExecutor(orderManager, rb);
        simulationExecutor.config.fillOnBar = true;
        simulationExecutor.config.fillOnQuote = true;
        simulationExecutor.config.fillOnTrade = true;
        simulationExecutor.config.fillOnBarMode = SimulatorConfig.FillOnBarMode.LastBarClose;

        order = new Order();
        order.side=Side.Buy;
        order.ordQty=98000.0;

        quote = new Quote();
        quote.ask=180.0;
        quote.askSize=1100;
        quote.bid=175.0;
        quote.bidSize=1900;

        trade = new Trade();
        trade.size = 10000;
        trade.price = 178.0;

        bar = new Bar();
        bar.open = 168;
        bar.high = 188;
        bar.low = 138;
        bar.close = 178;
    }



    @Test
    public void test_market_order_immediate_fill(){
        order = new Order();
        order.orderId = 999;
        order.instId = "HSI";
        order.execProviderId = simulationExecutor.providerId();
        order.side=Side.Buy;
        order.ordQty=98000.0;
        order.ordType = OrdType.Market;

        orderManager.onOrder(order);

//        ArgumentCaptor<ExecutionReport> argument1 = ArgumentCaptor.forClass(ExecutionReport.class);
//        verify(orderManager).onExecutionReport(argument1.capture());
//
//        ArgumentCaptor<ExecutionReport> argument2= ArgumentCaptor.forClass(ExecutionReport.class);
//        verify(orderManager).onExecutionReport(argument2.capture());

    }
}
