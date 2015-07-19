package com.unisoft.algotrader.config;

import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.provider.cassandra.CassandraHistoricalDataStore;
import com.unisoft.algotrader.provider.csv.CSVHistoricalDataStore;
import com.unisoft.algotrader.provider.google.GoogleHistoricalDataProvider;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.influxdb.InfluxDBHistoricalDataStore;
import com.unisoft.algotrader.provider.kdb.KDBHistoricalDataStore;
import com.unisoft.algotrader.provider.yahoo.YahooHistoricalDataProvider;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by alex on 7/19/15.
 */
public class ProviderManagerProvider implements Provider<ProviderManager> {

    private IBProvider ibProvider;
    private CassandraHistoricalDataStore cassandraHistoricalDataStore;
    private CSVHistoricalDataStore csvHistoricalDataStore;
    private KDBHistoricalDataStore kdbHistoricalDataStore;
    private InfluxDBHistoricalDataStore influxDBHistoricalDataStore;
    private YahooHistoricalDataProvider yahooHistoricalDataProvider;
    private GoogleHistoricalDataProvider googleHistoricalDataProvider;
    //private SimulationExecutor simulationExecutor;

    @Inject
    public ProviderManagerProvider(
            IBProvider ibProvider,
            CassandraHistoricalDataStore cassandraHistoricalDataStore,
            CSVHistoricalDataStore csvHistoricalDataStore,
            KDBHistoricalDataStore kdbHistoricalDataStore,
            InfluxDBHistoricalDataStore influxDBHistoricalDataStore,
            YahooHistoricalDataProvider yahooHistoricalDataProvider,
            GoogleHistoricalDataProvider googleHistoricalDataProvider
            //SimulationExecutor simulationExecutor

            ) {
        this.ibProvider = ibProvider;
        this.cassandraHistoricalDataStore = cassandraHistoricalDataStore;
        this.csvHistoricalDataStore = csvHistoricalDataStore;
        this.kdbHistoricalDataStore = kdbHistoricalDataStore;
        this.influxDBHistoricalDataStore = influxDBHistoricalDataStore;
        this.yahooHistoricalDataProvider = yahooHistoricalDataProvider;
        this.googleHistoricalDataProvider = googleHistoricalDataProvider;
        //this.simulationExecutor = simulationExecutor;
    }


    public ProviderManager get(){
        ProviderManager providerManager = new ProviderManager();
//
//        providerManager.addRealTimeDataProvider(ibProvider);
//
//        providerManager.addDataStoreProvider(cassandraHistoricalDataStore);
//        providerManager.addDataStoreProvider(csvHistoricalDataStore);
//        providerManager.addDataStoreProvider(kdbHistoricalDataStore);
//        providerManager.addDataStoreProvider(influxDBHistoricalDataStore);
//
//        providerManager.addExecutionProvider(ibProvider);
//       // providerManager.addExecutionProvider(simulationExecutor);
//
//        providerManager.addHistoricalDataProvider(ibProvider);
//        providerManager.addHistoricalDataProvider(cassandraHistoricalDataStore);
//        providerManager.addHistoricalDataProvider(csvHistoricalDataStore);
//        providerManager.addHistoricalDataProvider(kdbHistoricalDataStore);
//        providerManager.addHistoricalDataProvider(influxDBHistoricalDataStore);
//        providerManager.addHistoricalDataProvider(yahooHistoricalDataProvider);
//        providerManager.addHistoricalDataProvider(googleHistoricalDataProvider);

        return providerManager;
    }
}
