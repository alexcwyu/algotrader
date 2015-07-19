package com.unisoft.algotrader.provider.config;

import com.unisoft.algotrader.provider.data.DataService;
import com.unisoft.algotrader.provider.data.DefaultDataService;
import com.unisoft.algotrader.utils.config.BaseConfigModule;

/**
 * Created by alex on 7/19/15.
 */
public class DataServiceConfigModule extends BaseConfigModule {

    protected void configure() {
        super.configure();
        bind(DataService.class).to(DefaultDataService.class);
    }
}
