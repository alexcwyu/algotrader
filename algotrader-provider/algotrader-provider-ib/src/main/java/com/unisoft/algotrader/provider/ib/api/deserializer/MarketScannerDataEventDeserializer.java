package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.google.common.collect.Lists;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.InputStreamUtils;
import com.unisoft.algotrader.provider.ib.api.model.InstrumentSpecification;
import com.unisoft.algotrader.provider.ib.api.model.MarketScannerData;
import com.unisoft.algotrader.provider.ib.api.model.constants.IncomingMessageId;
import com.unisoft.algotrader.provider.ib.api.model.constants.OptionRight;
import com.unisoft.algotrader.provider.ib.api.model.constants.SecType;

import java.io.InputStream;
import java.util.List;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readInt;
import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class MarketScannerDataEventDeserializer extends Deserializer {

    public MarketScannerDataEventDeserializer(){
        super(IncomingMessageId.MARKET_SCANNER_DATA);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBProvider ibProvider) {
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
        int instid = (version >= 3) ? readInt(inputStream) : 0;

        final String symbol = InputStreamUtils.readString(inputStream);
        final Instrument.InstType instType = SecType.convert(InputStreamUtils.readString(inputStream));
        final String expString = InputStreamUtils.readString(inputStream);
        final double strike = InputStreamUtils.readDouble(inputStream);
        final Instrument.PutCall putCall = OptionRight.convert(InputStreamUtils.readString(inputStream));
        final String exchange = InputStreamUtils.readString(inputStream);
        final String ccyCode = InputStreamUtils.readString(inputStream);
        final String localSymbol = InputStreamUtils.readString(inputStream);


        contractSpecification.setMarketName(readString(inputStream));
        contractSpecification.setTradingClass(readString(inputStream));
        final String distance = readString(inputStream);
        final String benchmark = readString(inputStream);
        final String projection = readString(inputStream);
        String comboLegDescription = (version >= 2) ? readString(inputStream) : null;

        Instrument instrument = ibProvider.getRefDataStore().getInstrumentBySymbolAndExchange(IBProvider.PROVIDER_ID, symbol, exchange);
        if (instrument == null){
            throw new RuntimeException("Cannot find instrument symbol=" + symbol +", primaryExchange="+exchange);
        }
        contractSpecification.setInstrument(instrument);

        return new MarketScannerData(ranking, contractSpecification, distance, benchmark, projection,
                comboLegDescription);
    }
}