package com.unisoft.algotrader.event;

import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.Quote;
import com.unisoft.algotrader.model.event.data.Trade;
import com.unisoft.algotrader.model.event.execution.ExecutionReport;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.refdata.InstrumentManager;
import com.unisoft.algotrader.model.trading.*;

/**
 * Created by alex on 6/6/15.
 */
public class SampleEventFactory {

    public static long ordId = 0;
    public static long execId = 0;
    public static String MOCK_PORTFOLIO_ID = "MockPortfolio";
    public static String MOCK_STRATEGY_ID = "MockStrategy";
    public static Instrument testInstrument = InstrumentManager.INSTANCE.createStock("testInst1","testExch1","HKD");
    public static Instrument testInstrument2 = InstrumentManager.INSTANCE.createStock("testInst2","testExch2","USD");

    public static Order createOrder(int instId, Side side, OrdType type, double qty, double price){
        return createOrder(instId, side, type, qty, price, 0, "Simulator");
    }

    public static Order createOrder(int instId, Side side, OrdType type, double qty, double price, double stopPrice){
        return createOrder(instId, side, type, qty, price, stopPrice, "Simulator");
    }

    public static Order createOrder(int instId, Side side, OrdType type, double qty, double price, double stopPrice, String providerId){
        return createOrder(instId, side, type, qty, price, stopPrice, TimeInForce.Day, providerId, MOCK_PORTFOLIO_ID, MOCK_STRATEGY_ID);
    }

    public static Order createOrder(int instId, Side side, OrdType type, double qty, double price, double stopPrice, TimeInForce tif, String providerId, String portfolioId, String strategyId){
        Order order = new Order();
        order.orderId = ++ordId;
        order.instId = instId;
        order.execProviderId = providerId;
        order.portfolioId = portfolioId;
        order.strategyId = strategyId;
        order.side= side;
        order.ordType = type;
        order.ordQty=qty;
        order.limitPrice = price;
        order.stopPrice = stopPrice;
        order.tif = tif;
        order.ordStatus = OrdStatus.New;
        order.dateTime = System.currentTimeMillis();
        return order;
    }


    public static ExecutionReport createExecutionReport(Order order){
        double filledPrice = order.limitPrice == 0.0? 8.8 : order.limitPrice;
        return createExecutionReport(order, OrdStatus.Filled, order.ordQty, filledPrice, order.ordQty, filledPrice);
    }

    public static ExecutionReport createExecutionReport(long orderId, int instId, Side side, OrdType type, double qty, double price, double stopPrice, TimeInForce tif, OrdStatus ordStatus, double lastQty, double lastPrice, double filledQty, double avgPrice){
        ExecutionReport er = new ExecutionReport();
        er.execId = ++execId;
        er.orderId = orderId;
        er.instId = instId;
        er.side= side;
        er.ordType = type;
        er.ordQty=qty;
        er.limitPrice = price;
        er.stopPrice = stopPrice;
        er.tif = tif;

        er.transactionTime = System.currentTimeMillis();
        er.ordStatus = ordStatus;
        er.lastQty = lastQty;
        er.lastPrice = lastPrice;
        er.filledQty = filledQty;
        er.avgPrice = avgPrice;

        return er;
    }

    public static ExecutionReport createExecutionReport(Order order, OrdStatus ordStatus, double lastQty, double lastPrice, double filledQty, double avgPrice){
        ExecutionReport er = new ExecutionReport();
        er.execId = ++execId;
        er.orderId = order.orderId;
        er.instId = order.instId;
        er.side= order.side;
        er.ordType = order.ordType;
        er.ordQty=order.ordQty;
        er.limitPrice = order.limitPrice;
        er.stopPrice = order.stopPrice;
        er.tif = order.tif;

        er.transactionTime = System.currentTimeMillis();
        er.ordStatus = ordStatus;
        er.lastQty = lastQty;
        er.lastPrice = lastPrice;
        er.filledQty = filledQty;
        er.avgPrice = avgPrice;

        return er;
    }

    public static Portfolio createPortfolio(String name, String accountName){
        return new Portfolio(name, accountName);
    }

    public static Quote createQuote(int instId, double bid,
                              double ask,
                              int bidSize,
                              int askSize){
        return new Quote(instId, System.currentTimeMillis(),
                bid,ask,bidSize,askSize);
    }

    public static Trade createTrade(int instId, double price,
                              int size){
        return new Trade(instId, System.currentTimeMillis(), price,size);
    }

    public static Bar createBar(
            int instId,
            double open,
            double high,
            double low,
            double close){
        return new Bar(instId, 60, System.currentTimeMillis(), open, high, low, close, 0, 0);
    }
}
