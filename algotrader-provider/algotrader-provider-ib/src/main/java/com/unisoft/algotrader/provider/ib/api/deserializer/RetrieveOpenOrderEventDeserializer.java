package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.model.trading.OrdType;
import com.unisoft.algotrader.model.trading.Side;
import com.unisoft.algotrader.model.trading.TimeInForce;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.event.RetrieveOpenOrderEvent;
import com.unisoft.algotrader.provider.ib.api.model.contract.OptionRight;
import com.unisoft.algotrader.provider.ib.api.model.contract.SecType;
import com.unisoft.algotrader.provider.ib.api.model.order.*;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class RetrieveOpenOrderEventDeserializer extends Deserializer<RetrieveOpenOrderEvent> {

    private final RefDataStore refDataStore;

    public RetrieveOpenOrderEventDeserializer(RefDataStore refDataStore, int serverCurrentVersion){
        super(IncomingMessageId.RETRIEVE_OPEN_ORDER, serverCurrentVersion);
        this.refDataStore = refDataStore;
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBEventHandler eventHandler) {
        final int orderId = readInt(inputStream);
        final Instrument instrument = consumeInstrument(version, inputStream, refDataStore);
        Order order = consumeOrder(version, inputStream, eventHandler, orderId);
        OrderExecution orderExecution = consumeOrderExecution(version, inputStream, order);
        eventHandler.onRetrieveOpenOrderEvent(orderId, instrument, order, orderExecution);
    }

    protected Instrument consumeInstrument(final int version, final InputStream inputStream, final RefDataStore refDataStore) {
        final int instId = (version >= 17)? readInt(inputStream) : 0;
        final String symbol = readString(inputStream);
        final Instrument.InstType instType = SecType.convert(readString(inputStream));
        final String expString = readString(inputStream);
        final double strike = readDouble(inputStream);
        final Instrument.PutCall putCall = OptionRight.convert(readString(inputStream));
        final String multiplier = (version >= 32)? readString(inputStream) : null;
        final String exchange = readString(inputStream);
        final String ccyCode = readString(inputStream);
        final String localSymbol = (version >= 2)?readString(inputStream) : null;
        final String tradingClass = (version >= 32)? readString(inputStream): null;

        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange(IBProvider.PROVIDER_ID, symbol, exchange);
        if (instrument == null){
            throw new RuntimeException("Cannot find instrumnet symbol=" + symbol +", primaryExchange="+exchange);
        }

        return instrument;
    }

    protected Order consumeOrder(final int version, final InputStream inputStream, final IBEventHandler eventHandler, final int extOrderId){

        final Side side = OrderAction.convert(readString(inputStream));
        final int totalQty = readInt(inputStream);
        final OrdType orderType = OrderType.convert(readString(inputStream));

//        final double limitPrice = (version < 29)? readDouble(inputStream) : readDoubleMax(inputStream);
//        final double stopPrice = (version < 30) ? readDouble(inputStream) : readDoubleMax(inputStream);

        final double limitPrice = (version < 29)? readDouble(inputStream) : readDoubleMax(inputStream);
        final double stopPrice = (version < 30) ? readDouble(inputStream) : readDoubleMax(inputStream);

        final TimeInForce tif = TIF.convert(readString(inputStream));

        final String ocaGroupName = readString(inputStream);
        final String accountName = readString(inputStream);

        final String openCloseInstitutional = readString(inputStream);
        final int originInstitutional = readInt(inputStream);

        final String orderReference = readString(inputStream);

        final int clientId = (version >= 3)? readInt(inputStream) : 0;

        int permanentId = 0;
        boolean outsideRegularTradingHours = false;
        boolean hidden = false;
        double discretionaryAmount = 0.0;
        if (version >= 4) {
            permanentId = readInt(inputStream);
            if (version < 18) {
                readBoolean(inputStream);
            } else {
                outsideRegularTradingHours = readBoolean(inputStream);
            }
            hidden = readInt(inputStream) == 1;
            discretionaryAmount = readDouble(inputStream);
        }

        final String goodAfterDateTime = (version >= 5) ? readString(inputStream) : null;

        if (version >= 6) {
            readString(inputStream);
        }

        String financialAdvisorGroup = null;
        String financialAdvisorMethod = null;
        String financialAdvisorPercentage = null;
        String financialAdvisorProfile = null;

        if (version >= 7) {
            financialAdvisorGroup = readString(inputStream);
            financialAdvisorMethod = readString(inputStream);
            financialAdvisorPercentage = readString(inputStream);
            financialAdvisorProfile = readString(inputStream);
        }

        final String goodTillDateTime = (version >= 8) ? readString(inputStream) : null;

        if (version >= 9) {
            String Rule80A = readString(inputStream);
            double percentageOffset =readDoubleMax(inputStream);
            String settlingFirm = readString(inputStream);
            int shortSaleSlot = readInt(inputStream);
            String designatedLocation = readString(inputStream);
            if (serverCurrentVersion == 51) {
                readInt(inputStream);
            } else if (version >= 23) {
                int exemptionCode = readInt(inputStream);
            }
            int auctionStrategy = readInt(inputStream);
            double startingPrice = readDoubleMax(inputStream);
            double stockReferencePrice = readDoubleMax(inputStream);
            double delta = readDoubleMax(inputStream);
            double lowerStockPriceRange = readDoubleMax(inputStream);
            double upperStockPriceRange = readDoubleMax(inputStream);
            int displaySize = readInt(inputStream);
            if (version < 18) {
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
        if (version >= 10) {
            int parentId = readInt(inputStream);
            int stopTriggerMethod = readInt(inputStream);
        }
        if (version >= 11) {
            double volatility = readDoubleMax(inputStream);
            int volatilityType = readInt(inputStream);
            if (version == 11) {
                String deltaNeutralOrderType  = (readInt(inputStream) == 0 ? "NONE" : "MKT");
            } else {
                String deltaNeutralOrderType = readString(inputStream);
                double deltaNeutralAuxPrice = readDoubleMax(inputStream);
                if ((version >= 27)
                        && StringUtils.isNotEmpty(deltaNeutralOrderType)) {
                    int deltaNeutralContractId = readInt(inputStream);
                    String deltaNeutralSettlingFirm = readString(inputStream);
                    String deltaNeutralClearingAccount = readString(inputStream);
                    String deltaNeutralClearingIntent = readString(inputStream);
                }
                if ((version >= 31)
                        && StringUtils.isNotEmpty(deltaNeutralOrderType)) {
                    String deltaNeutralOpenClose = readString(inputStream);
                    boolean deltaNeutralShortSale = readBoolean(inputStream);
                    int deltaNeutralShortSaleSlot  = readInt(inputStream);
                    String deltaNeutralDesignatedLocation = readString(inputStream);
                }
            }
            int continuouslyUpdate = readInt(inputStream);
            if (serverCurrentVersion == 26) {
                double lowerStockPriceRange = readDouble(inputStream);
                double upperStockPriceRange = readDouble(inputStream);
            }
            int referencePriceType = readInt(inputStream);
        }
        if (version >= 13) {
            double trailingStopPrice = readDoubleMax(inputStream);
        }
        if (version >= 30) {
            double trailingPercent = readDoubleMax(inputStream);
        }
        if (version >= 14) {
            double basisPoint = readDoubleMax(inputStream);
            int basisPointType = readIntMax(inputStream);
            String comboLegsDescription = readString(inputStream);
        }
        if (version >= 29) {
            final int comboLegsCount = readInt(inputStream);
            for (int i = 0; i < comboLegsCount; i++) {
                //final ComboLeg comboLeg = new ComboLeg();
                //contract.addComboLeg(comboLeg);
                int contractId = readInt(inputStream);
                int ratio = readInt(inputStream);
                final Side side1 = OrderAction.convert(readString(inputStream));
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
        if (version >= 26) {
            final int smartComboRoutingParametersCount = readInt(inputStream);
            for (int i = 0; i < smartComboRoutingParametersCount; i++) {
                //final PairTagValue pairTagValue = new PairTagValue();
                //order.getSmartComboRoutingParameters().add(pairTagValue);
                String tagName = readString(inputStream);
                String pairTagValue = readString(inputStream);
            }
        }
        if (version >= 15) {
            if (version >= 20) {
                int scaleInitialLevelSize = readIntMax(inputStream);
                int scaleSubsequentLevelSize = readIntMax(inputStream);
            } else {
                readInt(inputStream);
                int scaleInitialLevelSize = readIntMax(inputStream);
            }
            double scalePriceIncrement = readDoubleMax(inputStream);
            if ((version >= 28) && (scalePriceIncrement > 0)
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

        if (version >= 24) {
            String hedgeType = readString(inputStream);
            if (StringUtils.isNotEmpty(hedgeType)){
                String hedgeParameter = readString(inputStream);
            }
        }
        if (version >= 25) {
            boolean optOutSmartRouting= readBoolean(inputStream);
        }
        if (version >= 19) {
            String clearingAccount = readString(inputStream);
            String clearingIntent =readString(inputStream);
        }
        if (version >= 22) {
            boolean notHeld = readBoolean(inputStream);
        }
        if (version >= 20) {
            if (readBoolean(inputStream)) {
                //final UnderlyingCombo underlyingCombo = new UnderlyingCombo();
                //contract.setUnderlyingCombo(underlyingCombo);
                int underlyingComboContractId = readInt(inputStream);
                double underlyingComboDelta = readDouble(inputStream);
                double underlyingComboPrice = readDouble(inputStream);
            }
        }
        if (version >= 21) {
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
        if (version >= 16) {
            boolean requestPreTradeInformation = readBoolean(inputStream);
        }
        //TODO lookup and update order
        return null;
    }

    //TODO
    protected OrderExecution consumeOrderExecution(final int version, final InputStream inputStream, Order order){

        if (version >= 16) {

            OrderExecution orderExecution = new OrderExecution();
            orderExecution.setOrderStatus(OrderStatus.fromLabel(readString(inputStream)));
            orderExecution.setInitialMargin(readString(inputStream));
            orderExecution.setMaintenanceMargin(readString(inputStream));
            orderExecution.setEquityWithLoan(readString(inputStream));
            orderExecution.setCommission(readDoubleMax(inputStream));
            orderExecution.setMinCommission(readDoubleMax(inputStream));
            orderExecution.setMaxCommission(readDoubleMax(inputStream));
            orderExecution.setCommissionCurrencyCode(readString(inputStream));
            orderExecution.setWarningText(readString(inputStream));

            return orderExecution;
        }
        return null;
    }
}