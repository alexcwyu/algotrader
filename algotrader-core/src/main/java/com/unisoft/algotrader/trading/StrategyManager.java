package com.unisoft.algotrader.trading;

import com.google.common.collect.Maps;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by alex on 5/19/15.
 */
@Singleton
public class StrategyManager {

    private Map<Integer, Strategy> map = Maps.newConcurrentMap();

    private final Supplier<Integer> strategyIdSupplier;

    @Inject
    public StrategyManager(Supplier<Integer> strategyIdSupplier){
        this.strategyIdSupplier = strategyIdSupplier;
    }

    public void register(Strategy strategy){
        if (map.containsKey(strategy.strategyId)){
            throw new IllegalArgumentException("Strategy Id already exist, strategyId="+strategy.strategyId);
        }
        map.put(strategy.strategyId, strategy);
    }

    public Strategy get(int strategyId){
        return map.get(strategyId);
    }

    public int nextStrategyId(){
        return strategyIdSupplier.get();
    }
}
