package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.model.event.EventHandler;

/**
 * Created by alex on 8/26/15.
 */
public interface IBEventHandler extends EventHandler {

    void onIBEvent(IBEvent ibEvent);
}
