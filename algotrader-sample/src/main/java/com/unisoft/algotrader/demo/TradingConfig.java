package com.unisoft.algotrader.demo;

import com.unisoft.algotrader.trading.Strategy;

/**
 * Created by alex on 11/30/15.
 */
public class TradingConfig {
    public final int marketDataProviderId;
    public final int executionDataProviderId;
    public final int strategyId;
    public final long [] instIds;

    public TradingConfig(int marketDataProviderId, int executionDataProviderId, int strategyId, long ... instIds) {
        this.marketDataProviderId = marketDataProviderId;
        this.executionDataProviderId = executionDataProviderId;
        this.strategyId = strategyId;
        this.instIds = instIds;
    }

}
