package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class HistoricalMarketDataUnsubscriptionRequestSerializer extends Serializer<Long> {

    private static final int VERSION = 1;

    public HistoricalMarketDataUnsubscriptionRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion);
    }

    public byte [] serialize(Long id){
        ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.append(OutgoingMessageId.HISTORICAL_DATA_UNSUBSCRIPTION_REQUEST.getId());
        builder.append(VERSION);
        builder.append(id);
        return builder.toBytes();
    }

}
