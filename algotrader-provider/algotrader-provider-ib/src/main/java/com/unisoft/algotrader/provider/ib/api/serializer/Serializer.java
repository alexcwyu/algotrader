package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

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

    protected final int serverCurrentVersion;

    protected final OutgoingMessageId messageId;

    protected Serializer(final int serverCurrentVersion, final OutgoingMessageId messageId) {
        this.serverCurrentVersion = serverCurrentVersion;
        this.messageId = messageId;
    }


    public OutgoingMessageId messageId(){
        return messageId;
    }

    protected int getServerCurrentVersion(){
        return serverCurrentVersion;
    }

}
