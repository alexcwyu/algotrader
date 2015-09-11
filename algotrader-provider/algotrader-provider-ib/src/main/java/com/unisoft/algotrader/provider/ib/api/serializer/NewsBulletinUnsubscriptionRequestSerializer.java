package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class NewsBulletinUnsubscriptionRequestSerializer extends Serializer{

    private static final int VERSION = 1;
    public NewsBulletinUnsubscriptionRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion, OutgoingMessageId.NEWS_BULLETIN_UNSUBSCRIPTION_REQUEST);
    }

    public byte [] serialize(){
        ByteArrayBuilder builder = getByteArrayBuilder();

        builder.append(messageId.getId());
        builder.append(VERSION);
        return builder.toBytes();
    }
}
