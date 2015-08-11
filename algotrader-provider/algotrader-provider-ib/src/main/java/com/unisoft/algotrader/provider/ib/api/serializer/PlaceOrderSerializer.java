package com.unisoft.algotrader.provider.ib.api.serializer;

import ch.aonyx.broker.ib.api.Feature;
import ch.aonyx.broker.ib.api.OutgoingMessageId;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ib.api.IBConstants;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by alex on 8/3/15.
 */
public class PlaceOrderSerializer extends Serializer<Order> {

    private RefDataStore refDataStore;
    private AtomicInteger counter;

    public PlaceOrderSerializer(AtomicInteger counter, RefDataStore refDataStore, int serverCurrentVersion) {
        super(serverCurrentVersion);
        this.counter = counter;
        this.refDataStore = refDataStore;
    }


    @Override
    public byte[] serialize(Order order) {
        ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.append(OutgoingMessageId.PLACE_ORDER_REQUEST.getId());
        builder.append(getVersion());
        builder.append(order.orderId);
        Instrument instrument = refDataStore.getInstrument(order.getInstId());
        appendInstrument(builder, instrument);
        appendOrder(builder, order);
        return builder.toBytes();
    }

    protected void appendInstrument(ByteArrayBuilder builder, Instrument instrument) {
        if (Feature.PLACE_ORDER_BY_CONTRACT_ID.isSupportedByVersion(serverCurrentVersion)) {
            builder.append(instrument.getInstId());
        }
        super.appendInstrument(builder, instrument);
        if (Feature.SECURITY_ID_TYPE.isSupportedByVersion(serverCurrentVersion)) {
            builder.appendEol(); //SecurityIdentifierCode
            builder.appendEol(); //SecurityId
        }
    }

    private void appendOrder(ByteArrayBuilder builder, Order order){
        builder.append(IBConstants.Action.convert(order.getSide()));
        builder.append(order.getOrdQty());
        builder.append(IBConstants.OrderType.convert(order.getOrdType()));

        builder.append(order.getLimitPrice());
        builder.append(order.getStopPrice());
        builder.append(IBConstants.TIF.convert(order.getTif()));

        builder.appendEol(); //OCA Group
        builder.appendEol(); //Account
        builder.appendEol(); //OpenClose
        builder.appendEol(); //Origin
        builder.appendEol(); //OrderReference
        builder.appendEol(); //OrderReference
        builder.append(false); //transmit
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
            builder.append(0); //ExemptionCode
        }
        builder.append(0); //OcaType
        builder.appendEol(); //Rule80A
        builder.appendEol(); //SettlingFirm
        builder.append(0); //all or none
        builder.append(0); //MinimumQuantity
        builder.append(0.0); //PercentageOffset
        builder.append(false); //ElectronicTradeOnly
        builder.append(false); //FirmQuoteOnly
        builder.append(0.0); //NbboPriceCap
        builder.append(0); //AuctionStrategy
        builder.append(0.0); //StartingPrice
        builder.append(0.0); //StockReferencePrice
        builder.append(0.0); //Delta
        builder.append(0.0); //LowerStockPriceRange
        builder.append(0.0); //UpperStockPriceRange
        builder.append(false); //overridePercentageConstraints
        builder.append(0.0); //Volatility
        builder.appendEol(); //VolatilityType
        builder.appendEol(); //DeltaNeutralOrderType
        builder.appendEol(); //DeltaNeutralAuxPrice

        appendDeltaNeutralComboOrderByContractId(builder, order);

        builder.append(0); //ContinuouslyUpdate
        builder.appendEol(); //ReferencePriceType
        builder.append(0.0); //TrailingStopPrice
        if (Feature.TRAILING_PERCENT.isSupportedByVersion(serverCurrentVersion)) {
            builder.append(0.0); //TrailingPercent
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
            builder.append(0); //ScaleInitialLevelSize
            builder.append(0); //ScaleSubsequentLevelSize
        } else {
            builder.appendEol();
            builder.append(0); //ScaleInitialLevelSize
        }
        builder.append(0.0); //ScalePriceIncrement

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
        //        if (Feature.DELTA_NEUTRAL_COMBO_ORDER.isSupportedByVersion(serverCurrentVersion)) {
//            final UnderlyingCombo underlyingCombo = contract.getUnderlyingCombo();
//            if (underlyingCombo != null) {
//                builder.append(true);
//                builder.append(underlyingCombo.getContractId());
//                builder.append(underlyingCombo.getDelta());
//                builder.append(underlyingCombo.getPrice());
//            } else {
        builder.append(false);
//            }
//        }
    }

    protected void appendAlgorithmStrategy(ByteArrayBuilder builder, Order order) {
        //        if (Feature.ALGORITHM_ORDER.isSupportedByVersion(getServerCurrentVersion())) {
//            builder.append(order.getAlgorithmStrategy());
//            if (StringUtils.isNotEmpty(order.getAlgorithmStrategy())) {
//                builder.append(order.getAlgorithmParameters().size());
//                for (final PairTagValue pairTagValue : order.getAlgorithmParameters()) {
//                    builder.append(pairTagValue.getTagName());
//                    builder.append(pairTagValue.getValue());
//                }
//            }
//        }
    }


    private int getVersion() {
        if (!Feature.NOT_HELD.isSupportedByVersion(serverCurrentVersion)) {
            return 27;
        }
        return 38;
    }

}
