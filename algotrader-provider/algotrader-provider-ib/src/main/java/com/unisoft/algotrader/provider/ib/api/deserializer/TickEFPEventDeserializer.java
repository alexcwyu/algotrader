package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.model.data.TickType;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.*;


/**
 * Created by alex on 8/13/15.
 */
public class TickEFPEventDeserializer extends Deserializer {


    public TickEFPEventDeserializer(){
        super(IncomingMessageId.TICK_EXCHANGE_FOR_PHYSICAL);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBEventHandler eventHandler) {
        final int requestId = readInt(inputStream);
        final int tickType = readInt(inputStream);
        final double basisPoints = readDouble(inputStream);
        final String formattedBasisPoints = readString(inputStream);
        final double impliedFuturePrice = readDouble(inputStream);
        final int holdDays = readInt(inputStream);
        final String futureExpiry = readString(inputStream);
        final double dividendImpact = readDouble(inputStream);
        final double dividendToExpiry = readDouble(inputStream);

        eventHandler.onTickEfpEvent(requestId, TickType.fromValue(tickType),  basisPoints,
        formattedBasisPoints, impliedFuturePrice, holdDays,futureExpiry, dividendImpact, dividendToExpiry);
    }
}