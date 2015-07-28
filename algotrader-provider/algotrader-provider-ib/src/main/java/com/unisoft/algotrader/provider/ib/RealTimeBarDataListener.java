package com.unisoft.algotrader.provider.ib;

import ch.aonyx.broker.ib.api.data.bar.RealTimeBarEvent;
import ch.aonyx.broker.ib.api.data.bar.RealTimeBarEventListener;

/**
 * Created by alex on 7/27/15.
 */
public class RealTimeBarDataListener extends  MarketDataListener implements RealTimeBarEventListener {


    public RealTimeBarDataListener(IBProvider ibProvider) {
        super(ibProvider);
    }

    @Override
    public void notify(RealTimeBarEvent e) {
        publish(e.getId(), e.getTimestamp(), e.getOpen(), e.getHigh(), e.getLow(), e.getClose(), e.getVolume());
    }
}
