package com.unisoft.algotrader.provider;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.provider.data.DataStoreProvider;
import com.unisoft.algotrader.provider.data.HistoricalDataProvider;
import com.unisoft.algotrader.provider.data.RealTimeDataProvider;
import com.unisoft.algotrader.provider.execution.ExecutionProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;


/**
 * Created by alex on 5/19/15.
 */
@Singleton
public class ProviderManager {

    @Inject
    public ProviderManager(){

    }

    private Map<Integer, Provider> providerIdMap = Maps.newHashMap();
    private Map<ProviderId, Provider> providerMap = Maps.newHashMap();
    private Map<ProviderId, DataStoreProvider> dataStoreProviderMap = Maps.newHashMap();
    private Map<ProviderId, ExecutionProvider> executionProviderMap = Maps.newHashMap();
    private Map<ProviderId, HistoricalDataProvider> histDataProviderMap = Maps.newHashMap();
    private Map<ProviderId, RealTimeDataProvider> rtDataProviderMap = Maps.newHashMap();

    private void addProvider(Provider provider){
        if (providerMap.containsKey(provider.providerId())) {
            throw new IllegalArgumentException("Provider id is registered, id=" + provider.providerId());
        }
        providerMap.put(provider.providerId(), provider);
        if (providerIdMap.containsKey(provider.providerId().id)) {
            throw new IllegalArgumentException("Provider id is registered, id=" + provider.providerId().id);
        }
        providerIdMap.put(provider.providerId().id, provider);
    }

    public void addHistoricalDataProvider(HistoricalDataProvider provider) {
        addProvider(provider);
        histDataProviderMap.put(provider.providerId(), provider);
    }

    public void addRealTimeDataProvider(RealTimeDataProvider provider){
        addProvider(provider);
        rtDataProviderMap.put(provider.providerId(), provider);
    }

    public void addExecutionProvider(ExecutionProvider provider){
        addProvider(provider);
        executionProviderMap.put(provider.providerId(), provider);
    }

    public void addDataStoreProvider(DataStoreProvider provider){
        providerMap.put(provider.providerId(), provider);
        dataStoreProviderMap.put(provider.providerId(), provider);
    }

    public RealTimeDataProvider getRealTimeDataProvider(ProviderId providerId){
        return rtDataProviderMap.get(providerId);
    }

    public HistoricalDataProvider getHistoricalDataProvider(ProviderId providerId){
        return histDataProviderMap.get(providerId);
    }

    public ExecutionProvider getExecutionProvider(ProviderId providerId){
        return executionProviderMap.get(providerId);
    }

    public DataStoreProvider getDataStoreProvider(ProviderId providerId){
        return dataStoreProviderMap.get(providerId);
    }


    public RealTimeDataProvider getRealTimeDataProvider(int providerId){
        return rtDataProviderMap.get(ProviderId.get(providerId));
    }

    public HistoricalDataProvider getHistoricalDataProvider(int providerId){
        return histDataProviderMap.get(ProviderId.get(providerId));
    }

    public ExecutionProvider getExecutionProvider(int providerId){
        return executionProviderMap.get(ProviderId.get(providerId));
    }

    public DataStoreProvider getDataStoreProvider(int providerId){
        return dataStoreProviderMap.get(ProviderId.get(providerId));
    }

    public Provider getProvider(int id){
        return providerIdMap.get(id);
    }

    public Provider getProvider(ProviderId providerId){
        return providerMap.get(providerId);
    }
}
