package com.unisoft.algotrader.config;

import com.unisoft.algotrader.model.trading.Account;
import com.unisoft.algotrader.model.trading.Portfolio;
import com.unisoft.algotrader.trading.Strategy;

import javax.inject.Inject;

/**
 * Created by alex on 7/16/15.
 */
public class TradingConfig {
    @Inject
    Account account;

    @Inject
    Portfolio portfolio;

    @Inject
    Strategy strategy;


}
