package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.data.HistoricalSubscriptionKey;
import com.unisoft.algotrader.provider.ib.api.model.constants.*;
import com.unisoft.algotrader.utils.DateHelper;

import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by alex on 8/7/15.
 */
public class HistoricalMarketDataSubscriptionRequestSerializer extends Serializer<HistoricalSubscriptionKey> {

    private static final int VERSION = 6;
    private final RefDataStore refDataStore;

    public HistoricalMarketDataSubscriptionRequestSerializer(RefDataStore refDataStore, int serverCurrentVersion){
        super(serverCurrentVersion);
        this.refDataStore = refDataStore;
    }

    public byte [] serialize(HistoricalSubscriptionKey subscriptionKey){
        Instrument instrument = refDataStore.getInstrument(subscriptionKey.instId);

        ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.append(OutgoingMessageId.HISTORICAL_DATA_SUBSCRIPTION_REQUEST.getId());
        builder.append(VERSION);
        builder.append(subscriptionKey.getSubscriptionId());
        appendInstrument(builder, instrument);
        builder.append(DateHelper.formatYYYYMMDDHHMMSS(subscriptionKey.toDate));
        builder.append(BarSize.getFormattedBarSize(subscriptionKey.barSize));
        Period period = Period.between(
                (new Date(subscriptionKey.fromDate)).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                (new Date(subscriptionKey.toDate)).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        if (period.getYears() > 0) {
            builder.append(period.getYears() + " Y");
        }
        else if (period.getMonths() > 0){
            builder.append(period.getMonths() + " M");
        }
        else {
            builder.append(period.getDays() + " D");
        }
        builder.append(false);
        builder.append(HistoricalDataType.from(subscriptionKey.type));
        builder.append(DateFormat.YYYYMMDD__HH_MM_SS.getValue());
        appendCombo(builder, instrument);
        if (Feature.LINKING.isSupportedByVersion(getServerCurrentVersion())) {
            builder.appendEol(); // chartOptions
        }
        return builder.toBytes();
    }

    protected void appendInstrument(ByteArrayBuilder builder, Instrument instrument) {
        if (Feature.TRADING_CLASS.isSupportedByVersion(getServerCurrentVersion())) {
           builder.append(0); //contract / instrument id
        }
        super.appendInstrument(builder, instrument);
        if (Feature.TRADING_CLASS.isSupportedByVersion(getServerCurrentVersion())) {
            builder.appendEol(); // trading class
        }
        builder.append(false); // include expiry
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

}
