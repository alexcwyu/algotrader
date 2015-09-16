package com.unisoft.algotrader.provider.ib.api.model.data;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.event.data.MDOperation;

import java.util.Map;

/**
 * Created by alex on 8/30/15.
 */
public enum Operation {

    UNKNOWN(MDOperation.Undefined, -1),
    INSERT(MDOperation.Insert, 0),
    UPDATE(MDOperation.Update, 1),
    DELETE(MDOperation.Delete, 2);

    private final int value;
    public final MDOperation mdOperation;
    private static final Map<Integer, Operation> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final Operation operation : values()) {
            MAP.put(operation.getValue(), operation);
        }
    }

    private Operation(final MDOperation mdOperation, final int value) {
        this.mdOperation = mdOperation;
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
