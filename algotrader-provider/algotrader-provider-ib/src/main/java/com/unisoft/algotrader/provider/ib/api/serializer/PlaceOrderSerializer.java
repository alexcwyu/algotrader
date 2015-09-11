package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.contract.OptionRight;
import com.unisoft.algotrader.provider.ib.api.model.contract.SecType;
import com.unisoft.algotrader.provider.ib.api.model.order.OrderAction;
import com.unisoft.algotrader.provider.ib.api.model.order.OrderType;
import com.unisoft.algotrader.provider.ib.api.model.order.TIF;
import com.unisoft.algotrader.provider.ib.api.model.system.Feature;
import com.unisoft.algotrader.provider.ib.api.model.system.IBModelUtils;
import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/3/15.
 */
public class PlaceOrderSerializer extends Serializer{

    private RefDataStore refDataStore;

    public PlaceOrderSerializer(RefDataStore refDataStore, int serverCurrentVersion) {
        super(serverCurrentVersion, OutgoingMessageId.PLACE_ORDER_REQUEST);
        this.refDataStore = refDataStore;
    }

    public byte[] serialize(Order order) {
        ByteArrayBuilder builder = getByteArrayBuilder();

        builder.append(messageId.getId());
        builder.append(getVersion());
        builder.append(order.getExtOrderId());
        Instrument instrument = refDataStore.getInstrument(order.getInstId());
        appendInstrument(builder, instrument);
        appendOrder(builder, instrument, order);
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

        if (Feature.TRADING_CLASS.isSupportedByVersion(serverCurrentVersion)) {
            builder.appendEol(); // trading class
        }

        if (Feature.SECURITY_ID_TYPE.isSupportedByVersion(serverCurrentVersion)) {
            builder.appendEol(); //SecurityIdentifierCode
            builder.appendEol(); //SecurityId
        }
    }

    private void appendOrder(ByteArrayBuilder builder, Instrument instrument, Order order){
        builder.append(OrderAction.convert(order.getSide()));
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
        appendCombo(builder, instrument);
        appendOrderCombo(builder, instrument, order);
        appendOrderOptions(builder, instrument, order);
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
        if (serverCurrentVersion < 38) {
            builder.append(false);
        }
        builder.appendEol(); //Rule80A
        builder.appendEol(); //SettlingFirm
        builder.append(false); //all or none
        builder.appendEol(); //MinimumQuantity
        builder.appendEol(); //PercentageOffset
        builder.append(false); //ElectronicTradeOnly
        builder.append(false); //FirmQuoteOnly
        builder.appendEol(); //NbboPriceCap
        builder.appendEol(); //AuctionStrategy
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
        if (serverCurrentVersion == 26){
            builder.appendEol(); //LowerStockPriceRange
            builder.appendEol(); //UpperStockPriceRange
        }
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

        if (Feature.LINKING.isSupportedByVersion(getServerCurrentVersion())) {
            builder.appendEol(); // chartOptions
        }
    }

    private void appendCombo(ByteArrayBuilder builder, Instrument instrument) {
        //should never happen
        if (SecType.COMBO.equals(SecType.convert(instrument.getType()))) {
            builder.append(0);
//            for (final ComboLeg comboLeg : contract.getComboLegs()) {
//                builder.append(comboLeg.getContractId());
//                builder.append(comboLeg.getRatio());
//                builder.append(comboLeg.getOrderAction().getAbbreviation());
//                builder.append(comboLeg.getExchange());
//            }
        }
    }

    private void appendOrderCombo(ByteArrayBuilder builder, Instrument instrument, Order order) {
        //should never happen
        if (Feature.ORDER_COMBO_LEGS_PRICE.isSupportedByVersion(serverCurrentVersion)
                && SecType.COMBO.equals(SecType.convert(instrument.getType()))) {
            builder.append(0);
//            for (final ComboLeg comboLeg : contract.getComboLegs()) {
//                builder.append(comboLeg.getContractId());
//                builder.append(comboLeg.getRatio());
//                builder.append(comboLeg.getOrderAction().getAbbreviation());
//                builder.append(comboLeg.getExchange());
//            }
        }
    }

    private void appendOrderOptions(ByteArrayBuilder builder, Instrument instrument, Order order) {
        //should never happen
        if (Feature.SMART_COMBO_ROUTING_PARAMETER.isSupportedByVersion(serverCurrentVersion)
                && SecType.COMBO.equals(SecType.convert(instrument.getType()))) {
            builder.append(0);
//            for (final ComboLeg comboLeg : contract.getComboLegs()) {
//                builder.append(comboLeg.getContractId());
//                builder.append(comboLeg.getRatio());
//                builder.append(comboLeg.getOrderAction().getAbbreviation());
//                builder.append(comboLeg.getExchange());
//            }
        }
    }

    protected void appendDeltaNeutralComboOrderByContractId(ByteArrayBuilder builder, Order order) {
        if (Feature.DELTA_NEUTRAL_COMBO_ORDER_BY_CONTRACT_ID.isSupportedByVersion(getServerCurrentVersion())) {
//            if (StringUtils.isNotEmpty(order.getDeltaNeutralOrderType())) {
//                builder.append(order.getDeltaNeutralContractId());
//                builder.append(order.getDeltaNeutralSettlingFirm());
//                builder.append(order.getDeltaNeutralClearingAccount());
//                builder.append(order.getDeltaNeutralClearingIntent());
//            }
        }

        if (Feature.DELTA_NEUTRAL_OPEN_CLOSE.isSupportedByVersion(getServerCurrentVersion())) {
//            if (StringUtils.isNotEmpty(order.getDeltaNeutralOrderType())) {
//                builder.append(order.getDeltaNeutralOpenClose());
//                builder.append(order.getDeltaNeutralShortSale());
//                builder.append(order.getDeltaNeutralShortSaleSlot());
//                builder.append(order.getDeltaNeutralDesignatedLocation());
//            }
        }
    }


    protected void appendScaleOrders(ByteArrayBuilder builder, Order order) {

        if (Feature.SCALE_ORDERS2.isSupportedByVersion(serverCurrentVersion)) {
            builder.appendEol(); //ScaleInitialLevelSize
            builder.appendEol(); //ScaleSubsequentLevelSize
        } else {
            builder.appendEol();
            builder.appendEol(); //ScaleInitialLevelSize
        }
        builder.appendEol(); //ScalePriceIncrement

        if (Feature.SCALE_ORDERS3.isSupportedByVersion(serverCurrentVersion)) {
//            if ((order.getScalePriceIncrement() > 0) && (order.getScalePriceIncrement() != Double.MAX_VALUE)) {
//            builder.append(order.getScalePriceAdjustValue());
//            builder.append(order.getScalePriceAdjustInterval());
//            builder.append(order.getScaleProfitOffset());
//            builder.append(order.isScaleAutoReset());
//            builder.append(order.getScaleInitPosition());
//            builder.append(order.getScaleInitFillQuantity());
//            builder.append(order.isScaleRandomPercent());
//            }
        }

        if (Feature.SCALE_TABLE.isSupportedByVersion(serverCurrentVersion)) {
            builder.appendEol(); //ScaleTable
            builder.appendEol(); //ActiveStartTime
            builder.appendEol(); //ActiveStopTime
        }
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
//            final UnderlyingCombo underlyingCombo = null;
//            if (underlyingCombo != null) {
//                builder.append(true);
//                builder.append(underlyingCombo.getInstId());
//                builder.append(underlyingCombo.getDelta());
//                builder.append(underlyingCombo.getPrice());
//            } else {
                builder.append(false);
//            }
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
        return 42;
    }

}
