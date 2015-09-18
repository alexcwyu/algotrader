package com.unisoft.algotrader.model.event.execution;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.unisoft.algotrader.model.trading.Side;

/**
 * Created by alex on 5/24/15.
 */
public class OrderCancelRequest {

    @PartitionKey
    @Column(name="cl_order_id")
    public long clOrderId;

    @Column(name="order_id")
    public long orderId = -1;

    @Column(name="orig_cl_order_id")
    public long origClOrderId;

    @Column(name="inst_id")
    public long instId;

    @Column(name="date_time")
    public long dateTime;

    public Side side;

    @Column(name="ord_qty")
    public double ordQty;

    @Column(name="exec_provider_id")
    public String execProviderId;

    @Column(name="portfolio_id")
    public String portfolioId;

    public long clOrderId() {
        return clOrderId;
    }

    public OrderCancelRequest setClOrderId(long clOrderId) {
        this.clOrderId = clOrderId;
        return this;
    }
}
