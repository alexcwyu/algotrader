package com.unisoft.algotrader.provider.ib.api.serializer;


import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/11/15.
 */
public class CancelOrderSerializer extends Serializer{

    private static final int VERSION = 1;

    public CancelOrderSerializer(int serverCurrentVersion) {
        super(serverCurrentVersion);
    }

    public byte[] serialize(long orderId) {
        ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.append(OutgoingMessageId.CANCEL_ORDER_REQUEST.getId());
        builder.append(VERSION);
        builder.append(orderId);
        return builder.toBytes();
    }
}
