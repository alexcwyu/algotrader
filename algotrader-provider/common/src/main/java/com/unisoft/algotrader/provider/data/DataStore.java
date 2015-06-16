package com.unisoft.algotrader.provider.data;

import com.unisoft.algotrader.event.data.MarketDataHandler;
import com.unisoft.algotrader.provider.Provider;

/**
 * Created by alex on 6/16/15.
 */
public interface DataStore extends Provider, MarketDataHandler {

    void close();
}
