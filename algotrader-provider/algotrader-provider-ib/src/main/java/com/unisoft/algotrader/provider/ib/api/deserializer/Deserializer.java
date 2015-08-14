package com.unisoft.algotrader.provider.ib.api.deserializer;

import ch.aonyx.broker.ib.api.contract.UnderlyingCombo;
import ch.aonyx.broker.ib.api.order.*;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.model.trading.*;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.IBConstants;
import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.InputStream;

import static ch.aonyx.broker.ib.api.util.InputStreamUtils.readDoubleMax;
import static ch.aonyx.broker.ib.api.util.InputStreamUtils.readString;
import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;

/**
 * Created by alex on 8/11/15.
 */
public abstract class Deserializer<M extends Event> {

    protected static final int VERSION_2 = 2;
    protected static final int VERSION_3 = 3;

    private final IncomingMessageId messageId;
    private int version;
    private final int serverCurrentVersion;

    public Deserializer(IncomingMessageId messageId, int serverCurrentVersion){
        this.messageId = messageId;
        this.serverCurrentVersion = serverCurrentVersion;
    }

    public void consume(final InputStream inputStream,
                     final IBSession ibSession) {
        Validate.notNull(inputStream);
        version = readInt(inputStream);
        consumeVersionLess(inputStream, ibSession);
    }

    protected final int getVersion() {
        return version;
    }

    protected final int getServerCurrentVersion() {
        return serverCurrentVersion;
    }

    public IncomingMessageId messageId(){
        return messageId;
    }

    public abstract void consumeVersionLess(final InputStream inputStream,
            final IBSession ibSession);

    protected Instrument parseInstrument(final InputStream inputStream, final RefDataStore refDataStore) {
        final int instId = readInt(inputStream);
        final String symbol = readString(inputStream);
        final Instrument.InstType instType = IBConstants.SecType.convert(readString(inputStream));
        final String expString = readString(inputStream);
        final double strike = readDouble(inputStream);
        final Instrument.PutCall putCall = IBConstants.OptionRight.convert(readString(inputStream));
        final String multiplier = (getVersion() >= 7)?readString(inputStream) : null;
        final String primaryExchange = (getVersion() >= 7)?readString(inputStream) : null;
        final String ccyCode = readString(inputStream);
        final String localSymbol = (getVersion() >= 2)?readString(inputStream) : null;

        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange(IBProvider.PROVIDER_ID, symbol, primaryExchange);
        if (instrument == null){
            throw new RuntimeException("Cannot find instrumnet symbol=" + symbol +", primaryExchange="+primaryExchange);
        }

        return instrument;
    }

