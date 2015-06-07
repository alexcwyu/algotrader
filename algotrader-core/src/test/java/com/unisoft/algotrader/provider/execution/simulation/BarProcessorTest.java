package com.unisoft.algotrader.provider.execution.simulation;

import com.unisoft.algotrader.core.OrdType;
import com.unisoft.algotrader.core.Side;
import com.unisoft.algotrader.event.SampleEventFactory;
import com.unisoft.algotrader.event.data.Bar;
import com.unisoft.algotrader.event.execution.Order;
import com.unisoft.algotrader.provider.execution.SimulatorConfig;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by alex on 6/6/15.
 */
public class BarProcessorTest {

    private static BarProcessor processor;
    private Order order;
    private Bar bar;


    @BeforeClass
    public static void init(){
        processor = new BarProcessor();
    }

    @Before
    public void setup(){
        order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument.instId, Side.Buy, OrdType.Limit, 800, 88);
        bar = SampleEventFactory.createBar(SampleEventFactory.testInstrument.instId, 95, 60, 87, 88);
    }

    @Test
    public void test_price(){
        SimulatorConfig config = new SimulatorConfig();
        config.fillOnBarMode = SimulatorConfig.FillOnBarMode.LastBarClose;
        assertEquals(bar.close, processor.getPrice(order, bar, config), 0.0);

        config.fillOnBarMode = SimulatorConfig.FillOnBarMode.NextBarOpen;
        assertEquals(bar.open, processor.getPrice(order, bar, config), 0.0);

        config.fillOnBarMode = SimulatorConfig.FillOnBarMode.NextBarClose;
        assertEquals(bar.close, processor.getPrice(order, bar, config), 0.0);
    }

    @Test
    public void test_qty(){
        assertEquals(order.ordQty, processor.getQty(order, bar, null), 0.0);
    }
}
