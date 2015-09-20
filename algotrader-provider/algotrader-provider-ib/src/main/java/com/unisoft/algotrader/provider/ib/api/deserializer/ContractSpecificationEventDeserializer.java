package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.event.ContractSpecificationEvent;
import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.model.contract.ContractSpecification;
import com.unisoft.algotrader.provider.ib.api.model.contract.OptionRight;
import com.unisoft.algotrader.provider.ib.api.model.contract.SecType;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;
import com.unisoft.algotrader.utils.collection.Tuple2;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.*;
/**
 * Created by alex on 8/13/15.
 */
public class ContractSpecificationEventDeserializer extends Deserializer<ContractSpecificationEvent> {

    private final RefDataStore refDataStore;

    public ContractSpecificationEventDeserializer(RefDataStore refDataStore, int serverCurrentVersion){
        super(IncomingMessageId.CONTRACT_SPECIFICATION, serverCurrentVersion);
        this.refDataStore = refDataStore;
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBEventHandler eventHandler) {
        final int requestId = (version >= 3) ? readInt(inputStream) : -1;
        final ContractSpecification contractSpecification = consumeInstrumentSpecification(version, inputStream, eventHandler);

        eventHandler.onInstrumentSpecificationEvent(requestId, contractSpecification);
    }


    private ContractSpecification consumeInstrumentSpecification(final int version, final InputStream inputStream, final IBEventHandler eventHandler) {
        final ContractSpecification contractSpecification = new ContractSpecification();
        final String symbol = readString(inputStream);
        final Instrument.InstType instType = SecType.convert(readString(inputStream));
        final String expString = readString(inputStream);
        final double strike = readDouble(inputStream);
        final Instrument.PutCall putCall = OptionRight.convert(readString(inputStream));
        final String exchange = readString(inputStream);
        final String ccyCode = readString(inputStream);
        final String localSymbol = readString(inputStream);

        contractSpecification.setMarketName(readString(inputStream));
        contractSpecification.setTradingClass(readString(inputStream));
        int intId = readInt(inputStream);
        contractSpecification.setMinimumFluctuation(readDouble(inputStream));
        final String multiplier  = readString(inputStream);


        contractSpecification.setValidOrderTypes(readString(inputStream));
        contractSpecification.setValidExchanges(readString(inputStream));
        if (version >= 2) {
            contractSpecification.setPriceMagnifier(readInt(inputStream));
        }
        if (version >= 4) {
            contractSpecification.setUnderlyingContractId(readInt(inputStream));
        }
        String primaryExchange = exchange;
        if (version >= 5) {
            contractSpecification.setLongName(readString(inputStream));
            primaryExchange = readString(inputStream);
        }

        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange(IBProvider.PROVIDER_ID.name(), symbol, exchange);
        contractSpecification.setInstrument(instrument);

        if (version >= 6) {
            contractSpecification.setContractMonth(readString(inputStream));
            contractSpecification.setIndustry(readString(inputStream));
            contractSpecification.setCategory(readString(inputStream));
            contractSpecification.setSubcategory(readString(inputStream));
            contractSpecification.setTimeZoneId(readString(inputStream));
            contractSpecification.setTradingHours(readString(inputStream));
            contractSpecification.setLiquidHours(readString(inputStream));
        }
        if (version >= 8) {
            contractSpecification.setEconomicValueRule(readString(inputStream));
            contractSpecification.setEconomicValueMultiplier(readDouble(inputStream));
        }
        if (version >= 7) {
            final int securityIdsCount = readInt(inputStream);
            for (int i = 0; i < securityIdsCount; i++) {
                final Tuple2<String, String> pairTagValue = new Tuple2(readString(inputStream), readString(inputStream));
                contractSpecification.getSecurityIds().add(pairTagValue);
            }
        }
        return contractSpecification;
    }
}