package com.unisoft.algotrader.strategy;

import com.google.common.collect.Maps;

import java.util.HashMap;
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
        return map.get(strategyId);
    }
}
