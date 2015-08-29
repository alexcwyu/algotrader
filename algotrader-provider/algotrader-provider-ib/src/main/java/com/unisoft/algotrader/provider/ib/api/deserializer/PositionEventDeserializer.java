package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.IBConstants;
import com.unisoft.algotrader.provider.ib.api.IBSocket;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class PositionEventDeserializer extends Deserializer {


    public PositionEventDeserializer(){
        super(IncomingMessageId.POSITION);
    }

    @Override
    public void consumeVersionLess(final int version, final InputStream inputStream, final IBProvider ibProvider) {

        final String account = readString(inputStream);

        Instrument instrument = parseInstrument(version, inputStream, ibProvider.getRefDataStore());

        final int pos = readInt(inputStream);
        final double avgCost =(version >= 3)? readDouble(inputStream) : 0.0;

        ibProvider.onPositionEvent(account, instrument, pos, avgCost);
    }


    protected Instrument parseInstrument(final int version, final InputStream inputStream, final RefDataStore refDataStore) {

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
        final String tradingClass = (version >= 2)? readString(inputStream): null;

        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange(IBProvider.PROVIDER_ID, symbol, exchange);
        if (instrument == null){
            throw new RuntimeException("Cannot find instrumnet symbol=" + symbol +", primaryExchange="+exchange);
        }

        return instrument;
    }
}