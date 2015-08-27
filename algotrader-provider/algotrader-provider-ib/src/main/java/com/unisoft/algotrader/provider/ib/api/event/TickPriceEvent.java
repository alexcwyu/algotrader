package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.provider.ib.api.IBConstants;

/**
 * Created by alex on 8/26/15.
 */
public class TickPriceEvent extends IBEvent<TickPriceEvent>  {

    public final IBConstants.TickType type;
    public final double price;
    public final boolean autoExecute;

    public TickPriceEvent(final String requestId, final IBConstants.TickType type,
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
}