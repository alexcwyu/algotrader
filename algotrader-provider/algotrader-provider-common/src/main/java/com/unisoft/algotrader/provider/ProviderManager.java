package com.unisoft.algotrader.provider;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.provider.execution.ExecutionProvider;
import com.unisoft.algotrader.provider.historical.HistoricalDataProvider;
import com.unisoft.algotrader.provider.realtime.RealTimeDataProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;


/**
 * Created by alex on 5/19/15.
 */
@Singleton
public class ProviderManager {


    public static final ProviderManager INSTANCE;

    static {
        INSTANCE = new ProviderManager();
    }

    @Inject
    ProviderManager(){

    }

    private Map<String, DataStore> dataImporterMap = Maps.newConcurrentMap();
    private Map<String, ExecutionProvider> executionProviderMap = Maps.newConcurrentMap();
    private Map<String, HistoricalDataProvider> histDataProviderMap = Maps.newConcurrentMap();
    private Map<String, RealTimeDataProvider> rtDataProviderMap = Maps.newConcurrentMap();

    public void registerExecutionProvider(ExecutionProvider provider){
        executionProviderMap.put(provider.providerId(), provider);
    }

    public void addHistoricalDataProvider(HistoricalDataProvider provider){
        histDataProviderMap.put(provider.providerId(), provider);
    }

    public void addRealTimeDataProvider(RealTimeDataProvider provider){
        rtDataProviderMap.put(provider.providerId(), provider);
    }

    public void addExecutionProvider(ExecutionProvider provider){
        executionProviderMap.put(provider.providerId(), provider);
    }

    public void addDataImporter(DataStore provider){
        dataImporterMap.put(provider.providerId(), provider);
    }

    public RealTimeDataProvider getRealTimeDataProvider(String providerId){
        return rtDataProviderMap.get(providerId);
    }

    public HistoricalDataProvider getHistoricalDataProvider(String providerId){
        return histDataProviderMap.get(providerId);
    }

    public ExecutionProvider getExecutionProvider(String providerId){
        return executionProviderMap.get(providerId);
    }

    public DataStore getDataImporter(String providerId){
        return dataImporterMap.get(providerId);
    }
}
