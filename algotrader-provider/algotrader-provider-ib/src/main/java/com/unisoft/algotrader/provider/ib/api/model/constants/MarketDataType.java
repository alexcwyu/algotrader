package com.unisoft.algotrader.provider.ib.api.model.constants;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alex on 8/30/15.
 */
public enum MarketDataType {

    UNKNOWN(-1),
    REAL_TIME(1),
    FROZEN(2);

    private final int value;
    private static final Map<Integer, MarketDataType> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final MarketDataType marketDataType : values()) {
            MAP.put(marketDataType.getValue(), marketDataType);
        }
    }

    private MarketDataType(final int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }

    public static final MarketDataType fromValue(final int value) {
        if (MAP.containsKey(value)) {
            return MAP.get(value);
        }
        return UNKNOWN;
    }
}
