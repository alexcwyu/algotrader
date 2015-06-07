package com.unisoft.algotrader.event;

import com.unisoft.algotrader.core.Currency;
import com.unisoft.algotrader.core.Instrument;
import com.unisoft.algotrader.core.OrdType;
import com.unisoft.algotrader.core.Side;
import com.unisoft.algotrader.event.data.Bar;
import com.unisoft.algotrader.event.data.Quote;
import com.unisoft.algotrader.event.data.Trade;
import com.unisoft.algotrader.event.execution.Order;
import com.unisoft.algotrader.provider.execution.SimulationExecutor;

/**
 * Created by alex on 6/6/15.
 */
public class SampleEventFactory {

    public static long ordId = 0;
    public static String MOCK_STRATEGY_ID = "MockStrategy";
    public static Instrument testInstrument = new Instrument("TestInst", "TestExch", Currency.HKD.ccyId);

    public static Order createOrder(String instId, Side side, OrdType type, double qty, double price){
        return createOrder(instId, side, type, qty, price, 0, SimulationExecutor.PROVIDER_ID);
    }

    public static Order createOrder(String instId, Side side, OrdType type, double qty, double price, double stopPx){
        return createOrder(instId, side, type, qty, price, stopPx, SimulationExecutor.PROVIDER_ID);
    }

    public static Order createOrder(String instId, Side side, OrdType type, double qty, double price, double stopPx, String providerId){
        return createOrder(instId, side, type, qty, price, stopPx, providerId, MOCK_STRATEGY_ID);
    }

    public static Order createOrder(String instId, Side side, OrdType type, double qty, double price, double stopPx, String providerId, String strategyId){
        Order order = new Order();
        order.orderId = ordId++;
        order.instId = instId;
        order.strategyId = strategyId;
        order.execProviderId = providerId;
        order.side= side;
        order.ordType = type;
        order.ordQty=qty;
        order.limitPrice = price;
        order.stopPx = stopPx;
        return order;
    }

    public static Quote createQuote(String instId, double bid,
                              double ask,
                              int bidSize,
                              int askSize){
        return new Quote(instId, System.currentTimeMillis(),
                bid,ask,bidSize,askSize);
    }

    public static Trade createTrade(String instId, double price,
                              int size){
        return new Trade(instId, System.currentTimeMillis(), price,size);
    }

    public static Bar createBar(
            String instId,
            double high,
            double low,
            double open,
            double close){
        return new Bar(instId, System.currentTimeMillis(), 60, high, low, open, close, 0, 0);
    }
}
