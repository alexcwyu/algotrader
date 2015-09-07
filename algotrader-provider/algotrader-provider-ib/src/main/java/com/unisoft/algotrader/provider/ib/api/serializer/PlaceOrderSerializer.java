package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.UnderlyingCombo;
import com.unisoft.algotrader.provider.ib.api.model.constants.*;

/**
 * Created by alex on 8/3/15.
 */
public class PlaceOrderSerializer extends Serializer<Order> {

    private RefDataStore refDataStore;

    public PlaceOrderSerializer(RefDataStore refDataStore, int serverCurrentVersion) {
        super(serverCurrentVersion);
        this.refDataStore = refDataStore;
    }


    @Override
    public byte[] serialize(Order order) {
        ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.append(OutgoingMessageId.PLACE_ORDER_REQUEST.getId());
        builder.append(getVersion());
        builder.append(order.getExtOrderId());
        Instrument instrument = refDataStore.getInstrument(order.getInstId());
        appendInstrument(builder, instrument);
        appendOrder(builder, order);
        return builder.toBytes();
    }

    protected void appendInstrument(ByteArrayBuilder builder, Instrument instrument) {
        if (Feature.PLACE_ORDER_BY_CONTRACT_ID.isSupportedByVersion(serverCurrentVersion)) {
            //builder.append(instrument.getInstId());
            builder.append(0); //contract / instrument id
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
        builder.appendEol(); // primary exch
        builder.append(instrument.getCcyId());
        builder.appendEol(); //localsymbol

        if (Feature.SECURITY_ID_TYPE.isSupportedByVersion(serverCurrentVersion)) {
            builder.appendEol(); //SecurityIdentifierCode
            builder.appendEol(); //SecurityId
        }
    }

    private void appendOrder(ByteArrayBuilder builder, Order order){
        builder.append(Action.convert(order.getSide()));
        builder.append((int)order.getOrdQty());
        builder.append(OrderType.convert(order.getOrdType()));

        builder.append(order.getLimitPrice());
        if (order.getStopPrice() != 0.0)
            builder.append(order.getStopPrice());
        else
            builder.appendEol();
        builder.append(TIF.convert(order.getTif()));

        builder.appendEol(); //OCA Group
        builder.appendEol(); //Account
        builder.append("O"); //OpenClose
        builder.append(0); //Origin
        builder.appendEol(); //OrderReference
        builder.append(true); //transmit
        builder.append(0); //ParentId

        builder.append(false); //isBlockOrder
        builder.append(false); // isSweepToFill
        builder.append(0); //DisplaySize
        builder.append(0); //StopTriggerMethod
        builder.append(false); //outsideRegularTradingHours
        builder.append(false); //hidden
        //appendComboLegs(builder);
        builder.appendEol();
        builder.append(0.0); //DiscretionaryAmount
        builder.appendEol(); //GoodAfterDateTime
        builder.appendEol(); //GoodTillDateTime
        builder.appendEol(); //FinancialAdvisorGroup
        builder.appendEol(); //FinancialAdvisorMethod
        builder.appendEol(); //FinancialAdvisorPercentage
        builder.appendEol(); //FinancialAdvisorProfile
        builder.append(0); //ShortSaleSlot
        builder.appendEol(); //DesignatedLocation
        if (Feature.SHORT_SALE_EXEMPT_ORDER_OLD.isSupportedByVersion(serverCurrentVersion)) {
            builder.append(-1); //ExemptionCode
        }
        builder.append(0); //OcaType
        builder.appendEol(); //Rule80A
        builder.appendEol(); //SettlingFirm
        builder.append(false); //all or none
        builder.appendEol(); //MinimumQuantity
        builder.appendEol(); //PercentageOffset
        builder.append(false); //ElectronicTradeOnly
        builder.append(false); //FirmQuoteOnly
        builder.appendEol(); //NbboPriceCap
        builder.append(0); //AuctionStrategy
        builder.appendEol(); //StartingPrice
        builder.appendEol(); //StockReferencePrice
        builder.appendEol(); //Delta
        builder.appendEol(); //LowerStockPriceRange
        builder.appendEol(); //UpperStockPriceRange
        builder.append(false); //overridePercentageConstraints
        builder.appendEol(); //Volatility
        builder.appendEol(); //VolatilityType
        builder.appendEol(); //DeltaNeutralOrderType
        builder.appendEol(); //DeltaNeutralAuxPrice

        appendDeltaNeutralComboOrderByContractId(builder, order);

        builder.append(0); //ContinuouslyUpdate
        builder.appendEol(); //ReferencePriceType
        builder.appendEol(); //TrailingStopPrice
        if (Feature.TRAILING_PERCENT.isSupportedByVersion(serverCurrentVersion)) {
            builder.appendEol(); //TrailingPercent
        }

        appendScaleOrders(builder, order);

        appendHedgingOrder(builder, order);

        if (Feature.OPT_OUT_DEFAULT_SMART_ROUTING_ASX_ORDER.isSupportedByVersion(serverCurrentVersion)) {
            builder.append(false); //OptOutSmartRouting
        }
        if (Feature.POST_TRADE_ALLOCATION.isSupportedByVersion(serverCurrentVersion)) {
            builder.appendEol();//ClearingAccount
            builder.appendEol(); //ClearingIntent
        }
        if (Feature.NOT_HELD.isSupportedByVersion(serverCurrentVersion)) {
            builder.append(false); // NotHeld
        }


        appendDeltaNeutralComboOrder(builder, order);
        appendAlgorithmStrategy(builder, order);
        builder.append(false); //RequestPreTradeInformation
    }

    protected void appendDeltaNeutralComboOrderByContractId(ByteArrayBuilder builder, Order order) {
        //        if (Feature.DELTA_NEUTRAL_COMBO_ORDER_BY_CONTRACT_ID.isSupportedByVersion(getServerCurrentVersion())) {
//            if (StringUtils.isNotEmpty(order.getDeltaNeutralOrderType())) {
//                builder.append(order.getDeltaNeutralContractId());
//                builder.append(order.getDeltaNeutralSettlingFirm());
//                builder.append(order.getDeltaNeutralClearingAccount());
//                builder.append(order.getDeltaNeutralClearingIntent());
//            }
//        }
    }


    protected void appendScaleOrders(ByteArrayBuilder builder, Order order) {

        if (Feature.SCALE_ORDER.isSupportedByVersion(serverCurrentVersion)) {
            builder.appendEol(); //ScaleInitialLevelSize
            builder.appendEol(); //ScaleSubsequentLevelSize
        } else {
            builder.appendEol();
            builder.appendEol(); //ScaleInitialLevelSize
        }
        builder.appendEol(); //ScalePriceIncrement

//        if (Feature.SCALE_ORDERS.isSupportedByVersion(serverCurrentVersion)
//                && (order.getScalePriceIncrement() > 0) && (order.getScalePriceIncrement() != Double.MAX_VALUE)) {
//            builder.append(order.getScalePriceAdjustValue());
//            builder.append(order.getScalePriceAdjustInterval());
//            builder.append(order.getScaleProfitOffset());
//            builder.append(order.isScaleAutoReset());
//            builder.append(order.getScaleInitPosition());
//            builder.append(order.getScaleInitFillQuantity());
//            builder.append(order.isScaleRandomPercent());
//        }
    }


    protected void appendHedgingOrder(ByteArrayBuilder builder, Order order) {
        if (Feature.HEDGING_ORDER.isSupportedByVersion(serverCurrentVersion)) {
            builder.appendEol(); //HedgeType
//            if (StringUtils.isNotEmpty(order.getHedgeType().getInitial())) {
//                builder.append(order.getHedgeParameter());
//            }
        }
    }

    protected void appendDeltaNeutralComboOrder(ByteArrayBuilder builder, Order order) {
        if (Feature.DELTA_NEUTRAL_COMBO_ORDER.isSupportedByVersion(serverCurrentVersion)) {
            final UnderlyingCombo underlyingCombo = null;
            if (underlyingCombo != null) {
//                builder.append(true);
//                builder.append(underlyingCombo.getContractId());
//                builder.append(underlyingCombo.getDelta());
//                builder.append(underlyingCombo.getPrice());
            } else {
                builder.append(false);
            }
        }
    }

    protected void appendAlgorithmStrategy(ByteArrayBuilder builder, Order order) {
        if (Feature.ALGORITHM_ORDER.isSupportedByVersion(getServerCurrentVersion())) {
            builder.appendEol(); //AlgorithmStrategy
//            if (StringUtils.isNotEmpty(order.getAlgorithmStrategy())) {
//                builder.append(order.getAlgorithmParameters().size());
//                for (final PairTagValue pairTagValue : order.getAlgorithmParameters()) {
//                    builder.append(pairTagValue.getTagName());
//                    builder.append(pairTagValue.getValue());
//                }
//            }
        }
    }


    private int getVersion() {
        if (!Feature.NOT_HELD.isSupportedByVersion(serverCurrentVersion)) {
            return 27;
        }
        return 38;
    }

}
