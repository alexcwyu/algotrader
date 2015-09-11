package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.contract.OptionRight;
import com.unisoft.algotrader.provider.ib.api.model.contract.SecType;
import com.unisoft.algotrader.provider.ib.api.model.order.ExerciseAction;
import com.unisoft.algotrader.provider.ib.api.model.system.Feature;
import com.unisoft.algotrader.provider.ib.api.model.system.IBModelUtils;
import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class ExerciseOptionRequestSerializer extends Serializer{

    private static final int VERSION = 2;
    private final RefDataStore refDataStore;

    public ExerciseOptionRequestSerializer(
            RefDataStore refDataStore, int serverCurrentVersion){
        super(serverCurrentVersion, OutgoingMessageId.EXERCISE_OPTION_REQUEST);
        this.refDataStore = refDataStore;
    }

    public byte [] serialize(final long requestId, final long instId, final ExerciseAction action,
                             final int quantity, final String accountName, final boolean override){
        Instrument instrument = refDataStore.getInstrument(instId);
        return serialize(requestId, instrument, action, quantity, accountName, override);
    }

    public byte [] serialize(final long requestId, final Instrument instrument, final ExerciseAction action,
                             final int quantity, final String accountName, final boolean override){

        ByteArrayBuilder builder = getByteArrayBuilder();
        builder.append(messageId.getId());
        builder.append(VERSION);
        builder.append(requestId);
        appendInstrument(builder, instrument);
        builder.append(action.getValue());
        builder.append(quantity);
        builder.append(accountName);
        builder.append(override);
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
        //builder.appendEol(); // primary exch
        builder.append(instrument.getCcyId());
        builder.appendEol(); //localsymbol

        if (Feature.TRADING_CLASS.isSupportedByVersion(getServerCurrentVersion())) {
            builder.appendEol(); // trading class
        }
    }
}
