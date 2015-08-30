package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.constants.IncomingMessageId;
import com.unisoft.algotrader.provider.ib.api.model.constants.MarketDataType;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readInt;

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
        final MarketDataType marketDataType = MarketDataType.fromValue(readInt(inputStream));

        ibProvider.onMarketDataTypeEvent(requestId, marketDataType);
    }
}