package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class GroupEventsSubscriptionRequestSerializer extends Serializer{

    private static final int VERSION = 1;

    public GroupEventsSubscriptionRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion);
    }

    public byte [] serialize(long requestId, int groupId){

        ByteArrayBuilder builder = getByteArrayBuilder();

        builder.append(OutgoingMessageId.GROUP_EVENTS_SUBSCRIPTION_REQUEST.getId());
        builder.append(VERSION);
        builder.append(requestId);
        builder.append(groupId);

        return builder.toBytes();
    }

}
