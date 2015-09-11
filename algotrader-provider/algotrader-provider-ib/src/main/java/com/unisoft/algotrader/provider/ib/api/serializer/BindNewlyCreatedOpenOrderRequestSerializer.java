package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class BindNewlyCreatedOpenOrderRequestSerializer extends Serializer{

    private static final int VERSION = 1;

    public BindNewlyCreatedOpenOrderRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion, OutgoingMessageId.BIND_NEWLY_CREATED_OPEN_ORDER_REQUEST);
    }

    public byte [] serialize(final boolean bindNewlyCreatedOrder){
        ByteArrayBuilder builder = getByteArrayBuilder();
        builder.append(messageId.getId());
        builder.append(VERSION);
        builder.append(bindNewlyCreatedOrder);
        return builder.toBytes();
    }
}
