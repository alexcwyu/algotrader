package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.model.event.Event;

/**
 * Created by alex on 8/26/15.
 */
public abstract class IBEvent <E extends IBEvent<? super E>> implements Event<IBEventHandler, E> {

    private long sequence;
    public final long requestId;
    public final long timeStamp;

    protected IBEvent(){
        this.requestId = -1;
        timeStamp = System.currentTimeMillis();
    }

    public IBEvent(long requestId){
        this.requestId = requestId;
        timeStamp = System.currentTimeMillis();
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

    public final long getRequestId() {
        return requestId;
    }


    @Override
    public void on(IBEventHandler handler) {
        handler.onIBEvent(this);
    }

    @Override
    public String toString() {
        return "IBEvent{" +
                "sequence=" + sequence +
                ", requestId=" + requestId +
                ", timeStamp=" + timeStamp +
                "} " + super.toString();
    }
}
