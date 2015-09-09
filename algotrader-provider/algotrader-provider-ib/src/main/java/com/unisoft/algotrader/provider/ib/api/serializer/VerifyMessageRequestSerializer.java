package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class VerifyMessageRequestSerializer extends Serializer{

    private static final int VERSION = 1;

    public VerifyMessageRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion);
    }

    public byte [] serialize(String apiData){

        ByteArrayBuilder builder = getByteArrayBuilder();

        builder.append(OutgoingMessageId.VERIFY_MESSAGE_REQUEST.getId());
        builder.append(VERSION);
        builder.append(apiData);

        return builder.toBytes();
    }

}
