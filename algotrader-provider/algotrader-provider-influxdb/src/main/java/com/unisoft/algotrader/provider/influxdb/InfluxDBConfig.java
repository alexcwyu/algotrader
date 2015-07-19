package com.unisoft.algotrader.provider.influxdb;


import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by alex on 7/20/15.
 */
public class InfluxDBConfig {
    public final String host;
    public final int port;
    public final String user;
    public final String password;

    @Inject
    public InfluxDBConfig(@Named("influxdb.host") String host, @Named("influxdb.port") int port,
                          @Nullable @Named("influxdb.user") String user, @Nullable @Named("influxdb.password") String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }
}
