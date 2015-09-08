package com.unisoft.algotrader.provider.ib.api.model.data;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alex on 8/30/15.
 */
public enum Operation {

    UNKNOWN(-1),
    INSERT(0),
    UPDATE(1),
    DELETE(2);

    private final int value;
    private static final Map<Integer, Operation> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final Operation operation : values()) {
            MAP.put(operation.getValue(), operation);
        }
    }

    private Operation(final int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }

    public static final Operation fromValue(final int value) {
        if (MAP.containsKey(value)) {
            return MAP.get(value);
        }
        return UNKNOWN;
    }
}
