package com.unisoft.algotrader.trading;

import com.google.common.collect.Maps;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

/**
 * Created by alex on 5/19/15.
 */
@Singleton
public class StrategyManager {

    public static final StrategyManager INSTANCE;

    static {
        INSTANCE = new StrategyManager();
    }

    @Inject
    StrategyManager(){

    }

    private Map<String, Strategy> map = Maps.newConcurrentMap();

    public void register(Strategy strategy){
        map.put(strategy.strategyId, strategy);
    }

    public Strategy get(String strategyId){
        if (strategyId == null)
            return null;
        return map.get(strategyId);
    }
}
