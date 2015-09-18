package com.unisoft.algotrader.model.event.execution;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.lmax.disruptor.EventFactory;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.trading.*;

/**
 * Created by alex on 5/24/15.
 */
public class OrderCancelReject<E extends OrderCancelReject<? super E>> implements Event<ExecutionHandler, E> {
    //TODO

//    @PartitionKey
    @Column(name="cl_order_id")
    public long clOrderId;
//
    @Column(name="order_id")
    public long orderId = -1;

    @Column(name="orig_cl_order_id")
    public long origClOrderId = -1;
//
//    @Column(name="inst_id")
//    public long instId;
//
    @Column(name="date_time")
    public long dateTime;
//
//    @Column(name="ord_type")
//    public OrdType ordType;
//
    @Column(name="ord_status")
    public OrdStatus ordStatus = OrdStatus.New;

    @Column(name="cxl_rej_reason")
    public CxlRejReason cxlRejReason;

    @Column(name="cxl_rej_response_to")
    public CxlRejResponseTo cxlRejResponseTo;
//
//    @Column(name="limit_price")
//    public double limitPrice;
//
//    @Column(name="stop_price")
//    public double stopPrice;
//
//    @Column(name="ord_qty")
//    public double ordQty;
//
//    @Column(name="filled_qty")
//    public double filledQty = 0;
//
//    @Column(name="avg_price")
//    public double avgPrice;
//
//    @Column(name="last_qty")
//    public double lastQty;
//
//    @Column(name="last_price")
//    public double lastPrice;
//
//    @Column(name="stop_limit_ready")
//    public boolean stopLimitReady = false;
//
//    @Column(name="trailing_stop_exec_price")
//    public double trailingStopExecPrice;
//
//    public TimeInForce tif;
//
//    public Side side;
//
//    @Column(name="exec_provider_id")
//    public String execProviderId;
//
//    @Column(name="portfolio_id")
//    public String portfolioId;
//
//    @Column(name="strategy_id")
//    public String strategyId;
//
//    public String text;


    public static final EventFactory<OrderCancelReject> FACTORY = new EventFactory(){
    @Override
    public OrderCancelReject newInstance() {
        return new OrderCancelReject();
        }
    };

    @Override
    public void on(ExecutionHandler handler) {
        handler.onOrderCancelReject(this);
    }

    @Override
    public void reset() {

    }

    @Override
    public void copy(E event) {

    }
}
