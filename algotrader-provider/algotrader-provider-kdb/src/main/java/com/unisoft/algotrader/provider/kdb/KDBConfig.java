package com.unisoft.algotrader.provider.kdb;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by alex on 6/23/15.
 */
public class KDBConfig {
    public String host = "127.0.0.1";
    public int port = 5000;
    public String user;
    public String password;

    @Inject
    public KDBConfig(@Named("kdb.host") String host, @Named("kdb.port") int port,
                     @Nullable @Named("kdb.user") String user, @Nullable @Named("kdb.password") String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }
}
