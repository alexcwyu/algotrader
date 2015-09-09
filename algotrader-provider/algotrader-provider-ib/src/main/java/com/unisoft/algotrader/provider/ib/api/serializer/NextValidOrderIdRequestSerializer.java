package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class NextValidOrderIdRequestSerializer extends Serializer{

    private static final int VERSION = 1;
    public NextValidOrderIdRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion);
    }

    public byte [] serialize(){
        return  serialize(1);
    }

    public byte [] serialize(long suggestId){

        ByteArrayBuilder builder = getByteArrayBuilder();
        builder.append(OutgoingMessageId.NEXT_VALID_ORDER_ID_REQUEST.getId());
        builder.append(VERSION);
        builder.append(suggestId);
        return builder.toBytes();
    }

}
