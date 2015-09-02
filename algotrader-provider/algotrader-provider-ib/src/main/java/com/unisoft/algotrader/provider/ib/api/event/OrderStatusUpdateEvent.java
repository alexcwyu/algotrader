package com.unisoft.algotrader.provider.ib.api.event;


import com.unisoft.algotrader.provider.ib.api.model.constants.OrderStatus;

/**
 * Created by alex on 8/26/15.
 */
public class OrderStatusUpdateEvent extends IBEvent<OrderStatusUpdateEvent>  {

    public final OrderStatus orderStatus;
    public final int filledQuantity;
    public final int remainingQuantity;
    public final double averageFilledPrice;
    public final int permanentId;
    public final String parentOrderId;
    public final double lastFilledPrice;
    public final int clientId;
    public final String heldCause;

    public OrderStatusUpdateEvent(final long requestId, final OrderStatus orderStatus, final int filledQuantity,
                                  final int remainingQuantity, final double averageFilledPrice, final int permanentId,
                                  final String parentOrderId, final double lastFilledPrice, final int clientId, final String heldCause){
        super(requestId);
        this.orderStatus = orderStatus;
        this.filledQuantity = filledQuantity;
        this.remainingQuantity = remainingQuantity;
        this.averageFilledPrice = averageFilledPrice;
        this.permanentId = permanentId;
        this.parentOrderId = parentOrderId;
        this.lastFilledPrice = lastFilledPrice;
        this.clientId = clientId;
        this.heldCause = heldCause;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onOrderStatusUpdateEvent(this);
    }

    @Override
    public String toString() {
        return "OrderStatusUpdateEvent{" +
                "orderStatus=" + orderStatus +
                ", filledQuantity=" + filledQuantity +
                ", remainingQuantity=" + remainingQuantity +
                ", averageFilledPrice=" + averageFilledPrice +
                ", permanentId=" + permanentId +
                ", parentOrderId='" + parentOrderId + '\'' +
                ", lastFilledPrice=" + lastFilledPrice +
                ", clientId=" + clientId +
                ", heldCause='" + heldCause + '\'' +
                "} " + super.toString();
    }
}