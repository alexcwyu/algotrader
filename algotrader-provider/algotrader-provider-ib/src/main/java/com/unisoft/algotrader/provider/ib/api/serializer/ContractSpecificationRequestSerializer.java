package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.contract.OptionRight;
import com.unisoft.algotrader.provider.ib.api.model.contract.SecType;
import com.unisoft.algotrader.provider.ib.api.model.system.Feature;
import com.unisoft.algotrader.provider.ib.api.model.system.IBModelUtils;
import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class ContractSpecificationRequestSerializer extends Serializer{

    private static final int VERSION = 7;
    private final RefDataStore refDataStore;

    public ContractSpecificationRequestSerializer(
            RefDataStore refDataStore, int serverCurrentVersion){
        super(serverCurrentVersion);
        this.refDataStore = refDataStore;
    }

    public byte [] serialize(long requestId, long instId){
        Instrument instrument = refDataStore.getInstrument(instId);
        return serialize(requestId, instrument);
    }

    public byte [] serialize(long requestId, Instrument instrument){

        ByteArrayBuilder builder = getByteArrayBuilder();
        builder.append(OutgoingMessageId.CONTRACT_SPECIFICATION_REQUEST.getId());
        builder.append(VERSION);
        if (Feature.CONTRACT_SPECIFICATION_MARKER.isSupportedByVersion(getServerCurrentVersion())) {
            builder.append(requestId);
        }
        appendInstrument(builder, instrument);
        return builder.toBytes();
    }

    protected void appendInstrument(ByteArrayBuilder builder, Instrument instrument) {
        if (Feature.CONTRACT_CONID.isSupportedByVersion(getServerCurrentVersion())) {
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
        builder.append(false); //includeExpired
        if (Feature.SECURITY_ID_TYPE.isSupportedByVersion(getServerCurrentVersion())) {
            builder.appendEol(); //getSecurityIdentifierCode().getAcronym()
            builder.appendEol(); //getSecurityId
        }
//
//        if (Feature.TRADING_CLASS.isSupportedByVersion(getServerCurrentVersion())) {
//            builder.appendEol(); // trading class
//        }
    }
}
