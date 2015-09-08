package com.unisoft.algotrader.provider.ib.api.model.data;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alex on 8/30/15.
 */
public enum TickType {

    UNKNOWN(-1),
    BID_SIZE(0),
    BID_PRICE(1),
    ASK_PRICE(2),
    ASK_SIZE(3),
    LAST_PRICE(4),
    LAST_SIZE(5),
    DAY_HIGH(6),
    DAY_LOW(7),
    VOLUME(8),
    CLOSE(9),
    BID_OPTION_COMPUTATION(10),
    ASK_OPTION_COMPUTATION(11),
    LAST_OPTION_COMPUTATION(12),
    MODEL_OPTION_COMPUTATION(13),
    DAY_OPEN(14),
    LOW_13_WEEK(15),
    HIGH_13_WEEK(16),
    LOW_26_WEEK(17),
    HIGH_26_WEEK(18),
    LOW_52_WEEK(19),
    HIGH_52_WEEK(20),
    AVERAGE_VOLUME(21),
    OPEN_INTEREST(22),
    OPTION_HISTORICAL_VOLATILITY(23),
    OPTION_IMPLIED_VOLATILITY(24),
    OPTION_BID_EXCHANGE(25),
    OPTION_ASK_EXCHANGE(26),
    OPTION_CALL_OPEN_INTEREST(27),
    OPTION_PUT_OPEN_INTEREST(28),
    OPTION_CALL_VOLUME(29),
    OPTION_PUT_VOLUME(30),
    INDEX_FUTURE_PREMIUM(31),
    BID_EXCHANGE(32),
    ASK_EXCHANGE(33),
    AUCTION_VOLUME(34),
    AUCTION_PRICE(35),
    AUCTION_IMBALANCE(36),
    MARK_PRICE(37),
    BID_EFP_COMPUTATION(38),
    ASK_EFP_COMPUTATION(39),
    LAST_EFP_COMPUTATION(40),
    OPEN_EFP_COMPUTATION(41),
    HIGH_EFP_COMPUTATION(42),
    LOW_EFP_COMPUTATION(43),
    CLOSE_EFP_COMPUTATION(44),
    LAST_TIMESTAMP(45),
    SHORTABLE(46),
    FUNDAMENTAL_RATIOS(47),
    REAL_TIME_VOLUME(48),
    HALTED(49),
    BID_YIELD(50),
    ASK_YIELD(51),
    LAST_YIELD(52),
    CUSTOM_OPTION_COMPUTATION(53),
    TRADE_COUNT(54),
    TRADE_RATE(55),
    VOLUME_RATE(56);

    private final int value;
    private static final Map<Integer, TickType> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final TickType tickType : values()) {
            MAP.put(tickType.getValue(), tickType);
        }
    }

    private TickType(final int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }

    public static final TickType fromValue(final int value) {
        if (MAP.containsKey(value)) {
            return MAP.get(value);
        }
        return UNKNOWN;
    }
}
