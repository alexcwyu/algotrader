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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.*;

/**
 * Created by alex on 6/6/15.
 */
public class StopLimitOrderHandlerTest {

    private SimulationExecutor executor;

    private StopLimitOrderHandler handler;
    private SimulatorConfig config;

    private Order noFillOrderBuy1;
    private Order noFillOrderBuy2;
    private Order noFillOrderSell1;
    private Order noFillOrderSell2;
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
        handler = new StopLimitOrderHandler(config, executor);

        noFillOrderBuy1 = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.instId, Side.Buy, OrdType.StopLimit, 800, 50, 200);
        noFillOrderBuy2 = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.instId, Side.Buy, OrdType.StopLimit, 800, 50, 50);
        noFillOrderSell1 = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.instId, Side.Sell, OrdType.StopLimit, 1000, 200, 50);
        noFillOrderSell2 = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.instId, Side.Sell, OrdType.StopLimit, 1000, 200, 200);

        quote = SampleEventFactory.createQuote(SampleEventFactory.testInstrument.instId, 95, 98, 550, 600);
        trade = SampleEventFactory.createTrade(SampleEventFactory.testInstrument.instId, 99, 500);
        bar = SampleEventFactory.createBar(SampleEventFactory.testInstrument.instId, 92, 60, 87, 88);
    }

    @Test
    public void test_no_fill_quote() {
        handler.process(noFillOrderBuy1, quote);
        assertFalse(noFillOrderBuy1.stopLimitReady);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());

        handler.process(noFillOrderBuy2, quote);
        assertTrue(noFillOrderBuy2.stopLimitReady);
        handler.process(noFillOrderBuy2, quote);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());

        handler.process(noFillOrderSell1, quote);
        assertFalse(noFillOrderSell1.stopLimitReady);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());

        handler.process(noFillOrderSell2, quote);
        assertTrue(noFillOrderSell2.stopLimitReady);
        handler.process(noFillOrderSell2, quote);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());

    }

    @Test
    public void test_no_fill_trade() {
        handler.process(noFillOrderBuy1, trade);
        assertFalse(noFillOrderBuy1.stopLimitReady);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());

        handler.process(noFillOrderBuy2, trade);
        assertTrue(noFillOrderBuy2.stopLimitReady);
        handler.process(noFillOrderBuy2, trade);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());

        handler.process(noFillOrderSell1, trade);
        assertFalse(noFillOrderSell1.stopLimitReady);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());

        handler.process(noFillOrderSell2, trade);
        assertTrue(noFillOrderSell2.stopLimitReady);
        handler.process(noFillOrderSell2, trade);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());
    }

    @Test
    public void test_no_fill_bar() {
        handler.process(noFillOrderBuy1, bar);
        assertFalse(noFillOrderBuy1.stopLimitReady);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());

        handler.process(noFillOrderBuy2, bar);
        assertTrue(noFillOrderBuy2.stopLimitReady);
        handler.process(noFillOrderBuy2, bar);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());

        handler.process(noFillOrderSell1, bar);
        assertFalse(noFillOrderSell1.stopLimitReady);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());

        handler.process(noFillOrderSell2, bar);
        assertTrue(noFillOrderSell2.stopLimitReady);
        handler.process(noFillOrderSell2, bar);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());
    }

    @Test
    public void test_no_fill_price(){
        handler.process(noFillOrderBuy1, 100, 200);
        assertFalse(noFillOrderBuy1.stopLimitReady);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());

        handler.process(noFillOrderBuy2, 100, 200);
        assertTrue(noFillOrderBuy2.stopLimitReady);
        handler.process(noFillOrderBuy2, 100, 200);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());

        handler.process(noFillOrderSell1, 100, 200);
        assertFalse(noFillOrderSell1.stopLimitReady);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());

        handler.process(noFillOrderSell2, 100, 200);
        assertTrue(noFillOrderSell2.stopLimitReady);
        handler.process(noFillOrderSell2, 100, 200);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());
    }


    @Test
    public void test_fill_on_quote(){
        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.instId, Side.Buy, OrdType.StopLimit, 50, 100, 98);
        handler.process(order, quote);
        assertTrue(order.stopLimitReady);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());
        handler.process(order, quote);
        verify(executor, times(1)).execute(order, quote.ask, quote.askSize);

        reset(executor);

        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.instId, Side.Sell, OrdType.StopLimit, 100, 90, 95);
        handler.process(order, quote);
        assertTrue(order.stopLimitReady);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());
        handler.process(order, quote);
        verify(executor, times(1)).execute(order, quote.bid, quote.bidSize);
    }


    @Test
    public void test_fill_on_bar(){
        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.instId, Side.Buy, OrdType.StopLimit, 50, 60, 92);
        handler.process(order, bar);
        assertTrue(order.stopLimitReady);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());
        handler.process(order, bar);
        verify(executor, times(1)).execute(order, order.limitPrice, order.ordQty);

        reset(executor);

        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.instId, Side.Sell, OrdType.StopLimit, 100, 92, 60);
        handler.process(order, bar);
        assertTrue(order.stopLimitReady);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());
        handler.process(order, bar);
        verify(executor, times(1)).execute(order, order.limitPrice, order.ordQty);
    }


    @Test
    public void test_fill_on_trade(){
        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.instId, Side.Buy, OrdType.StopLimit, 50, 99, 99);
        handler.process(order, trade);
        assertTrue(order.stopLimitReady);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());
        handler.process(order, trade);
        verify(executor, times(1)).execute(order, trade.price, order.ordQty);

        reset(executor);

        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.instId, Side.Sell, OrdType.StopLimit, 100, 99, 99);
        handler.process(order, trade);
        assertTrue(order.stopLimitReady);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());
        handler.process(order, trade);
        verify(executor, times(1)).execute(order, trade.price, order.ordQty);
    }


    @Test
    public void test_fill_on_price_qty(){

        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.instId, Side.Buy, OrdType.StopLimit, 50, 90, 98);
        handler.process(order, 98, 100);
        assertTrue(order.stopLimitReady);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());
        handler.process(order, 90, 100);
        verify(executor, times(1)).execute(order, 90, 100);

        reset(executor);

        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.instId, Side.Sell, OrdType.StopLimit, 100, 100, 95);
        handler.process(order, 95, 100);
        assertTrue(order.stopLimitReady);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());
        handler.process(order, 100, 100);
        verify(executor, times(1)).execute(order, 100, 100);
    }
}
