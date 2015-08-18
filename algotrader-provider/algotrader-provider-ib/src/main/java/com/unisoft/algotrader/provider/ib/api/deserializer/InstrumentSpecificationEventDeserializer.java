package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.IBConstants;
import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;
import com.unisoft.algotrader.provider.ib.api.model.InstrumentSpecification;
import com.unisoft.algotrader.utils.collection.Tuple2;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;
/**
 * Created by alex on 8/13/15.
 */
public class InstrumentSpecificationEventDeserializer extends Deserializer {


    private final RefDataStore refDataStore;

    public InstrumentSpecificationEventDeserializer(int serverCurrentVersion, RefDataStore refDataStore){
        super(IncomingMessageId.CONTRACT_SPECIFICATION, serverCurrentVersion);
        this.refDataStore = refDataStore;
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        int requestId = -1;
        if (getVersion() >= 3) {
            requestId = readInt(inputStream);
        }
        final InstrumentSpecification instrumentSpecification = consumeInstrumentSpecification(inputStream);

        ibSession.onInstrumentSpecification(requestId, instrumentSpecification);
    }


    private InstrumentSpecification consumeInstrumentSpecification(final InputStream inputStream) {
        final InstrumentSpecification instrumentSpecification = new InstrumentSpecification();
        final String symbol = readString(inputStream);
        final Instrument.InstType instType = IBConstants.SecType.convert(readString(inputStream));
        final String expString = readString(inputStream);
        final double strike = readDouble(inputStream);
        final Instrument.PutCall putCall = IBConstants.OptionRight.convert(readString(inputStream));
        final String exchange = readString(inputStream);
        final String ccyCode = readString(inputStream);
        final String localSymbol = readString(inputStream);

        instrumentSpecification.setMarketName(readString(inputStream));
        instrumentSpecification.setTradingClass(readString(inputStream));
        int intId = readInt(inputStream);
        instrumentSpecification.setMinimumFluctuation(readDouble(inputStream));
        final String multiplier  = readString(inputStream);


        instrumentSpecification.setValidOrderTypes(readString(inputStream));
        instrumentSpecification.setValidExchanges(readString(inputStream));
        if (getVersion() >= 2) {
            instrumentSpecification.setPriceMagnifier(readInt(inputStream));
        }
        if (getVersion() >= 4) {
            instrumentSpecification.setUnderlyingContractId(readInt(inputStream));
        }
        String primaryExchange = exchange;
        if (getVersion() >= 5) {
            instrumentSpecification.setLongName(readString(inputStream));
            primaryExchange = readString(inputStream);
        }

        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange(IBProvider.PROVIDER_ID, symbol, exchange);
        instrumentSpecification.setInstrument(instrument);

        if (getVersion() >= 6) {
            instrumentSpecification.setContractMonth(readString(inputStream));
            instrumentSpecification.setIndustry(readString(inputStream));
            instrumentSpecification.setCategory(readString(inputStream));
            instrumentSpecification.setSubcategory(readString(inputStream));
            instrumentSpecification.setTimeZoneId(readString(inputStream));
            instrumentSpecification.setTradingHours(readString(inputStream));
            instrumentSpecification.setLiquidHours(readString(inputStream));
        }
        if (getVersion() >= 8) {
            instrumentSpecification.setEconomicValueRule(readString(inputStream));
            instrumentSpecification.setEconomicValueMultiplier(readDouble(inputStream));
        }
        if (getVersion() >= 7) {
            final int securityIdsCount = readInt(inputStream);
            for (int i = 0; i < securityIdsCount; i++) {
                final Tuple2<String, String> pairTagValue = new Tuple2(readString(inputStream), readString(inputStream));
                instrumentSpecification.getSecurityIds().add(pairTagValue);
            }
        }
        return instrumentSpecification;
    }
}