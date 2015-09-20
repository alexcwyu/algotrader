package com.unisoft.algotrader.trading;

import com.unisoft.algotrader.model.event.data.DataType;
import com.unisoft.algotrader.provider.ProviderId;

import java.util.Collections;
import java.util.Set;

/**
 * Created by alex on 9/20/15.
 */
public class StrategyContext {

    public final ProviderId providerId;
    public final int portfolioId;

    public final Set<DataType> marketDataSet;
    public final Set<Long> instIdSet;
    public final long lastOrderId;



    public StrategyContext(ProviderId providerId,
                           int portfolioId,
                           Set<DataType> marketDataSet, Set<Long> instIdSet){
        this(providerId, portfolioId, marketDataSet, instIdSet, 0);
    }

    public StrategyContext(ProviderId providerId,
                           int portfolioId,
                           Set<DataType> marketDataSet, Set<Long> instIdSet, long lastOrderId){
        this.providerId = providerId;
        this.portfolioId = portfolioId;
        this.marketDataSet = Collections.unmodifiableSet(marketDataSet);
        this.instIdSet = Collections.unmodifiableSet(instIdSet);
        this.lastOrderId = lastOrderId;
    }


}
