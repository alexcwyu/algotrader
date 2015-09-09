package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class DisplayGroupUpdateRequestSerializer extends Serializer{

    private static final int VERSION = 1;

    public DisplayGroupUpdateRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion);
    }

    public byte [] serialize(long requestId, String contractInfo){

        ByteArrayBuilder builder = getByteArrayBuilder();

        builder.append(OutgoingMessageId.DISPLAY_GROUP_UPDATE_REQUEST.getId());
        builder.append(VERSION);
        builder.append(requestId);
        builder.append(contractInfo);

        return builder.toBytes();
    }

}
