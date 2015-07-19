package com.unisoft.algotrader.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.provider.cassandra.CassandraHistoricalDataStore;
import com.unisoft.algotrader.provider.config.DataServiceConfigModule;
import com.unisoft.algotrader.provider.csv.CSVHistoricalDataStore;
import com.unisoft.algotrader.provider.execution.simulation.SimulationExecutor;
import com.unisoft.algotrader.provider.google.GoogleHistoricalDataProvider;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.influxdb.InfluxDBHistoricalDataStore;
import com.unisoft.algotrader.provider.kdb.KDBHistoricalDataStore;
import com.unisoft.algotrader.provider.yahoo.YahooHistoricalDataProvider;
import com.unisoft.algotrader.utils.config.BaseConfigModule;

/**
 * Created by alex on 7/20/15.
 */
public class ServiceConfigModule extends BaseConfigModule {

    protected void configure() {
        super.configure();
        //bind(CSVConfig.class);
        bind(SimulationExecutor.class).toProvider(SimulationExecutorProvider.class);
        //bind(ProviderManager.class).toProvider(ProviderManagerProvider.class);
    }

    public static void main(String [] args){

        Injector injector = Guice.createInjector(new AppConfigModule(), new ServiceConfigModule(), new DataServiceConfigModule());
        ProviderManager providerManager = injector.getInstance(ProviderManager.class);


        providerManager.addRealTimeDataProvider(injector.getInstance(IBProvider.class));

        providerManager.addDataStoreProvider(injector.getInstance(CassandraHistoricalDataStore.class));
        providerManager.addDataStoreProvider(injector.getInstance(CSVHistoricalDataStore.class));
        providerManager.addDataStoreProvider(injector.getInstance(KDBHistoricalDataStore.class));
        providerManager.addDataStoreProvider(injector.getInstance(InfluxDBHistoricalDataStore.class));

        providerManager.addExecutionProvider(injector.getInstance(IBProvider.class));
        providerManager.addExecutionProvider(injector.getInstance(SimulationExecutor.class));

        providerManager.addHistoricalDataProvider(injector.getInstance(IBProvider.class));
        providerManager.addHistoricalDataProvider(injector.getInstance(CassandraHistoricalDataStore.class));
        providerManager.addHistoricalDataProvider(injector.getInstance(CSVHistoricalDataStore.class));
        providerManager.addHistoricalDataProvider(injector.getInstance(KDBHistoricalDataStore.class));
        providerManager.addHistoricalDataProvider(injector.getInstance(InfluxDBHistoricalDataStore.class));
        providerManager.addHistoricalDataProvider(injector.getInstance(YahooHistoricalDataProvider.class));
        providerManager.addHistoricalDataProvider(injector.getInstance(GoogleHistoricalDataProvider.class));

        System.out.print(providerManager);

    }
}
