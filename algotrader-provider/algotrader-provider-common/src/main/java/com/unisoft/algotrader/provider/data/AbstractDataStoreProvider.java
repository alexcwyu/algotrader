package com.unisoft.algotrader.provider.data;

import com.unisoft.algotrader.provider.ProviderManager;

/**
 * Created by alex on 6/16/15.
 */
public abstract class AbstractDataStoreProvider extends AbstractHistoricalDataProvider implements DataStoreProvider {

    protected AbstractDataStoreProvider(ProviderManager providerManager){
        super(providerManager);
        providerManager.addDataStoreProvider(this);
    }
}
