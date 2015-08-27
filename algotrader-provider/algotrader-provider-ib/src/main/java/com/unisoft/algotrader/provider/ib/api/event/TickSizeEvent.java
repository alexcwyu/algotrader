package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.provider.ib.api.IBConstants;

/**
 * Created by alex on 8/26/15.
 */
public class TickSizeEvent extends IBEvent<TickSizeEvent>  {

    public final IBConstants.TickType type;
    public final int size;

    public TickSizeEvent(final String requestId, final IBConstants.TickType type,
                          final int size){
        super(requestId);
        this.type = type;
        this.size = size;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onTickSizeEvent(this);
    }
}