package com.unisoft.algotrader.trading;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.trading.Portfolio;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by alex on 9/21/15.
 */
@Singleton
public class PortfolioProcessorManager {


    private Map<Integer, PortfolioProcessor> portfolioProcessorMap = Maps.newConcurrentMap();

    public void register(PortfolioProcessor portfolioProcessor){
        if (portfolioProcessorMap.containsKey(portfolioProcessor.portfolioId())){
            throw new IllegalArgumentException("Portfolio Id already exist, portfolioId="+portfolioProcessor.portfolioId());
        }
        portfolioProcessorMap.put(portfolioProcessor.portfolioId(), portfolioProcessor);
    }

    public PortfolioProcessor getPortfolioProcessor(int portfolioId){
        return portfolioProcessorMap.get(portfolioId);
    }


}
