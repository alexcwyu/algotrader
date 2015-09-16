package com.unisoft.algotrader.model.event.data;

import com.unisoft.algotrader.model.event.EventHandler;

/**
 * Created by alex on 4/16/15.
 */
public interface MarketDepthHandler extends EventHandler {

    default void onMarketDepth(MarketDepth marketDepth){
    }
}

