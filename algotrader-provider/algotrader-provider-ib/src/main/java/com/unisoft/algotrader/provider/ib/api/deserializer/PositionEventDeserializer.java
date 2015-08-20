package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.provider.ib.api.IBConstants;
import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;
import com.unisoft.algotrader.provider.ib.api.model.CommissionReport;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class PositionEventDeserializer extends Deserializer {


    public PositionEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.POSITION, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final int instId =readInt(inputStream);
        final String symbol = readString(inputStream);
        final Instrument.InstType instType = IBConstants.SecType.convert(readString(inputStream));
        final String expString = readString(inputStream);
        final double strike = readDouble(inputStream);
        final Instrument.PutCall putCall = IBConstants.OptionRight.convert(readString(inputStream));
        final String multiplier = readString(inputStream);
        final String exchange = readString(inputStream);
        final String ccyCode = readString(inputStream);
        final String localSymbol = readString(inputStream);
        final String tradingClass = (getVersion() >= 2)? readString(inputStream): null;


        final int field1 = readInt(inputStream);
        final double field2 =(getVersion() >= 3)? readDouble(inputStream) : 0.0;

        ibSession.onPosition();
    }
}