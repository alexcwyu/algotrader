package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.event.BondInstrumentSpecificationEvent;
import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.model.contract.ContractSpecification;
import com.unisoft.algotrader.provider.ib.api.model.contract.SecType;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;
import com.unisoft.algotrader.utils.collection.Tuple2;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class BondInstrumentSpecificationEventDeserializer extends Deserializer<BondInstrumentSpecificationEvent> {

    private final RefDataStore refDataStore;
    public BondInstrumentSpecificationEventDeserializer(RefDataStore refDataStore, int serverCurrentVersion){
        super(IncomingMessageId.BOND_CONTRACT_SPECIFICATION, serverCurrentVersion);
        this.refDataStore = refDataStore;
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBEventHandler eventHandler) {
        int requestId = (version >= 3) ? readInt(inputStream) : -1;
        final ContractSpecification contractSpecification = consumeInstrumentSpecification(version, inputStream, eventHandler);

        eventHandler.onInstrumentSpecificationEvent(requestId, contractSpecification);
    }


    private ContractSpecification consumeInstrumentSpecification(final int version, final InputStream inputStream, final IBEventHandler eventHandler) {
        final ContractSpecification contractSpecification = new ContractSpecification();
        String symbol = readString(inputStream);
        final Instrument.InstType instType = SecType.convert(readString(inputStream));
        contractSpecification.setCusip(readString(inputStream));
        contractSpecification.setCoupon(readDouble(inputStream));
        contractSpecification.setMaturity(readString(inputStream));
        contractSpecification.setIssueDate(readString(inputStream));
        contractSpecification.setRatings(readString(inputStream));
        contractSpecification.setBondType(readString(inputStream));
        contractSpecification.setCouponType(readString(inputStream));
        contractSpecification.setConvertible(readBoolean(inputStream));
        contractSpecification.setCallable(readBoolean(inputStream));
        contractSpecification.setPutable(readBoolean(inputStream));
        contractSpecification.setDescription(readString(inputStream));
        String exchange = readString(inputStream);
        String currencyCode = readString(inputStream);
        contractSpecification.setMarketName(readString(inputStream));
        contractSpecification.setTradingClass(readString(inputStream));
        int instId = readInt(inputStream);
        contractSpecification.setMinimumFluctuation(readDouble(inputStream));
        contractSpecification.setValidOrderTypes(readString(inputStream));
        contractSpecification.setValidExchanges(readString(inputStream));
        if (version >= 2) {
            contractSpecification.setNextOptionDate(readString(inputStream));
            contractSpecification.setNextOptionType(readString(inputStream));
            contractSpecification.setNextOptionPartial(readBoolean(inputStream));
            contractSpecification.setNotes(readString(inputStream));
        }
        if (version >= 4) {
            contractSpecification.setLongName(readString(inputStream));
        }
        if (version >= 6) {
            contractSpecification.setEconomicValueRule(readString(inputStream));
            contractSpecification.setEconomicValueMultiplier(readDouble(inputStream));
        }
        if (version >= 5) {
            final int securityIdsCount = readInt(inputStream);
            for (int i = 0; i < securityIdsCount; i++) {
                final Tuple2<String, String> pairTagValue = new Tuple2(readString(inputStream), readString(inputStream));
                contractSpecification.getSecurityIds().add(pairTagValue);
            }
        }

        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange(IBProvider.PROVIDER_ID, symbol, exchange);
        contractSpecification.setInstrument(instrument);

        return contractSpecification;
    }
}