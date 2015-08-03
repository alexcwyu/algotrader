package com.unisoft.algotrader.provider.ib.api.message;

import ch.aonyx.broker.ib.api.Feature;
import ch.aonyx.broker.ib.api.OutgoingMessageId;
import com.unisoft.algotrader.model.event.EventBusManager;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.IBUtils;
import com.unisoft.algotrader.provider.ib.api.IBConstants;

import java.io.InputStream;

/**
 * Created by alex on 8/3/15.
 */
public class OrderSerializer extends EventSerializer<Order>{

    private RefDataStore refDataStore;

    public OrderSerializer(int serverCurrentVersion, EventBusManager eventBusManager, RefDataStore refDataStore) {
        super(serverCurrentVersion, eventBusManager);
        this.refDataStore = refDataStore;
    }

    @Override
    public void publishEvent(InputStream inputStream, int serverCurrentVersion, EventBusManager eventBusManager) {
        throw new UnsupportedOperationException();
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

    private void appendInstrument(ByteArrayBuilder builder, Instrument instrument) {
        if (Feature.PLACE_ORDER_BY_CONTRACT_ID.isSupportedByVersion(serverCurrentVersion)) {
            builder.append(instrument.getInstId());
        }
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
//        if (Feature.DELTA_NEUTRAL_COMBO_ORDER_BY_CONTRACT_ID.isSupportedByVersion(getServerCurrentVersion())) {
//            if (StringUtils.isNotEmpty(order.getDeltaNeutralOrderType())) {
//                builder.append(order.getDeltaNeutralContractId());
//                builder.append(order.getDeltaNeutralSettlingFirm());
//                builder.append(order.getDeltaNeutralClearingAccount());
//                builder.append(order.getDeltaNeutralClearingIntent());
//            }
//        }
        builder.append(0); //ContinuouslyUpdate
        builder.appendEol(); //ReferencePriceType
        builder.append(0.0); //TrailingStopPrice
        if (Feature.TRAILING_PERCENT.isSupportedByVersion(serverCurrentVersion)) {
            builder.append(0.0); //TrailingPercent
        }
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
        if (Feature.HEDGING_ORDER.isSupportedByVersion(serverCurrentVersion)) {
            builder.appendEol(); //HedgeType
//            if (StringUtils.isNotEmpty(order.getHedgeType().getInitial())) {
//                builder.append(order.getHedgeParameter());
//            }
        }
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
        builder.append(false); //RequestPreTradeInformation
    }


    private int getVersion() {
        if (!Feature.NOT_HELD.isSupportedByVersion(serverCurrentVersion)) {
            return 27;
        }
        return 38;
    }

    @Override
    public Order deserialize(InputStream inputStream) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Order deserialize(byte[] bytes) {
        throw new UnsupportedOperationException();
    }
}
