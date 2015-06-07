package com.unisoft.algotrader.provider;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.provider.data.historical.CSVHistoricalDataProvider;
import com.unisoft.algotrader.provider.data.historical.HistoricalDataProvider;
import com.unisoft.algotrader.provider.data.realtime.RealTimeDataProvider;
import com.unisoft.algotrader.provider.execution.ExecutionProvider;

import java.util.Map;

/**
 * Created by alex on 5/19/15.
 */
public class ProviderManager {


    public static final ProviderManager INSTANCE;

    static {
        INSTANCE = new ProviderManager();

        //INSTANCE.registerExecutionProvider(new SimulationExecutor());
        INSTANCE.registerHistoricalDataProvider(new CSVHistoricalDataProvider("/mnt/data/dev/workspaces/algo/algotrader3/src/main/resources/"));
    }


    private ProviderManager(){

    }

    private Map<String, ExecutionProvider> executionProviderMap = Maps.newConcurrentMap();
    private Map<String, HistoricalDataProvider> histDataProviderMap = Maps.newConcurrentMap();
    private Map<String, RealTimeDataProvider> rtDataProviderMap = Maps.newConcurrentMap();

    public void registerExecutionProvider(ExecutionProvider provider){
        executionProviderMap.put(provider.providerId(), provider);
    }

    public ExecutionProvider getExecutionProvider(String providerId){
        return executionProviderMap.get(providerId);
    }

    public void registerHistoricalDataProvider(HistoricalDataProvider provider){
        histDataProviderMap.put(provider.providerId(), provider);
    }

    public HistoricalDataProvider getHistoricalDataProvider(String providerId){
        return histDataProviderMap.get(providerId);
    }

    public void registerRealTimeDataProvider(RealTimeDataProvider provider){
        rtDataProviderMap.put(provider.providerId(), provider);
    }

    public RealTimeDataProvider getRealTimeDataProvider(String providerId){
        return rtDataProviderMap.get(providerId);
    }
}
