package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.data.MarketDepthSubscriptionKey;
import com.unisoft.algotrader.provider.ib.api.model.constants.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class MarketDepthSubscriptionRequestSerializer extends Serializer<MarketDepthSubscriptionKey> {

    private static final int VERSION = 1;
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
        builder.append(subscriptionKey.subscriptionId);
        appendInstrument(builder, instrument);
        builder.append(subscriptionKey.numRows);

        return builder.toBytes();
    }

    protected void appendInstrument(ByteArrayBuilder builder, Instrument instrument) {
//        builder.append(instrument.getSymbol(IBProvider.PROVIDER_ID));
//        builder.append(IBConstants.SecType.convert(instrument.getType()));
//        builder.append(IBUtils.convertDate(instrument.getExpiryDate().getTime()));
//        builder.append(instrument.getStrike());
//        builder.append(IBConstants.OptionRight.convert(instrument.getPutCall()));
//        builder.append(instrument.getFactor());
//        builder.append(instrument.getExchId(IBProvider.PROVIDER_ID));
//        builder.append(instrument.getExchId(IBProvider.PROVIDER_ID));
//        builder.append(instrument.getCcyId());
//        builder.appendEol(); //localsymbol
        super.appendInstrument(builder, instrument);
    }
}
