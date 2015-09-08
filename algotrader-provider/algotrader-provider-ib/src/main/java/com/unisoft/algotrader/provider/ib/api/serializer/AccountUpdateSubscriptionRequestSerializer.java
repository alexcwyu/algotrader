package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class AccountUpdateSubscriptionRequestSerializer extends Serializer{

    private static final int VERSION = 2;

    public AccountUpdateSubscriptionRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion);
    }

    public byte [] serialize(String accountName){

        ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.append(OutgoingMessageId.ACCOUNT_UPDATE_SUBSCRIPTION_REQUEST.getId());
        builder.append(VERSION);
        builder.append(true);
        builder.append(accountName);

        return builder.toBytes();
    }

}
