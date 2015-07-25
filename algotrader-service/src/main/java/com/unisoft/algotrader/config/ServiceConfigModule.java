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

        //DataStoreProvider
        bind(CassandraHistoricalDataStore.class).asEagerSingleton();
        bind(CSVHistoricalDataStore.class).asEagerSingleton();
        bind(KDBHistoricalDataStore.class).asEagerSingleton();
        bind(InfluxDBHistoricalDataStore.class).asEagerSingleton();

        //ExecutionProvider
        bind(IBProvider.class).asEagerSingleton();
        bind(SimulationExecutor.class).toProvider(SimulationExecutorProvider.class).asEagerSingleton();

        //HistoricalDataProvider
        bind(YahooHistoricalDataProvider.class).asEagerSingleton();
        bind(GoogleHistoricalDataProvider.class).asEagerSingleton();
    }

    public static void main(String [] args){

        Injector injector = Guice.createInjector(new AppConfigModule(), new ServiceConfigModule(), new DataServiceConfigModule());
        ProviderManager providerManager = injector.getInstance(ProviderManager.class);

        System.out.print(providerManager);

    }
}
