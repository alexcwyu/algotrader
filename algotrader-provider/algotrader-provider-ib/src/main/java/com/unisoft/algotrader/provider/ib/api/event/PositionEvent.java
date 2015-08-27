package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.model.refdata.Instrument;

/**
 * Created by alex on 8/26/15.
 */
public class PositionEvent extends IBEvent<PositionEvent>  {

    public final String account;
    public final Instrument instrument;
    public final int pos;
    public final double avgCost;

    public PositionEvent(String account, Instrument instrument, int pos, double avgCost){
        this.account = account;
        this.instrument = instrument;
        this.pos = pos;
        this.avgCost = avgCost;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onPositionEvent(this);
    }
}