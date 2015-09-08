package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.provider.ib.api.model.data.TickType;

/**
 * Created by alex on 8/26/15.
 */
public class TickSizeEvent extends IBEvent<TickSizeEvent>  {

    public final TickType type;
    public final int size;

    public TickSizeEvent(final long requestId, final TickType type,
                          final int size){
        super(requestId);
        this.type = type;
        this.size = size;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onTickSizeEvent(this);
    }

    @Override
    public String toString() {
        return "TickSizeEvent{" +
                "type=" + type +
                ", size=" + size +
                "} " + super.toString();
    }
}