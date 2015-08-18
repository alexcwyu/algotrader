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
public class BondInstrumentSpecificationEventDeserializer extends Deserializer {


    private final RefDataStore refDataStore;

    public BondInstrumentSpecificationEventDeserializer(int serverCurrentVersion, RefDataStore refDataStore){
        super(IncomingMessageId.BOND_CONTRACT_SPECIFICATION, serverCurrentVersion);
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
        String symbol = readString(inputStream);
        final Instrument.InstType instType = IBConstants.SecType.convert(readString(inputStream));
        instrumentSpecification.setCusip(readString(inputStream));
        instrumentSpecification.setCoupon(readDouble(inputStream));
        instrumentSpecification.setMaturity(readString(inputStream));
        instrumentSpecification.setIssueDate(readString(inputStream));
        instrumentSpecification.setRatings(readString(inputStream));
        instrumentSpecification.setBondType(readString(inputStream));
        instrumentSpecification.setCouponType(readString(inputStream));
        instrumentSpecification.setConvertible(readBoolean(inputStream));
        instrumentSpecification.setCallable(readBoolean(inputStream));
        instrumentSpecification.setPutable(readBoolean(inputStream));
        instrumentSpecification.setDescription(readString(inputStream));
        String exchange = readString(inputStream);
        String currencyCode = readString(inputStream);
        instrumentSpecification.setMarketName(readString(inputStream));
        instrumentSpecification.setTradingClass(readString(inputStream));
        int instId = readInt(inputStream);
        instrumentSpecification.setMinimumFluctuation(readDouble(inputStream));
        instrumentSpecification.setValidOrderTypes(readString(inputStream));
        instrumentSpecification.setValidExchanges(readString(inputStream));
        if (getVersion() >= 2) {
            instrumentSpecification.setNextOptionDate(readString(inputStream));
            instrumentSpecification.setNextOptionType(readString(inputStream));
            instrumentSpecification.setNextOptionPartial(readBoolean(inputStream));
            instrumentSpecification.setNotes(readString(inputStream));
        }
        if (getVersion() >= 4) {
            instrumentSpecification.setLongName(readString(inputStream));
        }
        if (getVersion() >= 6) {
            instrumentSpecification.setEconomicValueRule(readString(inputStream));
            instrumentSpecification.setEconomicValueMultiplier(readDouble(inputStream));
        }
        if (getVersion() >= 5) {
            final int securityIdsCount = readInt(inputStream);
            for (int i = 0; i < securityIdsCount; i++) {
                final Tuple2<String, String> pairTagValue = new Tuple2(readString(inputStream), readString(inputStream));
                instrumentSpecification.getSecurityIds().add(pairTagValue);
            }
        }

        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange(IBProvider.PROVIDER_ID, symbol, exchange);
        instrumentSpecification.setInstrument(instrument);

        return instrumentSpecification;
    }
}