package com.unisoft.algotrader.provider.ib.api.serializer;

import ch.aonyx.broker.ib.api.*;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.data.SubscriptionKey;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.constants.*;
import com.unisoft.algotrader.provider.ib.api.model.constants.Feature;
import com.unisoft.algotrader.provider.ib.api.model.constants.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class MarketDataSubscriptionRequestSerializer extends Serializer<SubscriptionKey> {

    private static final int VERSION = 11;
    private final RefDataStore refDataStore;
    private final boolean snapshot = false;

    public MarketDataSubscriptionRequestSerializer(
            RefDataStore refDataStore, int serverCurrentVersion){
        super(serverCurrentVersion);
        this.refDataStore = refDataStore;
    }

    public byte [] serialize(SubscriptionKey subscriptionKey){
        Instrument instrument = refDataStore.getInstrument(subscriptionKey.instId);

        ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.append(OutgoingMessageId.MARKET_DATA_SUBSCRIPTION_REQUEST.getId());
        builder.append(VERSION);
        builder.append(subscriptionKey.getSubscriptionId());
        appendInstrument(builder, instrument);
        appendCombo(builder, instrument);
        appendUnderlyingCombo(builder, instrument);
        appendReturnedTickTypeFilters(builder);
        builder.append(snapshot);
        appendMarketDataOptions(builder);
        return builder.toBytes();
    }

    protected void appendInstrument(ByteArrayBuilder builder, Instrument instrument) {
        if (Feature.MARKET_DATA_REQUEST_BY_CONTRACT_ID.isSupportedByVersion(getServerCurrentVersion())) {
            builder.append(0);
        }
        builder.append(instrument.getSymbol(IBProvider.PROVIDER_ID));
        builder.append(SecType.convert(instrument.getType()));
        if (instrument.getExpiryDate() != null) {
            builder.append(IBModelUtils.convertDate(instrument.getExpiryDate().getTime()));
        }
        else {
            builder.appendEol();
        }
        builder.append(instrument.getStrike());
        builder.append(OptionRight.convert(instrument.getPutCall()));
        if (instrument.getFactor() == 0.0 || instrument.getFactor() == 1.0){
            builder.appendEol();
        }
        else {
            builder.append(instrument.getFactor());
        }
        builder.append(instrument.getExchId(IBProvider.PROVIDER_ID));
        builder.appendEol(); // primary exch
        builder.append(instrument.getCcyId());
        builder.appendEol(); //localsymbol

        if (Feature.TRADING_CLASS.isSupportedByVersion(getServerCurrentVersion())) {
            builder.appendEol(); // trading class
        }
    }

    private void appendCombo(final ByteArrayBuilder builder, final Instrument instrument) {
//        if (SecurityType.COMBO.equals(contract.getSecurityType())) {
//            builder.append(contract.getComboLegs().size());
//            for (final ComboLeg comboLeg : contract.getComboLegs()) {
//                builder.append(comboLeg.getContractId());
//                builder.append(comboLeg.getRatio());
//                builder.append(comboLeg.getOrderAction().getAbbreviation());
//                builder.append(comboLeg.getExchange());
//            }
//        }
    }

    private void appendUnderlyingCombo(final ByteArrayBuilder builder, final Instrument instrument) {
        if (Feature.DELTA_NEUTRAL_COMBO_ORDER.isSupportedByVersion(getServerCurrentVersion())) {
//            final UnderlyingCombo underComp = contract.getUnderlyingCombo();
//            if (underComp != null) {
//                builder.append(true);
//                builder.append(underComp.getContractId());
//                builder.append(underComp.getDelta());
//                builder.append(underComp.getPrice());
//            } else {
                builder.append(false);
//            }
        }
    }

    private void appendReturnedTickTypeFilters(final ByteArrayBuilder builder) {
        String returnedTickTypeFilterCommaSeparatedList = null;
//        if ((returnedTickTypeFilters != null) && (returnedTickTypeFilters.length > 0)) {
//            final List<String> returnedTickTypeFilterValues = Lists.transform(
//                    Lists.newArrayList(returnedTickTypeFilters), returnedTickTypeFilterToStringFunction);
//            returnedTickTypeFilterCommaSeparatedList = StringUtils.join(returnedTickTypeFilterValues, ',');
//        }
        builder.append(returnedTickTypeFilterCommaSeparatedList);
    }

    private void appendMarketDataOptions(final ByteArrayBuilder builder) {
        if (Feature.LINKING.isSupportedByVersion(getServerCurrentVersion())) {
            builder.append("");
        }
    }

}
