package com.unisoft.algotrader.provider.ib.api;

/**
 * Created by alex on 8/3/15.
 */
public enum Feature {
    POST_TRADE_ALLOCATION(39),
    REUTERS_FUNDAMENTAL_DATA(40),
    DELTA_NEUTRAL_COMBO_ORDER(40),
    SCALE_ORDER(40),
    CONTRACT_SPECIFICATION_MARKER(40),
    ALGORITHM_ORDER(41),
    EXECUTION_MARKER(42), NOT_HELD(44),
    SECURITY_ID_TYPE(45),
    PLACE_ORDER_BY_CONTRACT_ID(46),
    MARKET_DATA_REQUEST_BY_CONTRACT_ID(47),
    CALCULATE_IMPLIED_VOLATILITY(49),
    CALCULATE_OPTION_PRICE(50),
    CANCEL_CALCULATE_IMPLIED_VOLATILITY(50),
    CANCEL_CALCULATE_OPTION_PRICE(50),
    SHORT_SALE_EXEMPT_ORDER_OLD(51),
    SHORT_SALE_EXEMPT_ORDER(52),
    GLOBAL_CANCEL_ORDER_REQUEST(53),
    HEDGING_ORDER(54),
    MARKET_DATA_TYPE(55),
    OPT_OUT_DEFAULT_SMART_ROUTING_ASX_ORDER(56),
    SMART_COMBO_ROUTING_PARAMETER(57),
    DELTA_NEUTRAL_COMBO_ORDER_BY_CONTRACT_ID(58),
    SCALE_ORDERS(60),
    ORDER_COMBO_LEGS_PRICE(61),
    TRAILING_PERCENT(62),
    DELTA_NEUTRAL_OPEN_CLOSE(66),
    ACCT_SUMMARY(67),
    TRADING_CLASS(68),
    SCALE_TABLE(69),
    LINKING(70);

    private final int version;

    private Feature(final int version) {
        this.version = version;
    }

    private int getVersion() {
        return version;
    }

    public boolean isSupportedByVersion(final int version) {
        return version >= getVersion();
    }
}
