package com.unisoft.algotrader.trading;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.trading.Portfolio;

import javax.inject.Inject;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by alex on 9/21/15.
 */
public class PortfolioManager {


    private Map<Integer, PortfolioProcessor> portfolioProcessorMap = Maps.newConcurrentMap();
    private Map<Integer, Portfolio> portfolioMap = Maps.newConcurrentMap();
//
//    private final Supplier<Integer> idSupplier;

    @Inject
    public PortfolioManager(){
    }

//
//    public PortfolioManager(Supplier<Integer> idSupplier){
//        this.idSupplier = idSupplier;
//    }

    public void register(PortfolioProcessor portfolioProcessor){
        if (portfolioProcessorMap.containsKey(portfolioProcessor.portfolioId())){
            throw new IllegalArgumentException("Portfolio Id already exist, portfolioId="+portfolioProcessor.portfolioId());
        }
        portfolioProcessorMap.put(portfolioProcessor.portfolioId(), portfolioProcessor);
        portfolioMap.put(portfolioProcessor.portfolioId(), portfolioProcessor.portfolio());
    }

    public PortfolioProcessor getPortfolioProcessor(int portfolioId){
        return portfolioProcessorMap.get(portfolioId);
    }

    public Portfolio getPortfolio(int portfolioId){
        return portfolioMap.get(portfolioId);
    }

//    public int nextId(){
//        return idSupplier.get();
//    }
}
