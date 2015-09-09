package com.unisoft.algotrader.provider.ib.api.serializer;

/**
 * Created by alex on 8/2/15.
 */
public abstract class Serializer{

    private static final ThreadLocal<ByteArrayBuilder> BUFFER_BUILDER = ThreadLocal.withInitial(ByteArrayBuilder::new);

    protected static ByteArrayBuilder getByteArrayBuilder(){
        ByteArrayBuilder builder = BUFFER_BUILDER.get();
        builder.clear();
        return builder;
    }

    protected int serverCurrentVersion;

    protected Serializer(int serverCurrentVersion) {
        this.serverCurrentVersion = serverCurrentVersion;
    }

    protected int getServerCurrentVersion(){
        return serverCurrentVersion;
    }

}
