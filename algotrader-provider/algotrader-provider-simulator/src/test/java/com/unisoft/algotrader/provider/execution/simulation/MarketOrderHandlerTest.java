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

import static org.mockito.Mockito.*;

/**
 * Created by alex on 6/6/15.
 */
public class MarketOrderHandlerTest {

    private SimulationExecutor executor;

    private MarketOrderHandler handler;
    private SimulatorConfig config;

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
        handler = new MarketOrderHandler(config, executor);

        order = SampleEventFactory.createOrder(SampleEventFactory.TEST_HKD_INSTRUMENT.getInstId(), Side.Buy, OrdType.Market, 800, 0);

        quote = SampleEventFactory.createQuote(SampleEventFactory.TEST_HKD_INSTRUMENT.getInstId(), 95, 98, 550, 600);
        trade = SampleEventFactory.createTrade(SampleEventFactory.TEST_HKD_INSTRUMENT.getInstId(), 99, 500);
        bar = SampleEventFactory.createBar(SampleEventFactory.TEST_HKD_INSTRUMENT.getInstId(), 87, 92, 60, 88);
    }

    @Test
    public void test_fill_on_quote(){
        handler.process(order, quote);
        verify(executor, times(1)).execute(order, quote.ask, quote.askSize);

        reset(executor);

        order.side = Side.Sell;
        handler.process(order, quote);
        verify(executor, times(1)).execute(order, quote.bid, quote.bidSize);
    }

    @Test
    public void test_fill_on_bar(){

        config.fillOnBarMode = SimulatorConfig.FillOnBarMode.LastBarClose;
        handler.process(order, bar);
        verify(executor, times(1)).execute(order, bar.close, order.ordQty);

        reset(executor);

        config.fillOnBarMode = SimulatorConfig.FillOnBarMode.NextBarOpen;
        handler.process(order, bar);
        verify(executor, times(1)).execute(order, bar.open, order.ordQty);

        reset(executor);

        config.fillOnBarMode = SimulatorConfig.FillOnBarMode.NextBarClose;
        handler.process(order, bar);
        verify(executor, times(1)).execute(order, bar.close, order.ordQty);

        reset(executor);

        order.side = Side.Sell;
        config.fillOnBarMode = SimulatorConfig.FillOnBarMode.NextBarClose;
        handler.process(order, bar);
        verify(executor, times(1)).execute(order, bar.close, order.ordQty);
    }

    @Test
    public void test_fill_on_trade(){
        handler.process(order, trade);
        verify(executor, times(1)).execute(order, trade.price, order.ordQty);

        reset(executor);

        order.side = Side.Sell;
        handler.process(order, trade);
        verify(executor, times(1)).execute(order, trade.price, order.ordQty);
    }

    @Test
    public void test_fill_on_price_qty(){

        handler.process(order, 123, 999);
        verify(executor, times(1)).execute(order, 123, 999);

        reset(executor);

        order.side = Side.Sell;
        handler.process(order, 123, 999);
        verify(executor, times(1)).execute(order, 123, 999);
    }
}
