package com.unisoft.algotrader.model.event;

/**
 * Created by alex on 8/2/15.
 */
public class EventWrapper {

    private Event wrappedEvent;

    Event getWrappedEvent() {
        return wrappedEvent;
    }

    void setWrappedEvent(final Event wrappedEvent) {
        this.wrappedEvent = wrappedEvent;
    }
}
