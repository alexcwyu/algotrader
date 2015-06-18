package com.unisoft.algotrader.event.data;

import com.unisoft.algotrader.core.id.InstId;
import com.unisoft.algotrader.event.Event;

/**
 * Created by alex on 4/12/15.
 */
public abstract class MarketData<E extends MarketData<? super E>> implements Event<MarketDataHandler, E> {
    public InstId instId = null;
    public long dateTime = -1;
    protected MarketData(){

    }
    public MarketData(InstId instId, long dateTime){
        this.instId = instId;
        this.dateTime = dateTime;
    }

    public void reset(){
        this.instId = null;
        this.dateTime = -1;
    }
}
