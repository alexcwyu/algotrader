package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.provider.ib.api.model.constants.TickType;

/**
 * Created by alex on 8/26/15.
 */
public class TickStringEvent extends IBEvent<TickStringEvent>  {

    public final TickType type;
    public final String value;

    public TickStringEvent(final long requestId, final TickType type,
                           final String value){
        super(requestId);
        this.type = type;
        this.value = value;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onTickStringEvent(this);
    }

    @Override
    public String toString() {
        return "TickStringEvent{" +
                "type=" + type +
                ", value='" + value + '\'' +
                "} " + super.toString();
    }
}