package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.IBUtils;
import com.unisoft.algotrader.provider.ib.api.IBConstants;

import java.io.InputStream;

import static ch.aonyx.broker.ib.api.util.InputStreamUtils.readInt;

/**
 * Created by alex on 8/2/15.
 */
public abstract class Serializer<M> {

    protected int serverCurrentVersion;

    protected Serializer(int serverCurrentVersion) {
        this.serverCurrentVersion = serverCurrentVersion;
    }

    protected int parseVersion(InputStream inputStream){
        return readInt(inputStream);
    }

    public abstract byte[] serialize(M event);



    protected int getServerCurrentVersion(){
        return serverCurrentVersion;
    }

    protected void appendInstrument(ByteArrayBuilder builder, Instrument instrument) {
        builder.append(instrument.getSymbol(IBProvider.PROVIDER_ID));
        builder.append(IBConstants.SecType.convert(instrument.getType()));
        builder.append(IBUtils.convertDate(instrument.getExpiryDate().getTime()));
        builder.append(instrument.getStrike());
        builder.append(IBConstants.OptionRight.convert(instrument.getPutCall()));
        builder.append(instrument.getFactor());
        builder.append(instrument.getExchId(IBProvider.PROVIDER_ID));
        builder.append(instrument.getExchId(IBProvider.PROVIDER_ID));
        builder.append(instrument.getCcyId());
        builder.appendEol(); //localsymbol
    }



}
