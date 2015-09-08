package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.contract.InstrumentSpecification;
import com.unisoft.algotrader.provider.ib.api.model.contract.SecType;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;
import com.unisoft.algotrader.utils.collection.Tuple2;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class BondInstrumentSpecificationEventDeserializer extends Deserializer {


    public BondInstrumentSpecificationEventDeserializer(){
        super(IncomingMessageId.BOND_CONTRACT_SPECIFICATION);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        int requestId = (version >= 3) ? readInt(inputStream) : -1;
        final InstrumentSpecification instrumentSpecification = consumeInstrumentSpecification(version, inputStream, ibProvider);

        ibProvider.onInstrumentSpecificationEvent(requestId, instrumentSpecification);
    }


    private InstrumentSpecification consumeInstrumentSpecification(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        final InstrumentSpecification instrumentSpecification = new InstrumentSpecification();
        String symbol = readString(inputStream);
        final Instrument.InstType instType = SecType.convert(readString(inputStream));
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
        if (version >= 2) {
            instrumentSpecification.setNextOptionDate(readString(inputStream));
            instrumentSpecification.setNextOptionType(readString(inputStream));
            instrumentSpecification.setNextOptionPartial(readBoolean(inputStream));
            instrumentSpecification.setNotes(readString(inputStream));
        }
        if (version >= 4) {
            instrumentSpecification.setLongName(readString(inputStream));
        }
        if (version >= 6) {
            instrumentSpecification.setEconomicValueRule(readString(inputStream));
            instrumentSpecification.setEconomicValueMultiplier(readDouble(inputStream));
        }
        if (version >= 5) {
            final int securityIdsCount = readInt(inputStream);
            for (int i = 0; i < securityIdsCount; i++) {
                final Tuple2<String, String> pairTagValue = new Tuple2(readString(inputStream), readString(inputStream));
                instrumentSpecification.getSecurityIds().add(pairTagValue);
            }
        }

        Instrument instrument = ibProvider.getRefDataStore().getInstrumentBySymbolAndExchange(IBProvider.PROVIDER_ID, symbol, exchange);
        instrumentSpecification.setInstrument(instrument);

        return instrumentSpecification;
    }
}