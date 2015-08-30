package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.data.SubscriptionKey;
import com.unisoft.algotrader.provider.ib.api.model.constants.OutgoingMessageId;
import com.unisoft.algotrader.provider.ib.api.model.constants.RealTimeBarDataType;

/**
 * Created by alex on 8/7/15.
 */
public class RealTimeMarketDataSubscriptionRequestSerializer extends Serializer<SubscriptionKey> {

    private static final int VERSION = 1;
    private final RefDataStore refDataStore;
    private final static int size = 5;
    private final static RealTimeBarDataType type = RealTimeBarDataType.TRADES;
    private final static boolean useRegularTradingHours = false;


    public RealTimeMarketDataSubscriptionRequestSerializer(
            RefDataStore refDataStore, int serverCurrentVersion){
        super(serverCurrentVersion);
        this.refDataStore = refDataStore;
    }

    public byte [] serialize(SubscriptionKey subscriptionKey){
        Instrument instrument = refDataStore.getInstrument(subscriptionKey.instId);

        ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.append(OutgoingMessageId.REAL_TIME_BAR_SUBSCRIPTION_REQUEST.getId());
        builder.append(VERSION);
        builder.append(subscriptionKey.subscriptionId);
        appendInstrument(builder, instrument);
        builder.append(size);
        builder.append(type.getBytes());
        builder.append(useRegularTradingHours);

        return builder.toBytes();
    }
}
