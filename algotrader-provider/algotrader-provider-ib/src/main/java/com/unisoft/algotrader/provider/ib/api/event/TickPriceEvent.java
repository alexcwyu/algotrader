package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.provider.ib.api.model.data.TickType;

/**
 * Created by alex on 8/26/15.
 */
public class TickPriceEvent extends IBEvent<TickPriceEvent>  {

    public final TickType type;
    public final double price;
    public final boolean autoExecute;

    public TickPriceEvent(final long requestId, final TickType type,
                          final double price, final boolean autoExecute){
        super(requestId);
        this.type = type;
        this.price = price;
        this.autoExecute = autoExecute;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onTickPriceEvent(this);
    }

    @Override
    public String toString() {
        return "TickPriceEvent{" +
                "type=" + type +
                ", price=" + price +
                ", autoExecute=" + autoExecute +
                "} " + super.toString();
    }
}