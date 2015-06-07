package com.unisoft.algotrader.provider.execution.simulation;

import com.unisoft.algotrader.core.OrdType;
import com.unisoft.algotrader.core.Side;
import com.unisoft.algotrader.event.SampleEventFactory;
import com.unisoft.algotrader.event.data.Quote;
import com.unisoft.algotrader.event.execution.Order;
import com.unisoft.algotrader.provider.execution.SimulatorConfig;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by alex on 6/6/15.
 */
public class QuoteProcessorTest {
    private static QuoteProcessor processor;
    private Order order;
    private Quote quote;

    @BeforeClass
    public static void init() {
        processor = new QuoteProcessor();
    }

    @Before
    public void setup(){
        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.instId, Side.Buy, OrdType.Limit, 800, 88);
        quote = SampleEventFactory.createQuote(SampleEventFactory.testInstrument.instId, 95, 98, 500, 600);
    }

    @Test
    public void test_price(){
        assertEquals(0.0 , processor.getPrice(order, null, null), 0.0);

        assertEquals(quote.ask, processor.getPrice(order, quote, null), 0.0);

        order.side = Side.Sell;
        assertEquals(quote.bid, processor.getPrice(order, quote, null), 0.0);

    }

    @Test
    public void test_qty(){
        // no partial fill
        SimulatorConfig config = new SimulatorConfig();
        config.partialFills = false;
        assertEquals(order.ordQty, processor.getQty(order, quote, config), 0.0);

        // partial fill, buy
        config.partialFills = true;
        assertEquals(quote.askSize, processor.getQty(order, quote, config), 0.0);

        // partial fill, sell
        order.side = Side.Sell;
        assertEquals(quote.bidSize, processor.getQty(order, quote, config), 0.0);
    }
}
