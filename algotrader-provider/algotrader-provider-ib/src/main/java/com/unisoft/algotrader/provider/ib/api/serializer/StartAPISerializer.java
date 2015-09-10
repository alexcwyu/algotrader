package com.unisoft.algotrader.provider.ib.api.serializer;


import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/11/15.
 */
public class StartAPISerializer extends Serializer{

    private static final int VERSION = 1;

    public StartAPISerializer(int serverCurrentVersion) {
        super(serverCurrentVersion);
    }

    public byte[] serialize(int clientId) {
        ByteArrayBuilder builder = getByteArrayBuilder();

        builder.append(OutgoingMessageId.START_API_REQUEST.getId());
        builder.append(VERSION);
        builder.append(clientId);
        return builder.toBytes();
    }
}
