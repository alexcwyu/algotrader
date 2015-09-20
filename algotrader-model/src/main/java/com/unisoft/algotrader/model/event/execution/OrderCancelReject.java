package com.unisoft.algotrader.model.event.execution;

import com.datastax.driver.mapping.annotations.Column;
import com.unisoft.algotrader.model.trading.CxlRejReason;
import com.unisoft.algotrader.model.trading.CxlRejResponseTo;
import com.unisoft.algotrader.model.trading.OrdStatus;

/**
 * Created by alex on 5/24/15.
 */
public class OrderCancelReject<E extends OrderCancelReject<? super E>> extends ExecutionEvent<E> {
    //TODO

//    @PartitionKey
    @Column(name="cl_order_id")
    public long clOrderId;

    @Column(name="order_id")
    public long orderId = -1;

    @Column(name="orig_cl_order_id")
    public long origClOrderId = -1;

    @Column(name="date_time")
    public long dateTime;

    @Column(name="ord_status")
    public OrdStatus ordStatus = OrdStatus.New;

    @Column(name="cxl_rej_reason")
    public CxlRejReason cxlRejReason;

    @Column(name="cxl_rej_response_to")
    public CxlRejResponseTo cxlRejResponseTo;


    @Override
    public void on(ExecutionEventHandler handler) {
        handler.onOrderCancelReject(this);
    }

    public long clOrderId() {
        return clOrderId;
    }

    public void clOrderId(long clOrderId) {
        this.clOrderId = clOrderId;
    }

    public CxlRejReason cxlRejReason() {
        return cxlRejReason;
    }

    public void cxlRejReason(CxlRejReason cxlRejReason) {
        this.cxlRejReason = cxlRejReason;
    }

    public CxlRejResponseTo cxlRejResponseTo() {
        return cxlRejResponseTo;
    }

    public void cxlRejResponseTo(CxlRejResponseTo cxlRejResponseTo) {
        this.cxlRejResponseTo = cxlRejResponseTo;
    }

    public long dateTime() {
        return dateTime;
    }

    public void dateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public long orderId() {
        return orderId;
    }

    public void orderId(long orderId) {
        this.orderId = orderId;
    }

    public OrdStatus ordStatus() {
        return ordStatus;
    }

    public void ordStatus(OrdStatus ordStatus) {
        this.ordStatus = ordStatus;
    }

    public long origClOrderId() {
        return origClOrderId;
    }

    public void origClOrderId(long origClOrderId) {
        this.origClOrderId = origClOrderId;
    }
}
