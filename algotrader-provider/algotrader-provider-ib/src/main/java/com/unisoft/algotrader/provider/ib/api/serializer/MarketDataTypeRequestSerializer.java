package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.exception.RequestException;
import com.unisoft.algotrader.provider.ib.api.model.data.MarketDataType;
import com.unisoft.algotrader.provider.ib.api.model.system.ClientMessageCode;
import com.unisoft.algotrader.provider.ib.api.model.system.Feature;
import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class MarketDataTypeRequestSerializer extends Serializer{

    private static final int VERSION = 1;
    public MarketDataTypeRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion, OutgoingMessageId.MARKET_DATA_TYPE_REQUEST);
    }

    public byte [] serialize(MarketDataType type){
        checkMarketDataTypeRequest();

        ByteArrayBuilder builder = getByteArrayBuilder();
        builder.append(messageId.getId());
        builder.append(VERSION);
        builder.append(type.getValue());

        return builder.toBytes();
    }


    private void checkMarketDataTypeRequest() {
        if (!Feature.MARKET_DATA_TYPE.isSupportedByVersion(getServerCurrentVersion())) {
            throw new RequestException(ClientMessageCode.UPDATE_TWS, "It does not support marketDataType requests.");
        }
    }
}
