package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readInt;
import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class MarketScannerValidParametersEventDeserializer extends Deserializer {


    public MarketScannerValidParametersEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.MARKET_SCANNER_VALID_PARAMETERS, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final String xml = readString(inputStream);

        ibSession.onMarketScannerValidParameters(xml);
    }
}