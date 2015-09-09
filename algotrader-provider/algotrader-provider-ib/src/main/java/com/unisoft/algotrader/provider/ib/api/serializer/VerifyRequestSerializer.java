package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class VerifyRequestSerializer extends Serializer{

    private static final int VERSION = 1;

    public VerifyRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion);
    }

    public byte [] serialize(String apiName, String apiVersion){

        ByteArrayBuilder builder = getByteArrayBuilder();

        builder.append(OutgoingMessageId.VERIFY_REQUEST.getId());
        builder.append(VERSION);
        builder.append(apiName);
        builder.append(apiVersion);

        return builder.toBytes();
    }

}
