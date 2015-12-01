package com.unisoft.algotrader.provider.data;

import com.unisoft.algotrader.provider.ProviderManager;

/**
 * Created by alex on 7/25/15.
 */
public abstract class AbstractHistoricalDataProvider implements HistoricalDataProvider{

    protected AbstractHistoricalDataProvider(ProviderManager providerManager){
        providerManager.addHistoricalDataProvider(this);
    }

    protected AbstractHistoricalDataProvider(){
    }



}
