package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.IBConstants;
import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static ch.aonyx.broker.ib.api.util.InputStreamUtils.readDouble;
import static ch.aonyx.broker.ib.api.util.InputStreamUtils.readInt;
import static ch.aonyx.broker.ib.api.util.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class PortfolioUpdateEventDeserializer extends Deserializer {

    private final RefDataStore refDataStore;

    public PortfolioUpdateEventDeserializer(RefDataStore refDataStore, int serverCurrentVersion){
        super(IncomingMessageId.PORTFOLIO_UPDATE, serverCurrentVersion);
        this.refDataStore = refDataStore;
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final Instrument instrument = parseInstrument(inputStream ,refDataStore);
        final int position = readInt(inputStream);
        final double marketPrice = readDouble(inputStream);
        final double marketValue = readDouble(inputStream);
        double averageCost = 0;
        double unrealizedProfitAndLoss = 0;
        double realizedProfitAndLoss = 0;
        if (getVersion() >= 3) {
            averageCost = readDouble(inputStream);
            unrealizedProfitAndLoss = readDouble(inputStream);
            realizedProfitAndLoss = readDouble(inputStream);
        }
        String accountName = null;
        if (getVersion() >= 4) {
            accountName = readString(inputStream);
        }
        if ((getVersion() == 6) && (getServerCurrentVersion() == 39)) {
            final String exchId = readString(inputStream);
            //instrument.setExchId(exchId);
        }

        ibSession.onPortfolioUpdateEvent(instrument, position, marketPrice, marketValue, averageCost, unrealizedProfitAndLoss,
                realizedProfitAndLoss, accountName);

    }


}