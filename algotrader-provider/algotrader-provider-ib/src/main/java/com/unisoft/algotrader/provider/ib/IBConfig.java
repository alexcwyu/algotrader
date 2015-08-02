package com.unisoft.algotrader.provider.ib;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by alex on 7/20/15.
 */
public class IBConfig {
    public final String host;
    public final int port;
    public final int clientId;
    public final String user;
    public final String password;
    public final int currentVersion;
    public final int minVersion;

    @Inject
    public IBConfig(@Named("ib.host") String host,
                    @Named("ib.port") int port,
                    @Named("ib.clientId") int clientId,
                    @Named("ib.user") String user,
                    @Named("ib.password") String password,
                    @Named("ib.client.current.version") int currentVersion,
                    @Named("ib.client.min.version") int minVersion) {
        this.host = host;
        this.port = port;
        this.clientId = clientId;
        this.user = user;
        this.password = password;
        this.currentVersion = currentVersion;
        this.minVersion = minVersion;
    }
}
