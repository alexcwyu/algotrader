package com.unisoft.algotrader.model.event.execution;

import com.google.common.base.Objects;
import com.lmax.disruptor.EventFactory;

import java.util.BitSet;

/**
 * Created by alex on 9/20/15.
 */
public class OrderEventContainer extends OrderEvent<OrderEventContainer>{



    public static final int NEW_ORDER_BIT = 1;
    public static final int REPLACE_ORDER_BIT = 2;
    public static final int CANCEL_ORDER_BIT = 3;

    public BitSet bitset = new BitSet();

    public Order newOrderRequest = null;
    public Order replaceOrderRequest = null;
    public Order cancelOrderRequest = null;


    public static final EventFactory<OrderEventContainer> FACTORY = new EventFactory(){
        @Override
        public OrderEventContainer newInstance() {
            return new OrderEventContainer();
        }
    };

    public void reset(){
        bitset.clear();
        newOrderRequest = null;
        replaceOrderRequest = null;
        cancelOrderRequest = null;
    }

    @Override
    public void on(OrderEventHandler handler) {
        handler.onOrderEventContainer(this);
    }

    public boolean hasNewOrderRequest(){
        return bitset.get(NEW_ORDER_BIT);
    }

    public boolean hasReplaceOrderRequest(){
        return bitset.get(REPLACE_ORDER_BIT);
    }


    public boolean hasCancelOrderRequest(){
        return bitset.get(CANCEL_ORDER_BIT);
    }

    public void setNewOrderRequest(Order newOrderRequest){
        this.newOrderRequest = newOrderRequest;
        bitset.set(NEW_ORDER_BIT);
    }


    public void setReplaceOrderRequest(Order replaceOrderRequest) {
        this.replaceOrderRequest = replaceOrderRequest;
        bitset.set(REPLACE_ORDER_BIT);
    }

    public void setCancelOrderRequest(Order cancelOrderRequest) {
        this.cancelOrderRequest = cancelOrderRequest;
        bitset.set(CANCEL_ORDER_BIT);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderEventContainer)) return false;
        OrderEventContainer that = (OrderEventContainer) o;
        return Objects.equal(bitset, that.bitset) &&
                Objects.equal(newOrderRequest, that.newOrderRequest) &&
                Objects.equal(replaceOrderRequest, that.replaceOrderRequest) &&
                Objects.equal(cancelOrderRequest, that.cancelOrderRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(bitset, newOrderRequest, replaceOrderRequest, cancelOrderRequest);
    }

    @Override
    public String toString() {
        return "OrderEventContainer{" +
                "bitset=" + bitset +
                ", newOrderRequest=" + newOrderRequest +
                ", replaceOrderRequest=" + replaceOrderRequest +
                ", cancelOrderRequest=" + cancelOrderRequest +
                "} " + super.toString();
    }
}
