package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class AccountSummaryUnsubscriptionRequestSerializer extends Serializer{

    private static final int VERSION = 1;

    public AccountSummaryUnsubscriptionRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion, OutgoingMessageId.ACCOUNT_SUMMARY_UNSUBSCRIPTION_REQUEST);
    }

    public byte [] serialize(long requestId){

        ByteArrayBuilder builder = getByteArrayBuilder();

        builder.append(messageId.getId());
        builder.append(VERSION);
        builder.append(requestId);

        return builder.toBytes();
    }

}
