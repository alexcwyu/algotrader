package com.unisoft.algotrader.provider.ib.api.serializer;


import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/11/15.
 */
public class StartAPISerializer extends Serializer{

    private static final int VERSION = 1;

    public StartAPISerializer(int serverCurrentVersion) {
        super(serverCurrentVersion, OutgoingMessageId.START_API_REQUEST);
    }

    public byte[] serialize(int clientId) {
        ByteArrayBuilder builder = getByteArrayBuilder();
        if (serverCurrentVersion < 70) {
            builder.append(clientId);
        }
        else {
            builder.append(messageId.getId());
            builder.append(VERSION);
            builder.append(clientId);
        }
        return builder.toBytes();
    }
}
