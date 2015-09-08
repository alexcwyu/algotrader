package com.unisoft.algotrader.provider.ib.api.model.order;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alex on 9/9/15.
 */
public enum VolatilityType {

    DAILY(1),
    ANNUAL(2),
    INAPPLICABLE(Integer.MAX_VALUE);

    private final int value;
    private static final Map<Integer, VolatilityType> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final VolatilityType type : values()) {
            MAP.put(type.getValue(), type);
        }
    }

    private VolatilityType(final int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }

    public static final VolatilityType fromValue(final int value) {
        if (MAP.containsKey(value)) {
            return MAP.get(value);
        }
        return INAPPLICABLE;
    }
}
