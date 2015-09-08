package com.unisoft.algotrader.provider.ib.api.model.order;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alex on 9/9/15.
 */
public enum OcaType {

    UNKNOWN(-1),
    CANCEL_REMAINING_ORDERS_WITH_BLOCK(1),
    PROPORTIONATELY_REDUCE_REMAINING_SIZE_ORDERS_WITH_BLOCK(2),
    PROPORTIONATELY_REDUCE_REMAINING_SIZE_ORDERS_WITH_NO_BLOCK(3),
    EMPTY(0);

    private final int value;
    private static final Map<Integer, OcaType> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final OcaType ocaType : values()) {
            MAP.put(ocaType.getValue(), ocaType);
        }
    }

    private OcaType(final int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }

    public static final OcaType fromValue(final int value) {
        if (MAP.containsKey(value)) {
            return MAP.get(value);
        }
        return UNKNOWN;
    }
}
