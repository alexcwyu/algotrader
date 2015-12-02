package com.unisoft.algotrader.config;

import com.unisoft.algotrader.trading.BackTestOrderManager;
import com.unisoft.algotrader.trading.OrderManager;
import com.unisoft.algotrader.utils.config.BaseConfigModule;

/**
 * Created by alex on 7/16/15.
 */
public class BackTestingConfigModule extends BaseConfigModule {

    protected void configure() {
        super.configure();
        bind(OrderManager.class).to(BackTestOrderManager.class);

    }
}
