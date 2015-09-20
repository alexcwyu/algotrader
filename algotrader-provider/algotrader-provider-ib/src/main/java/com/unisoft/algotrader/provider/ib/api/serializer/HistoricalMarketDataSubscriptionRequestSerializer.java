package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.data.HistoricalSubscriptionKey;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.contract.OptionRight;
import com.unisoft.algotrader.provider.ib.api.model.contract.SecType;
import com.unisoft.algotrader.provider.ib.api.model.data.BarSize;
import com.unisoft.algotrader.provider.ib.api.model.data.DateFormat;
import com.unisoft.algotrader.provider.ib.api.model.data.HistoricalDataType;
import com.unisoft.algotrader.provider.ib.api.model.system.Feature;
import com.unisoft.algotrader.provider.ib.api.model.system.IBModelUtils;
import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;
import com.unisoft.algotrader.utils.DateHelper;

import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by alex on 8/7/15.
 */
public class HistoricalMarketDataSubscriptionRequestSerializer extends Serializer{

    private static final int VERSION = 6;
    private final RefDataStore refDataStore;

    public HistoricalMarketDataSubscriptionRequestSerializer(RefDataStore refDataStore, int serverCurrentVersion){
        super(serverCurrentVersion, OutgoingMessageId.HISTORICAL_DATA_SUBSCRIPTION_REQUEST);
        this.refDataStore = refDataStore;
    }

    public byte [] serialize(long requestId, HistoricalSubscriptionKey subscriptionKey){
        Instrument instrument = refDataStore.getInstrument(subscriptionKey.instId);

        ByteArrayBuilder builder = getByteArrayBuilder();

        builder.append(messageId.getId());
        builder.append(VERSION);
        builder.append(requestId);
        appendInstrument(builder, instrument);
        builder.append(false); // include expiry
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
        builder.append(false); //RTH
        builder.append(HistoricalDataType.from(subscriptionKey.type)); //What to show
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

        builder.append(instrument.getSymbol(IBProvider.PROVIDER_ID.name()));
        builder.append(SecType.convert(instrument.getType()));
        if (instrument.getExpiryDate() != null) {
            builder.append(IBModelUtils.convertDateTime(instrument.getExpiryDate().getTime()));
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
        builder.append(instrument.getExchId(IBProvider.PROVIDER_ID.name()));
        builder.appendEol(); // primary exch
        builder.append(instrument.getCcyId());
        builder.appendEol(); //localsymbol

        if (Feature.TRADING_CLASS.isSupportedByVersion(getServerCurrentVersion())) {
            builder.appendEol(); // trading class
        }
    }

    private void appendCombo(ByteArrayBuilder builder, Instrument instrument) {
        //should never happen
        if (SecType.COMBO.equals(SecType.convert(instrument.getType()))) {
            builder.append(0);
//            for (final ComboLeg comboLeg : contract.getComboLegs()) {
//                builder.append(comboLeg.getContractId());
//                builder.append(comboLeg.getRatio());
//                builder.append(comboLeg.getOrderAction().getAbbreviation());
//                builder.append(comboLeg.getExchange());
//            }
        }
    }

}
