package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class RetrieveAllOpenOrderRequestSerializer extends Serializer{

    private static final int VERSION = 1;
    public RetrieveAllOpenOrderRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion);
    }

    public byte [] serialize(){

        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.append(OutgoingMessageId.RETRIEVE_ALL_OPEN_ORDER_REQUEST.getId());
        builder.append(VERSION);
        return builder.toBytes();
    }

}
