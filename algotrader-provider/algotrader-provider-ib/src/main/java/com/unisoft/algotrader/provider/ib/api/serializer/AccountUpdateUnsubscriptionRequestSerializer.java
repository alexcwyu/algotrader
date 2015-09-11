package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class AccountUpdateUnsubscriptionRequestSerializer extends Serializer{

    private static final int VERSION = 2;

    public AccountUpdateUnsubscriptionRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion, OutgoingMessageId.ACCOUNT_UPDATE_SUBSCRIPTION_REQUEST);
    }

    public byte [] serialize(String accountName){

        ByteArrayBuilder builder = getByteArrayBuilder();

        builder.append(messageId.getId());
        builder.append(VERSION);
        builder.append(false);
        builder.append(accountName);

        return builder.toBytes();
    }

}
