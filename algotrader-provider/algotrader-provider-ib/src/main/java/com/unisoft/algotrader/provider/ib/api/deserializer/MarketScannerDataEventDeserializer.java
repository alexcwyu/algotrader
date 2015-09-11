package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.google.common.collect.Lists;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.InputStreamUtils;
import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.model.contract.ContractSpecification;
import com.unisoft.algotrader.provider.ib.api.model.contract.OptionRight;
import com.unisoft.algotrader.provider.ib.api.model.contract.SecType;
import com.unisoft.algotrader.provider.ib.api.model.data.MarketScannerData;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;

import java.io.InputStream;
import java.util.List;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readInt;
import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class MarketScannerDataEventDeserializer extends Deserializer {

    private final RefDataStore refDataStore;

    public MarketScannerDataEventDeserializer(RefDataStore refDataStore, int serverCurrentVersion){
        super(IncomingMessageId.MARKET_SCANNER_DATA, serverCurrentVersion);
        this.refDataStore = refDataStore;

    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBEventHandler eventHandler) {
        final int requestId = readInt(inputStream);
        final List<MarketScannerData> marketScannerDataEvents = Lists.newArrayList();
        final int marketScannerDatas = readInt(inputStream);
        for (int i = 0; i < marketScannerDatas; i++) {
            marketScannerDataEvents.add(consumeMarketScannerDataEvent(version, inputStream, eventHandler, requestId));
        }

        eventHandler.onMarketScannerDataListEvent(requestId, marketScannerDataEvents);
    }

    private MarketScannerData consumeMarketScannerDataEvent(final int version, final InputStream inputStream, final IBEventHandler eventHandler, final int requestId) {

        final ContractSpecification contractSpecification = new ContractSpecification();
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

        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange(IBProvider.PROVIDER_ID, symbol, exchange);
        if (instrument == null){
            throw new RuntimeException("Cannot find instrument symbol=" + symbol +", primaryExchange="+exchange);
        }
        contractSpecification.setInstrument(instrument);

        return new MarketScannerData(ranking, contractSpecification, distance, benchmark, projection,
                comboLegDescription);
    }
}