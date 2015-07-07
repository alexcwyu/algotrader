package com.unisoft.algotrader.provider.realtime;

import com.unisoft.algotrader.provider.DataProvider;

/**
 * Created by alex on 5/17/15.
 */
public interface RealTimeDataProvider extends DataProvider {
    void subscribe(String instId);
}
