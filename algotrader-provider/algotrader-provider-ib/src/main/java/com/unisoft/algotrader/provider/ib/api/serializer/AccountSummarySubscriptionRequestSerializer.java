package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class AccountSummarySubscriptionRequestSerializer extends Serializer{

    private static final int VERSION = 1;

    public AccountSummarySubscriptionRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion);
    }

    public byte [] serialize(long requestId, String group, String tags){

        ByteArrayBuilder builder = getByteArrayBuilder();

        builder.append(OutgoingMessageId.ACCOUNT_SUMMARY_SUBSCRIPTION_REQUEST.getId());
        builder.append(VERSION);
        builder.append(requestId);
        builder.append(group);
        builder.append(tags);

        return builder.toBytes();
    }

}
