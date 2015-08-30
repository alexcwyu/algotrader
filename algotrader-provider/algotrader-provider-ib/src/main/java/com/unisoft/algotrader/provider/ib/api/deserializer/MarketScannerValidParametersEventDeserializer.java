package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.constants.IncomingMessageId;

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
    public void consumeVersionLess(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        final String xml = readString(inputStream);

        ibProvider.onMarketScannerValidParametersEvent(xml);
    }
}