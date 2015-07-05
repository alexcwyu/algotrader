package com.unisoft.algotrader.provider.execution.simulation;

import com.unisoft.algotrader.core.OrdType;
import com.unisoft.algotrader.core.Side;
import com.unisoft.algotrader.event.SampleEventFactory;
import com.unisoft.algotrader.event.data.Bar;
import com.unisoft.algotrader.event.data.Quote;
import com.unisoft.algotrader.event.data.Trade;
import com.unisoft.algotrader.event.execution.Order;
import com.unisoft.algotrader.provider.execution.SimulationExecutor;
import com.unisoft.algotrader.provider.execution.SimulatorConfig;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.*;

/**
 * Created by alex on 6/6/15.
 */
public class StopOrderHandlerTest {

    private SimulationExecutor executor;

    private StopOrderHandler handler;
    private SimulatorConfig config;

    private Order noFillOrderBuy;
    private Order noFillOrderSell;
    private Order order;

    private Quote quote;
    private Trade trade;
    private Bar bar;

    @BeforeClass
    public static void init(){
    }

    @Before
    public void setup(){
        executor = mock(SimulationExecutor.class);

        config = new SimulatorConfig();
        handler = new StopOrderHandler(config, executor);

        noFillOrderBuy = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.getInstId(), Side.Buy, OrdType.Stop, 800, 0, 200);
        noFillOrderSell = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.getInstId(), Side.Sell, OrdType.Stop, 1000, 0, 50);

        quote = SampleEventFactory.createQuote(SampleEventFactory.testInstrument.getInstId(), 95, 98, 550, 600);
        trade = SampleEventFactory.createTrade(SampleEventFactory.testInstrument.getInstId(), 99, 500);
        bar = SampleEventFactory.createBar(SampleEventFactory.testInstrument.getInstId(), 87, 92, 60, 88);
    }

    @Test
    public void test_no_fill(){
        handler.process(noFillOrderBuy, quote);
        handler.process(noFillOrderSell, quote);

        handler.process(noFillOrderBuy, trade);
        handler.process(noFillOrderSell, trade);

        handler.process(noFillOrderBuy, bar);
        handler.process(noFillOrderSell, bar);

        handler.process(noFillOrderBuy, 100, 200);
        handler.process(noFillOrderSell, 100, 200);

        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());
    }


    @Test
    public void test_fill_on_quote(){
        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.getInstId(), Side.Buy, OrdType.Stop, 50, 0, 98);
        handler.process(order, quote);
        verify(executor, times(1)).execute(order, quote.ask, quote.askSize);

        reset(executor);

        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.getInstId(), Side.Sell, OrdType.Stop, 100, 0, 95);
        handler.process(order, quote);
        verify(executor, times(1)).execute(order, quote.bid, quote.bidSize);
    }


    @Test
    public void test_fill_on_bar(){
        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.getInstId(), Side.Buy, OrdType.Stop, 50, 0, 92);
        handler.process(order, bar);
        verify(executor, times(1)).execute(order, order.stopPrice, order.ordQty);

        reset(executor);

        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.getInstId(), Side.Sell, OrdType.Stop, 100, 0, 90);
        handler.process(order, bar);
        verify(executor, times(1)).execute(order, order.stopPrice, order.ordQty);
    }


    @Test
    public void test_fill_on_trade(){
        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.getInstId(), Side.Buy, OrdType.Stop, 50, 0, 99);
        handler.process(order, trade);
        verify(executor, times(1)).execute(order, trade.price, order.ordQty);

        reset(executor);


        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.getInstId(), Side.Buy, OrdType.Stop, 50, 0, 98);
        handler.process(order, trade);
        verify(executor, times(1)).execute(order, trade.price, order.ordQty);

        reset(executor);


        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.getInstId(), Side.Sell, OrdType.Stop, 100, 0, 99);
        handler.process(order, trade);
        verify(executor, times(1)).execute(order, trade.price, order.ordQty);
        reset(executor);


        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.getInstId(), Side.Sell, OrdType.Stop, 100, 0, 100);
        handler.process(order, trade);
        verify(executor, times(1)).execute(order, trade.price, order.ordQty);
    }


    @Test
    public void test_fill_on_price_qty(){

        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.getInstId(), Side.Buy, OrdType.Stop, 50, 0, 98);
        handler.process(order, 99, 100);
        verify(executor, times(1)).execute(order, 99, 100);

        reset(executor);

        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.getInstId(), Side.Sell, OrdType.Stop, 100, 0, 95);
        handler.process(order, 94, 100);
        verify(executor, times(1)).execute(order, 94, 100);
    }

}
