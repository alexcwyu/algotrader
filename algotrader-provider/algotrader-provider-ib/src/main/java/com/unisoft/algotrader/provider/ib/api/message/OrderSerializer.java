package com.unisoft.algotrader.provider.ib.api.message;

import ch.aonyx.broker.ib.api.Feature;
import ch.aonyx.broker.ib.api.OutgoingMessageId;
import com.unisoft.algotrader.model.event.EventBusManager;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.IBUtils;

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

    private void appendInstrument(ByteArrayBuilder builder, Instrument instrument){
        if (Feature.PLACE_ORDER_BY_CONTRACT_ID.isSupportedByVersion(serverCurrentVersion)) {
            builder.append(instrument.getInstId());
        }
        builder.append(instrument.getSymbol(IBProvider.PROVIDER_ID));
        builder.append(IBUtils.convertSecurityType(instrument.getType()));
        builder.append(IBUtils.convertDate(instrument.getExpiryDate().getTime()));
        builder.append(instrument.getStrike());
        builder.append(IBUtils.convertPutCall(instrument.getPutCall()));
        builder.append(instrument.getFactor());
        builder.append(instrument.getExchId(IBProvider.PROVIDER_ID));
        builder.append(instrument.getExchId(IBProvider.PROVIDER_ID));
        builder.append(instrument.getCcyId());
        //TODO
//        builder.append(instrument.getLocalSymbol());
        //TODO
//        if (Feature.SECURITY_ID_TYPE.isSupportedByVersion(serverCurrentVersion)) {
//            builder.append(instrument.getSecurityIdentifierCode().getAcronym());
//            builder.append(instrument.getSecurityId());
//        }
    }

    private void appendOrder(ByteArrayBuilder builder, Order order){
        builder.append(IBUtils.convertAction(order.getSide()));
        builder.append(order.getOrdQty());
        builder.append(order.)
    }

    /*
    *
        private void appendOrder(final RequestBuilder builder) {
        builder.append(order.getAction().getAbbreviation());
        builder.append(order.getTotalQuantity());
        builder.append(order.getOrderType().getAbbreviation());
        final double limitPrice = order.getLimitPrice();
        if (Feature.ORDER_COMBO_LEGS_PRICE.isSupportedByVersion(getServerCurrentVersion())) {
            builder.append(limitPrice);
        } else {
            builder.append(limitPrice == Double.MAX_VALUE ? 0 : limitPrice);
        }
        final double stopPrice = order.getStopPrice();
        if (Feature.TRAILING_PERCENT.isSupportedByVersion(getServerCurrentVersion())) {
            builder.append(stopPrice);
        } else {
            builder.append(stopPrice == Double.MAX_VALUE ? 0 : stopPrice);
        }
        builder.append(order.getTimeInForce().getAbbreviation());
        builder.append(order.getOcaGroupName());
        builder.append(order.getAccountName());
        builder.append(order.getOpenClose().getInitial());
        builder.append(order.getOrigin().getValue());
        builder.append(order.getOrderReference());
        builder.append(order.isTransmit());
        builder.append(toInternalId(order.getParentId()));
        builder.append(order.isBlockOrder());
        builder.append(order.isSweepToFill());
        builder.append(order.getDisplaySize());
        builder.append(order.getStopTriggerMethod().getValue());
        builder.append(order.isOutsideRegularTradingHours());
        builder.append(order.isHidden());
        appendComboLegs(builder);
        builder.append("");
        builder.append(order.getDiscretionaryAmount());
        builder.append(order.getGoodAfterDateTime());
        builder.append(order.getGoodTillDateTime());
        builder.append(order.getFinancialAdvisorGroup());
        builder.append(order.getFinancialAdvisorMethod());
        builder.append(order.getFinancialAdvisorPercentage());
        builder.append(order.getFinancialAdvisorProfile());
        builder.append(order.getShortSaleSlot().getValue());
        builder.append(order.getDesignatedLocation());
        if (Feature.SHORT_SALE_EXEMPT_ORDER_OLD.isSupportedByVersion(getServerCurrentVersion())) {
            builder.append(order.getExemptionCode());
        }
        builder.append(order.getOcaType().getValue());
        builder.append(order.getRule80A().getInitial());
        builder.append(order.getSettlingFirm());
        builder.append(order.isAllOrNone());
        builder.append(order.getMinimumQuantity());
        builder.append(order.getPercentageOffset());
        builder.append(order.isElectronicTradeOnly());
        builder.append(order.isFirmQuoteOnly());
        builder.append(order.getNbboPriceCap());
        builder.append(order.getAuctionStrategy().getValue());
        builder.append(order.getStartingPrice());
        builder.append(order.getStockReferencePrice());
        builder.append(order.getDelta());
        builder.append(order.getLowerStockPriceRange());
        builder.append(order.getUpperStockPriceRange());
        builder.append(order.isOverridePercentageConstraints());
        builder.append(order.getVolatility());
        builder.append(order.getVolatilityType().getValue());
        builder.append(order.getDeltaNeutralOrderType());
        builder.append(order.getDeltaNeutralAuxPrice());
        if (Feature.DELTA_NEUTRAL_COMBO_ORDER_BY_CONTRACT_ID.isSupportedByVersion(getServerCurrentVersion())) {
            if (StringUtils.isNotEmpty(order.getDeltaNeutralOrderType())) {
                builder.append(order.getDeltaNeutralContractId());
                builder.append(order.getDeltaNeutralSettlingFirm());
                builder.append(order.getDeltaNeutralClearingAccount());
                builder.append(order.getDeltaNeutralClearingIntent());
            }
        }
        builder.append(order.getContinuouslyUpdate());
        builder.append(order.getReferencePriceType().getValue());
        builder.append(order.getTrailingStopPrice());
        if (Feature.TRAILING_PERCENT.isSupportedByVersion(getServerCurrentVersion())) {
            builder.append(order.getTrailingPercent());
        }
        if (Feature.SCALE_ORDER.isSupportedByVersion(getServerCurrentVersion())) {
            builder.append(order.getScaleInitialLevelSize());
            builder.append(order.getScaleSubsequentLevelSize());
        } else {
            builder.append("");
            builder.append(order.getScaleInitialLevelSize());
        }
        builder.append(order.getScalePriceIncrement());
        if (Feature.SCALE_ORDERS.isSupportedByVersion(getServerCurrentVersion())
                && (order.getScalePriceIncrement() > 0) && (order.getScalePriceIncrement() != Double.MAX_VALUE)) {
            builder.append(order.getScalePriceAdjustValue());
            builder.append(order.getScalePriceAdjustInterval());
            builder.append(order.getScaleProfitOffset());
            builder.append(order.isScaleAutoReset());
            builder.append(order.getScaleInitPosition());
            builder.append(order.getScaleInitFillQuantity());
            builder.append(order.isScaleRandomPercent());
        }
        if (Feature.HEDGING_ORDER.isSupportedByVersion(getServerCurrentVersion())) {
            builder.append(order.getHedgeType().getInitial());
            if (StringUtils.isNotEmpty(order.getHedgeType().getInitial())) {
                builder.append(order.getHedgeParameter());
            }
        }
        if (Feature.OPT_OUT_DEFAULT_SMART_ROUTING_ASX_ORDER.isSupportedByVersion(getServerCurrentVersion())) {
            builder.append(order.isOptOutSmartRouting());
        }
        if (Feature.POST_TRADE_ALLOCATION.isSupportedByVersion(getServerCurrentVersion())) {
            builder.append(order.getClearingAccount());
            builder.append(order.getClearingIntent());
        }
        if (Feature.NOT_HELD.isSupportedByVersion(getServerCurrentVersion())) {
            builder.append(order.isNotHeld());
        }
        if (Feature.DELTA_NEUTRAL_COMBO_ORDER.isSupportedByVersion(getServerCurrentVersion())) {
            final UnderlyingCombo underlyingCombo = contract.getUnderlyingCombo();
            if (underlyingCombo != null) {
                builder.append(true);
                builder.append(underlyingCombo.getContractId());
                builder.append(underlyingCombo.getDelta());
                builder.append(underlyingCombo.getPrice());
            } else {
                builder.append(false);
            }
        }
        if (Feature.ALGORITHM_ORDER.isSupportedByVersion(getServerCurrentVersion())) {
            builder.append(order.getAlgorithmStrategy());
            if (StringUtils.isNotEmpty(order.getAlgorithmStrategy())) {
                builder.append(order.getAlgorithmParameters().size());
                for (final PairTagValue pairTagValue : order.getAlgorithmParameters()) {
                    builder.append(pairTagValue.getTagName());
                    builder.append(pairTagValue.getValue());
                }
            }
        }
        builder.append(order.isRequestPreTradeInformation());
    }

    private void appendComboLegs(final RequestBuilder builder) {
        if (SecurityType.COMBO.equals(contract.getSecurityType())) {
            builder.append(contract.getComboLegs().size());
            for (final ComboLeg comboLeg : contract.getComboLegs()) {
                builder.append(comboLeg.getContractId());
                builder.append(comboLeg.getRatio());
                builder.append(comboLeg.getOrderAction().getAbbreviation());
                builder.append(comboLeg.getExchange());
                builder.append(comboLeg.getOpenClose().getValue());
                builder.append(comboLeg.getShortSaleSlotValue().getValue());
                builder.append(comboLeg.getDesignatedLocation());
                if (Feature.SHORT_SALE_EXEMPT_ORDER_OLD.isSupportedByVersion(getServerCurrentVersion())) {
                    builder.append(comboLeg.getExemptionCode());
                }
            }

            if (Feature.ORDER_COMBO_LEGS_PRICE.isSupportedByVersion(getServerCurrentVersion())) {
                builder.append(order.getOrderComboLegs().size());
                for (final OrderComboLeg orderComboLeg : order.getOrderComboLegs()) {
                    builder.append(orderComboLeg.getPrice());
                }
            }

            if (Feature.SMART_COMBO_ROUTING_PARAMETER.isSupportedByVersion(getServerCurrentVersion())) {
                builder.append(order.getSmartComboRoutingParameters().size());
                for (final PairTagValue pairTagValue : order.getSmartComboRoutingParameters()) {
                    builder.append(pairTagValue.getTagName());
                    builder.append(pairTagValue.getValue());
                }
            }
        }
    }
    * */

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