    //TODO
    protected Order parseOrder(final InputStream inputStream){

        final Side side = IBConstants.Action.convert(readString(inputStream));
        final int totalQty = readInt(inputStream);
        final OrdType orderType = IBConstants.OrderType.convert(readString(inputStream));

        final double limitPrice = (getVersion() < 29)? readDouble(inputStream) : readDoubleMax(inputStream);
        final double stopPrice = (getVersion() < 30) ? readDouble(inputStream) : readDoubleMax(inputStream);

        final com.unisoft.algotrader.model.trading.TimeInForce tif = IBConstants.TIF.convert(readString(inputStream));

        final String ocaGroupName = readString(inputStream);
        final String accountName = readString(inputStream);

        final String openCloseInstitutional = readString(inputStream);
        final int originInstitutional = readInt(inputStream);

        final String orderReference = readString(inputStream);


        final int clientId = (getVersion() >= 3)? readInt(inputStream) : 0;

        int permanentId = 0;
        boolean outsideRegularTradingHours = false;
        boolean hidden = false;
        double discretionaryAmount = 0.0;
        if (getVersion() >= 4) {
            permanentId = readInt(inputStream);
            if (getVersion() < 18) {
                readBoolean(inputStream);
            } else {
                outsideRegularTradingHours = readBoolean(inputStream);
            }
            hidden = readBoolean(inputStream);
            discretionaryAmount = readDouble(inputStream);
        }

        String goodAfterDateTime = null;
        if (getVersion() >= 5) {
            goodAfterDateTime = readString(inputStream);
        }

        if (getVersion() >= 6) {
            readString(inputStream);
        }

        String financialAdvisorGroup = null;
        String financialAdvisorMethod = null;
        String financialAdvisorPercentage = null;
        String financialAdvisorProfile = null;

        if (getVersion() >= 7) {
            financialAdvisorGroup = readString(inputStream);
            financialAdvisorMethod = readString(inputStream);
            financialAdvisorPercentage = readString(inputStream);
            financialAdvisorProfile = readString(inputStream);
        }

        String goodTillDateTime = null;
        if (getVersion() >= 8) {
            goodTillDateTime = readString(inputStream);
        }
        if (getVersion() >= 9) {
            String Rule80A = readString(inputStream);
            double percentageOffset =readDoubleMax(inputStream);
            String settlingFirm = readString(inputStream);
            int shortSaleSlot = readInt(inputStream);
            String designatedLocation = readString(inputStream);
            if (getServerCurrentVersion() == 51) {
                readInt(inputStream);
            } else if (getVersion() >= 23) {
                int exemptionCode = readInt(inputStream);
            }
            int auctionStrategy = readInt(inputStream);
            double startingPrice = readDoubleMax(inputStream);
            double stockReferencePrice = readDoubleMax(inputStream);
            double delta = readDoubleMax(inputStream);
            double lowerStockPriceRange = readDoubleMax(inputStream);
            double upperStockPriceRange = readDoubleMax(inputStream);
            int displaySize = readInt(inputStream);
            if (getVersion() < 18) {
                readBoolean(inputStream);
            }
            boolean blockOrder = readBoolean(inputStream);
            boolean sweepToFill = readBoolean(inputStream);
            boolean allOrNone = readBoolean(inputStream);
            int minimumQuantity = readIntMax(inputStream);
            int ocaType = readInt(inputStream);
            boolean electronicTradeOnly = readBoolean(inputStream);
            boolean firmQuoteOnly = readBoolean(inputStream);
            double nbboPriceCap = readDoubleMax(inputStream);
        }
        if (getVersion() >= 10) {
            int parentId = readInt(inputStream);
            int stopTriggerMethod = readInt(inputStream);
        }
        if (getVersion() >= 11) {
            double volatility = readDoubleMax(inputStream);
            int volatilityType = readInt(inputStream);
            if (getVersion() == 11) {
                String deltaNeutralOrderType  = (readInt(inputStream) == 0 ? "NONE" : "MKT");
            } else {
                String deltaNeutralOrderType = readString(inputStream);
                double deltaNeutralAuxPrice = readDoubleMax(inputStream);
                if ((getVersion() >= 27)
                        && StringUtils.isNotEmpty(deltaNeutralOrderType)) {
                    int deltaNeutralContractId = readInt(inputStream);
                    String deltaNeutralSettlingFirm = readString(inputStream);
                    String deltaNeutralClearingAccount = readString(inputStream);
                    String deltaNeutralClearingIntent = readString(inputStream);
                }
            }
            int continuouslyUpdate = readInt(inputStream);
            if (getServerCurrentVersion() == 26) {
                double lowerStockPriceRange = readDouble(inputStream);
                double upperStockPriceRange = readDouble(inputStream);
            }
            int referencePriceType = readInt(inputStream);
        }
        if (getVersion() >= 13) {
            double trailingStopPrice = readDoubleMax(inputStream);
        }
        if (getVersion() >= 30) {
            double trailingPercent = readDoubleMax(inputStream);
        }
        if (getVersion() >= 14) {
            double basisPoint = readDoubleMax(inputStream);
            int basisPointType = readIntMax(inputStream);
            String comboLegsDescription = readString(inputStream);
        }
        if (getVersion() >= 29) {
            final int comboLegsCount = readInt(inputStream);
            for (int i = 0; i < comboLegsCount; i++) {
                //final ComboLeg comboLeg = new ComboLeg();
                //contract.addComboLeg(comboLeg);
                int contractId = readInt(inputStream);
                int ratio = readInt(inputStream);
                final Side side1 = IBConstants.Action.convert(readString(inputStream));
                String exchange = readString(inputStream);
                int openClose = readInt(inputStream);
                int shortSaleSlotValue= readInt(inputStream);
                String designatedLocation = readString(inputStream);
                int exemptionCode = readInt(inputStream);
            }
            final int orderComboLegsCount = readInt(inputStream);
            for (int i = 0; i < orderComboLegsCount; i++) {
                //final OrderComboLeg orderComboLeg = new OrderComboLeg();
                //order.getOrderComboLegs().add(orderComboLeg);
                double price = readDouble(inputStream);
            }
        }
        if (getVersion() >= 26) {
            final int smartComboRoutingParametersCount = readInt(inputStream);
            for (int i = 0; i < smartComboRoutingParametersCount; i++) {
                //final PairTagValue pairTagValue = new PairTagValue();
                //order.getSmartComboRoutingParameters().add(pairTagValue);
                String tagName = readString(inputStream);
                String pairTagValue = readString(inputStream);
            }
        }
        if (getVersion() >= 15) {
            if (getVersion() >= 20) {
                int scaleInitialLevelSize = readIntMax(inputStream);
                int scaleSubsequentLevelSize = readIntMax(inputStream);
            } else {
                readInt(inputStream);
                int scaleInitialLevelSize = readIntMax(inputStream);
            }
            double scalePriceIncrement = readDoubleMax(inputStream);
            if ((getVersion() >= 28) && (scalePriceIncrement > 0)
                    && (scalePriceIncrement!= Double.MAX_VALUE)) {
                double scalePriceAdjustValue = readDoubleMax(inputStream);
                int scalePriceAdjustInterval = readIntMax(inputStream);
                double scaleProfitOffset = readDoubleMax(inputStream);
                boolean scaleAutoReset = readBoolean(inputStream);
                int scaleInitPosition = readIntMax(inputStream);
                int scaleInitFillQuantity = readIntMax(inputStream);
                boolean scaleRandomPercent = readBoolean(inputStream);
            }
        }

        if (getVersion() >= 24) {
            String hedgeType = readString(inputStream);
            if (StringUtils.isNotEmpty(hedgeType)){
                String hedgeParameter = readString(inputStream);
            }
        }
        if (getVersion() >= 25) {
            boolean optOutSmartRouting= readBoolean(inputStream);
        }
        if (getVersion() >= 19) {
            String clearingAccount = readString(inputStream);
            String clearingIntent =readString(inputStream);
        }
        if (getVersion() >= 22) {
            boolean notHeld = readBoolean(inputStream);
        }
        if (getVersion() >= 20) {
            if (readBoolean(inputStream)) {
                //final UnderlyingCombo underlyingCombo = new UnderlyingCombo();
                //contract.setUnderlyingCombo(underlyingCombo);
                int underlyingComboContractId = readInt(inputStream);
                double underlyingComboDelta = readDouble(inputStream);
                double underlyingComboPrice = readDouble(inputStream);
            }
        }
        if (getVersion() >= 21) {
            String algorithmStrategy = readString(inputStream);
            if (StringUtils.isNotEmpty(algorithmStrategy)) {
                final int algorithmParametersCount = readInt(inputStream);
                for (int i = 0; i < algorithmParametersCount; i++) {
                    //final PairTagValue pairTagValue = new PairTagValue();
                    //order.getAlgorithmParameters().add(pairTagValue);
                    String algorithmStrategyTagName = readString(inputStream);
                    String algorithmStrategyTagValue = readString(inputStream);
                }
            }
        }
        if (getVersion() >= 16) {
            boolean requestPreTradeInformation = readBoolean(inputStream);
        }
        return null;
    }

    //TODO
    protected Order parseOrderExecution(final InputStream inputStream, Order order){

        if (getVersion() >= 16) {
            String orderStatus = readString(inputStream);
            String initialMargin = readString(inputStream);
            String maintenanceMargin = readString(inputStream);
            String equityWithLoan = readString(inputStream);
            double commission = readDoubleMax(inputStream);
            double minCommission = readDoubleMax(inputStream);
            double maxCommission  = readDoubleMax(inputStream);
            String commissionCurrencyCode = readString(inputStream);
            String warningText = readString(inputStream);
        }
        return order;
    }
}
