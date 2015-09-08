package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class MarketDepthUnsubscriptionRequestSerializer extends Serializer{

    private static final int VERSION = 1;

    public MarketDepthUnsubscriptionRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion);
    }

    public byte [] serialize(long requestId){
        ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.append(OutgoingMessageId.MARKET_DEPTH_UNSUBSCRIPTION_REQUEST.getId());
        builder.append(VERSION);
        builder.append(requestId);

        return builder.toBytes();
    }

}
