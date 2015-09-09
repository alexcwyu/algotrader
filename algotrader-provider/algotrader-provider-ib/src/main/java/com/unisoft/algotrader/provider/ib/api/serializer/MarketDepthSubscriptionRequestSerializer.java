package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.data.MarketDepthSubscriptionKey;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.contract.OptionRight;
import com.unisoft.algotrader.provider.ib.api.model.contract.SecType;
import com.unisoft.algotrader.provider.ib.api.model.system.Feature;
import com.unisoft.algotrader.provider.ib.api.model.system.IBModelUtils;
import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class MarketDepthSubscriptionRequestSerializer extends Serializer{

    private static final int VERSION = 5;
    private final RefDataStore refDataStore;

    public MarketDepthSubscriptionRequestSerializer(RefDataStore refDataStore, int serverCurrentVersion){
        super(serverCurrentVersion);
        this.refDataStore = refDataStore;
    }

    public byte [] serialize(MarketDepthSubscriptionKey subscriptionKey){
        Instrument instrument = refDataStore.getInstrument(subscriptionKey.instId);

        ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.append(OutgoingMessageId.MARKET_DEPTH_SUBSCRIPTION_REQUEST.getId());
        builder.append(VERSION);
        builder.append(subscriptionKey.getSubscriptionId());
        appendInstrument(builder, instrument);
        builder.append(subscriptionKey.numRows);
        if (Feature.LINKING.isSupportedByVersion(getServerCurrentVersion())) {
            builder.appendEol(); // options
        }
        return builder.toBytes();
    }

    protected void appendInstrument(ByteArrayBuilder builder, Instrument instrument) {
        if (Feature.TRADING_CLASS.isSupportedByVersion(getServerCurrentVersion())) {
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
        builder.append(instrument.getCcyId());
        builder.appendEol(); //localsymbol

        if (Feature.TRADING_CLASS.isSupportedByVersion(getServerCurrentVersion())) {
            builder.appendEol(); // trading class
        }
    }
}
