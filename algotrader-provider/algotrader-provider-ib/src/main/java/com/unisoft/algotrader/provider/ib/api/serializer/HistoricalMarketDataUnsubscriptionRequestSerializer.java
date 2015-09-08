package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class HistoricalMarketDataUnsubscriptionRequestSerializer extends Serializer{

    private static final int VERSION = 1;

    public HistoricalMarketDataUnsubscriptionRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion);
    }

    public byte [] serialize(long requestId){
        ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.append(OutgoingMessageId.HISTORICAL_DATA_UNSUBSCRIPTION_REQUEST.getId());
        builder.append(VERSION);
        builder.append(requestId);
        return builder.toBytes();
    }

}
