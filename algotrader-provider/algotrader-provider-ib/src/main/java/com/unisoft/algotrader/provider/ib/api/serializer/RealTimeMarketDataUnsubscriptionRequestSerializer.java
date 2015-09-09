package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class RealTimeMarketDataUnsubscriptionRequestSerializer extends Serializer{

    private static final int VERSION = 1;
    public RealTimeMarketDataUnsubscriptionRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion);
    }

    public byte [] serialize(long requestId){
        ByteArrayBuilder builder = getByteArrayBuilder();

        builder.append(OutgoingMessageId.REAL_TIME_BAR_UNSUBSCRIPTION_REQUEST.getId());
        builder.append(VERSION);
        builder.append(requestId);

        return builder.toBytes();
    }
}
