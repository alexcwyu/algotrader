package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.model.constants.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class RealTimeMarketDataUnsubscriptionRequestSerializer extends Serializer<Long> {

    private static final int VERSION = 1;
    public RealTimeMarketDataUnsubscriptionRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion);
    }

    public byte [] serialize(Long id){
        ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.append(OutgoingMessageId.REAL_TIME_BAR_UNSUBSCRIPTION_REQUEST.getId());
        builder.append(VERSION);
        builder.append(id);

        return builder.toBytes();
    }
}
