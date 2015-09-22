package com.unisoft.algotrader.trading;

import com.unisoft.algotrader.model.event.data.DataType;
import com.unisoft.algotrader.provider.ProviderId;
import com.unisoft.algotrader.provider.data.SubscriptionType;

import java.util.Collections;
import java.util.Set;

/**
 * Created by alex on 9/20/15.
 */
public class StrategyContext {

    public final ProviderId execProviderId;
    public final ProviderId dataProviderId;
    public final int portfolioId;

    public final Set<SubscriptionType> marketDataSet;
    public final Set<Long> instIdSet;
    public final long lastOrderId;

    public StrategyContext(ProviderId execProviderId, ProviderId dataProviderId,
                           int portfolioId,
                           Set<SubscriptionType> marketDataSet, Set<Long> instIdSet){
        this(execProviderId, dataProviderId, portfolioId, marketDataSet, instIdSet, 0);
    }

    public StrategyContext(ProviderId execProviderId, ProviderId dataProviderId,
                           int portfolioId,
                           Set<SubscriptionType> marketDataSet, Set<Long> instIdSet, long lastOrderId){
        this.execProviderId = execProviderId;
        this.dataProviderId = dataProviderId;
        this.portfolioId = portfolioId;
        this.marketDataSet = Collections.unmodifiableSet(marketDataSet);
        this.instIdSet = Collections.unmodifiableSet(instIdSet);
        this.lastOrderId = lastOrderId;
    }


}
