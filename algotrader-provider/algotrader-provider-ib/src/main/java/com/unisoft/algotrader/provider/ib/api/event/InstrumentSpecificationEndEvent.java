package com.unisoft.algotrader.provider.ib.api.event;

/**
 * Created by alex on 8/26/15.
 */
public class InstrumentSpecificationEndEvent extends IBEvent<InstrumentSpecificationEndEvent>  {


    public InstrumentSpecificationEndEvent(final long requestId){
        super(requestId);
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onInstrumentSpecificationEndEvent(this);
    }

    @Override
    public String toString() {
        return "InstrumentSpecificationEndEvent{}";
    }
}