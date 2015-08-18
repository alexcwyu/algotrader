package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.IBConstants;
import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;
import com.unisoft.algotrader.provider.ib.api.InputStreamUtils;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;

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
            final String primaryExchange = readString(inputStream);
            //instrument.setExchId(exchId);
        }

        ibSession.onPortfolioUpdateEvent(instrument, position, marketPrice, marketValue, averageCost, unrealizedProfitAndLoss,
                realizedProfitAndLoss, accountName);

    }


    protected Instrument parseInstrument(final InputStream inputStream, final RefDataStore refDataStore) {
        final int instId = InputStreamUtils.readInt(inputStream);
        final String symbol = readString(inputStream);
        final Instrument.InstType instType = IBConstants.SecType.convert(readString(inputStream));
        final String expString = readString(inputStream);
        final double strike = InputStreamUtils.readDouble(inputStream);
        final Instrument.PutCall putCall = IBConstants.OptionRight.convert(readString(inputStream));
        final String multiplier = (getVersion() >= 7)?readString(inputStream) : null;
        final String primaryExchange = (getVersion() >= 7)?readString(inputStream) : null;
        final String ccyCode = readString(inputStream);
        final String localSymbol = (getVersion() >= 2)?readString(inputStream) : null;
        final String tradingClass = (getVersion() >= 8)? readString(inputStream): null;

        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange(IBProvider.PROVIDER_ID, symbol, primaryExchange);
        if (instrument == null){
            throw new RuntimeException("Cannot find instrumnet symbol=" + symbol +", primaryExchange="+primaryExchange);
        }

        return instrument;
    }


}