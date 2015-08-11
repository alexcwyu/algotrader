package com.unisoft.algotrader.provider.ib.api.serializer;

import ch.aonyx.broker.ib.api.OutgoingMessageId;

/**
 * Created by alex on 8/11/15.
 */
public class CancelOrderSerializer extends Serializer<Long> {

    private static final int VERSION = 1;

    public CancelOrderSerializer(int serverCurrentVersion) {
        super(serverCurrentVersion);
    }


    @Override
    public byte[] serialize(Long orderId) {
        ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.append(OutgoingMessageId.CANCEL_ORDER_REQUEST.getId());
        builder.append(VERSION);
        builder.append(orderId);
        return builder.toBytes();
    }
}
