package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.event.MarketDepthLevelTwoUpdateEvent;
import com.unisoft.algotrader.provider.ib.api.model.data.BookSide;
import com.unisoft.algotrader.provider.ib.api.model.data.Operation;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class MarketDepthLevelTwoUpdateEventDeserializer extends Deserializer<MarketDepthLevelTwoUpdateEvent> {


    public MarketDepthLevelTwoUpdateEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.MARKET_DEPTH_LEVEL_TWO_UPDATE, serverCurrentVersion);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBEventHandler eventHandler) {
        final int requestId = readInt(inputStream);
        final int rowId = readInt(inputStream);
        final String marketMakerName = readString(inputStream);
        final int operation = readInt(inputStream);
        final int bookSide = readInt(inputStream);
        final double price = readDouble(inputStream);
        final int size = readInt(inputStream);

        eventHandler.onMarketDepthLevelTwoUpdateEvent(requestId, rowId, marketMakerName, Operation.fromValue(operation),
                BookSide.fromValue(bookSide), price, size);
    }
}