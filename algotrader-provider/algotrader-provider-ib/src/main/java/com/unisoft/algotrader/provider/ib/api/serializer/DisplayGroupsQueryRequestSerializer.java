package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class DisplayGroupsQueryRequestSerializer extends Serializer{

    private static final int VERSION = 1;

    public DisplayGroupsQueryRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion, OutgoingMessageId.DISPLAY_GROUP_QUERY_REQUEST);
    }

    public byte [] serialize(long requestId){

        ByteArrayBuilder builder = getByteArrayBuilder();

        builder.append(messageId.getId());
        builder.append(VERSION);
        builder.append(requestId);

        return builder.toBytes();
    }

}
