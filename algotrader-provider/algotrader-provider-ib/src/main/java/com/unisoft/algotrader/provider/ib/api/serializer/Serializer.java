package com.unisoft.algotrader.provider.ib.api.serializer;

/**
 * Created by alex on 8/2/15.
 */
public abstract class Serializer{

    protected int serverCurrentVersion;

    protected Serializer(int serverCurrentVersion) {
        this.serverCurrentVersion = serverCurrentVersion;
    }

    protected int getServerCurrentVersion(){
        return serverCurrentVersion;
    }

}
