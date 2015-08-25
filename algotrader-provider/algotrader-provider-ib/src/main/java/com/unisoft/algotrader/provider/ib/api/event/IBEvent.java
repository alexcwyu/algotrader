package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.model.event.Event;

/**
 * Created by alex on 8/26/15.
 */
public abstract class IBEvent <E extends IBEvent<? super E>> implements Event<IBEventHandler, E> {

    private long sequence;
    private String requestId;
    public final long timeStamp;

    protected IBEvent(){
        timeStamp = System.currentTimeMillis();
    }

    public IBEvent(String requestId){
        this();
        this.requestId = requestId;
    }

    public final long getTimeStamp() {
        return timeStamp;
    }

    public final void setSequence(final long sequence) {
        this.sequence = sequence;
    }

    public final long getSequence() {
        return sequence;
    }

    public final String getRequestId() {
        return requestId;
    }


    @Override
    public void on(IBEventHandler handler) {
        handler.onIBEvent(this);
    }

    public void reset() {
        throw new UnsupportedOperationException();
    }

    public void copy(E event) {
        throw new UnsupportedOperationException();
    }
}
