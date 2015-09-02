package com.unisoft.algotrader.provider.ib.api.event;


/**
 * Created by alex on 8/26/15.
 */
public class CompositeTickEvent extends IBEvent<CompositeTickEvent> {

    public final TickPriceEvent tickPriceEvent;
    public final TickSizeEvent tickSizeEvent;

    public CompositeTickEvent(final TickPriceEvent tickPriceEvent, TickSizeEvent tickSizeEvent) {
        this.tickPriceEvent = tickPriceEvent;
        this.tickSizeEvent = tickSizeEvent;
    }
    @Override
    public void on(IBEventHandler handler) {
        handler.onCompositeTickEvent(this);
    }

    @Override
    public String toString() {
        return "CompositeTickEvent{" +
                "tickPriceEvent=" + tickPriceEvent +
                ", tickSizeEvent=" + tickSizeEvent +
                "} " + super.toString();
    }
}
