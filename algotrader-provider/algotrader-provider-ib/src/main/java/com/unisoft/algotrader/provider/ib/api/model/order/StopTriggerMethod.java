package com.unisoft.algotrader.provider.ib.api.model.order;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alex on 9/9/15.
 */
public enum StopTriggerMethod {

    UNKNOWN(-1),
    DEFAULT(0),
    TWO_CONSECUTIVE_BID_ASK_PRICE(1),
    LAST_PRICE(2),
    DOUBLE_LAST_PRICE(3),
    BID_ASK_PRICE(4),
    LAST_OR_BID_ASK_PRICE(7),
    MID_POINT_PRICE(8);

    private final int value;
    private static final Map<Integer, StopTriggerMethod> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final StopTriggerMethod method : values()) {
            MAP.put(method.getValue(), method);
        }
    }

    private StopTriggerMethod(final int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }

    public static final StopTriggerMethod fromValue(final int value) {
        if (MAP.containsKey(value)) {
            return MAP.get(value);
        }
        return UNKNOWN;
    }
}
