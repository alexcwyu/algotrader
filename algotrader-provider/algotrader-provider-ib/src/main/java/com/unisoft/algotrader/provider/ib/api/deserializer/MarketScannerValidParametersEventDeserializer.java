package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class MarketScannerValidParametersEventDeserializer extends Deserializer {


    public MarketScannerValidParametersEventDeserializer(){
        super(IncomingMessageId.MARKET_SCANNER_VALID_PARAMETERS);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBEventHandler eventHandler) {
        final String xml = readString(inputStream);

        eventHandler.onMarketScannerValidParametersEvent(xml);
    }
}