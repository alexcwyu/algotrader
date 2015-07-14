package com.unisoft.algotrader.trading;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alex on 5/19/15.
 */
public class StrategyManager {

    public static final StrategyManager INSTANCE;

    static {
        INSTANCE = new StrategyManager();
    }


    private StrategyManager(){

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
