package com.unisoft.algotrader.core;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alex on 6/4/15.
 */
public class PortfolioManager {


    public static final PortfolioManager INSTANCE;


    static {
        INSTANCE = new PortfolioManager();
    }


    private PortfolioManager(){
    }

    private Map<String, Portfolio> map = Maps.newConcurrentMap();

    public void add(Portfolio portfolio){
        map.put(portfolio.getPortfolioId(), portfolio);
    }

    public Portfolio get(String portfolioId){
        return map.get(portfolioId);
    }
}
