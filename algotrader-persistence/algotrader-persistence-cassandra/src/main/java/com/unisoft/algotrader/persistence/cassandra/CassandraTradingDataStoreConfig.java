package com.unisoft.algotrader.persistence.cassandra;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by alex on 6/18/15.
 */
public class CassandraTradingDataStoreConfig {

    public String host = "127.0.0.1";
    public int port;
    public String keyspace = "trading";
    public String user;
    public String password;

    @Inject
    public CassandraTradingDataStoreConfig(@Named("cassandra.trading.host") String host,
                                           @Named("cassandra.trading.port") int port,
                                           @Named("cassandra.trading.keyspace") String keyspace,
                                           @Named("cassandra.trading.user") String user,
                                           @Named("cassandra.trading.password") String password) {
        this.host = host;
        this.port = port;
        this.keyspace = keyspace;
        this.user = user;
        this.password = password;
    }
}
