package com.unisoft.algotrader.provider.ib.api.serializer;

import com.google.common.collect.Lists;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.data.SubscriptionKey;
import com.unisoft.algotrader.provider.ib.api.model.constants.Feature;
import com.unisoft.algotrader.provider.ib.api.model.constants.OutgoingMessageId;
import com.unisoft.algotrader.provider.ib.api.model.constants.RealTimeBarDataType;
import com.unisoft.algotrader.provider.ib.api.model.constants.ReturnedTickTypeFilter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by alex on 8/7/15.
 */
public class HistoricalMarketDataSubscriptionRequestSerializer extends Serializer<SubscriptionKey> {

    private static final int VERSION = 9;
    private final RefDataStore refDataStore;
    private final static RealTimeBarDataType type = RealTimeBarDataType.TRADES;
    private final static boolean snapshot = false;

    private final ReturnedTickTypeFilter[] returnedTickTypeFilters;

    public HistoricalMarketDataSubscriptionRequestSerializer(RefDataStore refDataStore, int serverCurrentVersion, final ReturnedTickTypeFilter... returnedTickTypeFilters){
        super(serverCurrentVersion);
        this.refDataStore = refDataStore;
        this.returnedTickTypeFilters = returnedTickTypeFilters;
    }

    public byte [] serialize(SubscriptionKey subscriptionKey){
        Instrument instrument = refDataStore.getInstrument(subscriptionKey.instId);

        ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.append(OutgoingMessageId.MARKET_DATA_SUBSCRIPTION_REQUEST.getId());
        builder.append(VERSION);
        builder.append(subscriptionKey.subscriptionId);
        appendInstrument(builder, instrument);
        appendCombo(builder, instrument);
        appendUnderlyingCombo(builder, instrument);
        appendReturnedTickTypeFilters(builder);
        builder.append(snapshot);

        return builder.toBytes();
    }

    protected void appendInstrument(ByteArrayBuilder builder, Instrument instrument) {
        if (Feature.MARKET_DATA_REQUEST_BY_CONTRACT_ID.isSupportedByVersion(getServerCurrentVersion())) {
            builder.append(instrument.getInstId());
        }
        super.appendInstrument(builder, instrument);
    }

    private void appendCombo(ByteArrayBuilder builder, Instrument instrument) {
//        if (IBConstants.SecType.COMBO.equals(IBConstants.SecType.convert(instrument.getType()))) {
//            builder.append(contract.getComboLegs().size());
//            for (final ComboLeg comboLeg : contract.getComboLegs()) {
//                builder.append(comboLeg.getContractId());
//                builder.append(comboLeg.getRatio());
//                builder.append(comboLeg.getOrderAction().getAbbreviation());
//                builder.append(comboLeg.getExchange());
//            }
//        }
    }

    private void appendUnderlyingCombo(ByteArrayBuilder builder, Instrument instrument) {
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

    private void appendReturnedTickTypeFilters(ByteArrayBuilder builder) {
        String returnedTickTypeFilterCommaSeparatedList = null;
        if ((returnedTickTypeFilters != null) && (returnedTickTypeFilters.length > 0)) {
            final List<String> returnedTickTypeFilterValues = Lists.transform(
                    Lists.newArrayList(returnedTickTypeFilters), (input) -> String.valueOf(input.getId()));
            returnedTickTypeFilterCommaSeparatedList = StringUtils.join(returnedTickTypeFilterValues, ',');
        }
        builder.append(returnedTickTypeFilterCommaSeparatedList);
    }
}
