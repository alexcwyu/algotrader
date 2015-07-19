package com.unisoft.algotrader.provider.cassandra;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by alex on 6/18/15.
 */
public class CassandraHistoricalDataStoreConfig {

    public String host = "127.0.0.1";
    public int port;
    public String keyspace = "marketdata";
    public String user;
    public String password;

    @Inject
    public CassandraHistoricalDataStoreConfig(@Named("cassandra.marketdata.host") String host,
                                              @Named("cassandra.marketdata.port") int port,
                                              @Named("cassandra.marketdata.keyspace") String keyspace,
                                              @Named("cassandra.marketdata.user") String user,
                                              @Named("cassandra.marketdata.password") String password) {
        this.host = host;
        this.port = port;
        this.keyspace = keyspace;
        this.user = user;
        this.password = password;
    }
}
