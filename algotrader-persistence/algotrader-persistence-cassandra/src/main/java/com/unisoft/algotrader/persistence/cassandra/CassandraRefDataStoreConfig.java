package com.unisoft.algotrader.persistence.cassandra;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by alex on 6/18/15.
 */
public class CassandraRefDataStoreConfig {

    public String host = "127.0.0.1";
    public int port;
    public String keyspace = "refdata";
    public String user;
    public String password;

    @Inject
    public CassandraRefDataStoreConfig(@Named("cassandra.refdata.host") String host,
                                       @Named("cassandra.refdata.port") int port,
                                       @Named("cassandra.refdata.keyspace") String keyspace,
                                       @Named("cassandra.refdata.user") String user,
                                       @Named("cassandra.refdata.password") String password) {
        this.host = host;
        this.port = port;
        this.keyspace = keyspace;
        this.user = user;
        this.password = password;
    }
}
