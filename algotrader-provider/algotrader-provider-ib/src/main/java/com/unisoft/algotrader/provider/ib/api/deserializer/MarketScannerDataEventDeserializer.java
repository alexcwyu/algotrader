package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.google.common.collect.Lists;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.IBConstants;
import com.unisoft.algotrader.provider.ib.api.IBSocket;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;
import com.unisoft.algotrader.provider.ib.api.InputStreamUtils;
import com.unisoft.algotrader.provider.ib.api.model.InstrumentSpecification;
import com.unisoft.algotrader.provider.ib.api.model.MarketScannerData;

import java.io.InputStream;
import java.util.List;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readInt;
import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class MarketScannerDataEventDeserializer extends Deserializer {

    public MarketScannerDataEventDeserializer(){
        super(IncomingMessageId.MARKET_SCANNER_DATA);
    }

    @Override
    public void consumeVersionLess(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        final int requestId = readInt(inputStream);
        final List<MarketScannerData> marketScannerDataEvents = Lists.newArrayList();
        final int marketScannerDatas = readInt(inputStream);
        for (int i = 0; i < marketScannerDatas; i++) {
            marketScannerDataEvents.add(consumeMarketScannerDataEvent(version, inputStream, ibProvider, requestId));
        }

        ibProvider.onMarketScannerDataListEvent(requestId, marketScannerDataEvents);
    }

    private MarketScannerData consumeMarketScannerDataEvent(final int version, final InputStream inputStream, final IBProvider ibProvider, final int requestId) {

        final InstrumentSpecification contractSpecification = new InstrumentSpecification();
        final int ranking = readInt(inputStream);
        int instid = 0;
        if (version >= 3) {
            instid = readInt(inputStream);
        }


        final String symbol = InputStreamUtils.readString(inputStream);
        final Instrument.InstType instType = IBConstants.SecType.convert(InputStreamUtils.readString(inputStream));
        final String expString = InputStreamUtils.readString(inputStream);
        final double strike = InputStreamUtils.readDouble(inputStream);
        final Instrument.PutCall putCall = IBConstants.OptionRight.convert(InputStreamUtils.readString(inputStream));
        final String exchange = InputStreamUtils.readString(inputStream);
        final String ccyCode = InputStreamUtils.readString(inputStream);
        final String localSymbol = InputStreamUtils.readString(inputStream);


        contractSpecification.setMarketName(readString(inputStream));
        contractSpecification.setTradingClass(readString(inputStream));
        final String distance = readString(inputStream);
        final String benchmark = readString(inputStream);
        final String projection = readString(inputStream);
        String comboLegDescription = null;
        if (version >= 2) {
            comboLegDescription = readString(inputStream);
        }

        Instrument instrument = ibProvider.getRefDataStore().getInstrumentBySymbolAndExchange(IBProvider.PROVIDER_ID, symbol, exchange);
        if (instrument == null){
            throw new RuntimeException("Cannot find instrumnet symbol=" + symbol +", primaryExchange="+exchange);
        }
        contractSpecification.setInstrument(instrument);

        return new MarketScannerData(ranking, contractSpecification, distance, benchmark, projection,
                comboLegDescription);
    }
}