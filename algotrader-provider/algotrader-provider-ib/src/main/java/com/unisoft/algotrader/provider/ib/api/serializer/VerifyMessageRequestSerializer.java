package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class VerifyMessageRequestSerializer extends Serializer{

    private static final int VERSION = 1;

    public VerifyMessageRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion, OutgoingMessageId.VERIFY_MESSAGE_REQUEST);
    }

    public byte [] serialize(String apiData){

        ByteArrayBuilder builder = getByteArrayBuilder();

        builder.append(messageId.getId());
        builder.append(VERSION);
        builder.append(apiData);

        return builder.toBytes();
    }

}
