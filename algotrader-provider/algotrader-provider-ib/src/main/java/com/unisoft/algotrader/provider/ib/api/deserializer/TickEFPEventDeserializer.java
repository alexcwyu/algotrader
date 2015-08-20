package com.unisoft.algotrader.provider.ib.api.deserializer;

import ch.aonyx.broker.ib.api.util.InputStreamUtils;
import com.unisoft.algotrader.provider.ib.api.IBConstants;
import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static ch.aonyx.broker.ib.api.util.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class TickEFPEventDeserializer extends Deserializer {


    public TickEFPEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.TICK_EXCHANFE_FOR_PHYSICAL, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final int requestId = readInt(inputStream);
        final int tickType = readInt(inputStream);
        final double basisPoints = readDouble(inputStream);
        final String formattedBasisPoints = readString(inputStream);
        final double impliedFuturePrice = readDouble(inputStream);
        final int holdDays = readInt(inputStream);
        final String futureExpiry = readString(inputStream);
        final double dividendImpact = readDouble(inputStream);
        final double dividendToExpiry = readDouble(inputStream);
        
        ibSession.onTickEfpEvent(requestId, IBConstants.TickType.fromValue(tickType),  basisPoints,
        formattedBasisPoints, impliedFuturePrice, holdDays,futureExpiry, dividendImpact, dividendToExpiry);
    }
}