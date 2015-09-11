package com.unisoft.algotrader.provider.ib.api.serializer;


import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/11/15.
 */
public class PositionsRequestSerializer extends Serializer{

    private static final int VERSION = 1;

    public PositionsRequestSerializer(int serverCurrentVersion) {
        super(serverCurrentVersion, OutgoingMessageId.POSITIONS_REQUEST);
    }

    public byte[] serialize(long orderId) {
        ByteArrayBuilder builder = getByteArrayBuilder();

        builder.append(messageId.getId());
        builder.append(VERSION);
        return builder.toBytes();
    }
}
