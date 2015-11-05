package com.unisoft.algotrader.demo;

import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.Quote;
import com.unisoft.algotrader.model.event.data.Trade;
import com.unisoft.algotrader.model.event.execution.ExecutionReport;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.model.trading.*;
import com.unisoft.algotrader.persistence.InMemoryRefDataStore;
import com.unisoft.algotrader.persistence.InstrumentFactory;
import com.unisoft.algotrader.provider.ProviderId;

/**
 * Created by alex on 6/6/15.
 */
public class SampleEventFactory {

    public static long ordId = 0;
    public static long execId = 0;
    public static int MOCK_PORTFOLIO_ID = 1;
    public static int MOCK_STRATEGY_ID = 1;
    public static InMemoryRefDataStore REF_DATA_STORE = new InMemoryRefDataStore();
    public static InstrumentFactory INSTRUMEN_FACTORY = new InstrumentFactory(REF_DATA_STORE);
    public static Instrument TEST_HKD_INSTRUMENT = INSTRUMEN_FACTORY.createStock("testInst1", "testInst1", "testExch1","HKD");
    public static Instrument TEST_USD_INSTRUMENT = INSTRUMEN_FACTORY.createStock("testInst2", "testInst2", "testExch2","USD");

    public static Order createOrder(long instId, Side side, OrdType type, double qty, double price){
        return createOrder(instId, side, type, qty, price, 0, ProviderId.Simulation.id);
    }

    public static Order createOrder(long instId, Side side, OrdType type, double qty, double price, double stopPrice){
        return createOrder(instId, side, type, qty, price, stopPrice, ProviderId.Simulation.id);
    }

    public static Order createOrder(long instId, Side side, OrdType type, double qty, double price, double stopPrice, int providerId){
        return createOrder(instId, side, type, qty, price, stopPrice, TimeInForce.Day, providerId, MOCK_PORTFOLIO_ID, MOCK_STRATEGY_ID);
    }

    public static Order createOrder(long instId, Side side, OrdType type, double qty, double price, double stopPrice, TimeInForce tif, int providerId, int portfolioId, int strategyId){
        Order order = new Order();
        order.clOrderId = ++ordId;
        order.instId = instId;
        order.providerId = providerId;
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

    public static Order newLimitOrder(long instId, int strategyId, int providerId, Side side, double price, double qty, TimeInForce tif){
        Order order = new Order();
        order.clOrderId = ++ordId;
        order.instId = instId;
        order.strategyId = strategyId;
        order.providerId = providerId;
        order.side= side;
        order.ordType = OrdType.Limit;
        order.ordQty=qty;
        order.limitPrice = price;
        order.tif = tif;
        return order;
    }

    public static Order newMarketOrder(long instId, int strategyId, int providerId, Side side, double qty, TimeInForce tif){
        Order order = new Order();
        order.clOrderId = ++ordId;
        order.instId = instId;
        order.strategyId = strategyId;
        order.providerId = providerId;
        order.side= side;
        order.ordType = OrdType.Market;
        order.ordQty=qty;
        order.tif = tif;
        return order;
    }


    public static ExecutionReport createExecutionReport(Order order){
        double filledPrice = order.limitPrice == 0.0? 8.8 : order.limitPrice;
        return createExecutionReport(order, OrdStatus.Filled, order.ordQty, filledPrice, order.ordQty, filledPrice);
    }

    public static ExecutionReport createExecutionReport(long clOrderId, long instId, Side side, OrdType type, double qty, double price, double stopPrice, TimeInForce tif, OrdStatus ordStatus, double lastQty, double lastPrice, double filledQty, double avgPrice){
        ExecutionReport er = new ExecutionReport();
        er.execId = ++execId;
        er.clOrderId = clOrderId;
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
        er.clOrderId = order.clOrderId;
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

    public static Portfolio createPortfolio(int portfolioId, String accountName){
        return new Portfolio(portfolioId, accountName);
    }

    public static Quote createQuote(long instId, double bid,
                              double ask,
                              int bidSize,
                              int askSize){
        return new Quote(instId, System.currentTimeMillis(),
                bid,ask,bidSize,askSize);
    }

    public static Trade createTrade(long instId, double price,
                              int size){
        return new Trade(instId, System.currentTimeMillis(), price,size);
    }

    public static Bar createBar(
            long instId,
            double open,
            double high,
            double low,
            double close){
        return new Bar(instId, 60, System.currentTimeMillis(), open, high, low, close, 0, 0);
    }
}
