package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.IBConstants;
import com.unisoft.algotrader.provider.ib.api.IBSocket;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readInt;

/**
 * Created by alex on 8/13/15.
 */
public class MarketDataTypeEventDeserializer extends Deserializer {


    public MarketDataTypeEventDeserializer(){
        super(IncomingMessageId.MARKET_DATA_TYPE);
    }

    @Override
    public void consumeVersionLess(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        final int requestId = readInt(inputStream);
        final IBConstants.MarketDataType marketDataType = IBConstants.MarketDataType.fromValue(readInt(inputStream));

        ibProvider.onMarketDataTypeEvent(requestId, marketDataType);
    }
}