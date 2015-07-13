package com.unisoft.algotrader.provider.execution.simulation;

import com.unisoft.algotrader.event.SampleEventFactory;
import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.Quote;
import com.unisoft.algotrader.model.event.data.Trade;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.trading.OrdType;
import com.unisoft.algotrader.model.trading.Side;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.*;

/**
 * Created by alex on 6/6/15.
 */
public class TrailingStopOrderHandlerTest {

    private SimulationExecutor executor;

    private TrailingStopOrderHandler handler;
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
        handler = new TrailingStopOrderHandler(config, executor);

        noFillOrderBuy = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.getInstId(), Side.Buy, OrdType.TrailingStop, 800, 0, 20);
        noFillOrderSell = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.getInstId(), Side.Sell, OrdType.TrailingStop, 1000, 0, 20);

        quote = SampleEventFactory.createQuote(SampleEventFactory.testInstrument.getInstId(), 95, 98, 550, 600);
        trade = SampleEventFactory.createTrade(SampleEventFactory.testInstrument.getInstId(), 99, 500);
        bar = SampleEventFactory.createBar(SampleEventFactory.testInstrument.getInstId(), 87, 92, 80, 88);
    }

    @Test
    public void test_no_fill(){
        handler.process(noFillOrderBuy, quote);
        handler.process(noFillOrderSell, quote);

        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());

        handler.process(noFillOrderBuy, trade);
        handler.process(noFillOrderSell, trade);

        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());

        handler.process(noFillOrderBuy, bar);
        handler.process(noFillOrderSell, bar);

        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());

        handler.process(noFillOrderBuy, 90, 200);
        handler.process(noFillOrderSell, 100, 200);

        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());
    }


    @Test
    public void test_fill_on_quote(){
        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.getInstId(), Side.Buy, OrdType.TrailingStop, 50, 0, 10);
        handler.process(order, quote);
        assertEquals(quote.ask+10, order.trailingStopExecPrice, 0.0);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());

        quote = SampleEventFactory.createQuote(SampleEventFactory.testInstrument.getInstId(),105, 108, 550, 600);
        handler.process(order, quote);
        verify(executor, times(1)).execute(order, quote.ask, quote.askSize);

        reset(executor);

        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.getInstId(), Side.Sell, OrdType.TrailingStop, 100, 0, 10);
        quote = SampleEventFactory.createQuote(SampleEventFactory.testInstrument.getInstId(), 105, 108, 550, 600);
        handler.process(order, quote);
        assertEquals(quote.bid - 10, order.trailingStopExecPrice, 0.0);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());

        quote = SampleEventFactory.createQuote(SampleEventFactory.testInstrument.getInstId(), 85, 88, 550, 600);
        handler.process(order, quote);
        verify(executor, times(1)).execute(order, quote.bid, quote.bidSize);
    }


    @Test
    public void test_fill_on_bar(){
        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.getInstId(), Side.Buy, OrdType.TrailingStop, 50, 0, 10);
        handler.process(order, bar);
        verify(executor, times(1)).execute(order, order.trailingStopExecPrice, order.ordQty);

        reset(executor);

        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.getInstId(), Side.Sell, OrdType.TrailingStop, 100, 0, 10);
        handler.process(order, bar);
        verify(executor, times(1)).execute(order, order.trailingStopExecPrice, order.ordQty);
    }


    @Test
    public void test_fill_on_trade(){
        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.getInstId(), Side.Buy, OrdType.TrailingStop, 50, 0, 10);
        handler.process(order, trade);
        assertEquals(109, order.trailingStopExecPrice, 0.0);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());
        trade = SampleEventFactory.createTrade(SampleEventFactory.testInstrument.getInstId(), 109, 80);
        handler.process(order, trade);
        verify(executor, times(1)).execute(order, trade.price, order.ordQty);

        reset(executor);

        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.getInstId(), Side.Sell, OrdType.TrailingStop, 100, 0, 10);
        trade = SampleEventFactory.createTrade(SampleEventFactory.testInstrument.getInstId(), 88, 80);
        handler.process(order, trade);
        assertEquals(78, order.trailingStopExecPrice, 0.0);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());
        trade = SampleEventFactory.createTrade(SampleEventFactory.testInstrument.getInstId(), 68, 80);
        handler.process(order, trade);
        verify(executor, times(1)).execute(order, trade.price, order.ordQty);

    }


    @Test
    public void test_fill_on_price_qty(){

        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.getInstId(), Side.Buy, OrdType.TrailingStop, 50, 0, 10);
        handler.process(order, 90, 100);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());
        handler.process(order, 100, 100);
        verify(executor, times(1)).execute(order, 100, 100);

        reset(executor);

        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.getInstId(), Side.Sell, OrdType.TrailingStop, 100, 0, 10);
        handler.process(order, 90, 100);
        verify(executor, times(0)).execute(any(), anyDouble(), anyDouble());
        handler.process(order, 80, 100);
        verify(executor, times(1)).execute(order, 80, 100);
    }

    @Test
    public void test_change_trailing_stop(){
        //test 1 BUY ORDER
        //buy order trailingstop should be changed when price go down
        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.getInstId(), Side.Buy, OrdType.TrailingStop, 50, 0, 10);
        trade = SampleEventFactory.createTrade(SampleEventFactory.testInstrument.getInstId(), 99, 500);
        handler.process(order, trade);
        //trade.price + order.stopPrice = 99 + 10 = 109
        assertEquals(109, order.trailingStopExecPrice, 0.0);
        //price move up, trailing close remaining unchanged
        trade = SampleEventFactory.createTrade(SampleEventFactory.testInstrument.getInstId(), 88, 80);
        handler.process(order, trade);
        assertEquals(98, order.trailingStopExecPrice, 0.0);



        //test 2 SELL ORDER
        //order trailingstop should be changed when price go up
        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.getInstId(), Side.Sell, OrdType.TrailingStop, 50, 0, 10);
        trade = SampleEventFactory.createTrade(SampleEventFactory.testInstrument.getInstId(), 99, 500);
        handler.process(order, trade);
        //trade.price + order.stopPrice = 99 - 10 = 89
        assertEquals(89, order.trailingStopExecPrice, 0.0);
        //price move up, trailing close remaining unchanged
        trade = SampleEventFactory.createTrade(SampleEventFactory.testInstrument.getInstId(), 108, 80);
        handler.process(order, trade);
        assertEquals(98, order.trailingStopExecPrice, 0.0);
    }

    @Test
    public void test_no_change_trailing_stop(){
        //test 1 BUY ORDER
        //buy order trailingstop should not be changed when price go up
        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.getInstId(), Side.Buy, OrdType.TrailingStop, 50, 0, 10);
        trade = SampleEventFactory.createTrade(SampleEventFactory.testInstrument.getInstId(), 99, 500);
        handler.process(order, trade);
        //trade.price + order.stopPrice = 99 + 10 = 109
        assertEquals(109, order.trailingStopExecPrice, 0.0);
        //price move up, trailing close remaining unchanged
        trade = SampleEventFactory.createTrade(SampleEventFactory.testInstrument.getInstId(), 108, 80);
        handler.process(order, trade);
        assertEquals(109, order.trailingStopExecPrice, 0.0);



        //test 2 SELL ORDER
        //order trailingstop should be not changed when price go down
        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.getInstId(), Side.Sell, OrdType.TrailingStop, 50, 0, 10);
        trade = SampleEventFactory.createTrade(SampleEventFactory.testInstrument.getInstId(), 99, 500);
        handler.process(order, trade);
        //trade.price + order.stopPrice = 99 - 10 = 89
        assertEquals(89, order.trailingStopExecPrice, 0.0);
        //price move up, trailing close remaining unchanged
        trade = SampleEventFactory.createTrade(SampleEventFactory.testInstrument.getInstId(), 88, 80);
        handler.process(order, trade);
        assertEquals(89, order.trailingStopExecPrice, 0.0);

    }
}
