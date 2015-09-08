package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.provider.ib.api.model.data.TickType;

/**
 * Created by alex on 8/26/15.
 */
public class TickGenericEvent extends IBEvent<TickGenericEvent>  {

    public final TickType type;
    public final double value;

    public TickGenericEvent(final long requestId, final TickType type, final double value){
        super(requestId);
        this.type = type;
        this.value = value;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onTickGenericEvent(this);
    }

    @Override
    public String toString() {
        return "TickGenericEvent{" +
                "type=" + type +
                ", value=" + value +
                "} " + super.toString();
    }
}
