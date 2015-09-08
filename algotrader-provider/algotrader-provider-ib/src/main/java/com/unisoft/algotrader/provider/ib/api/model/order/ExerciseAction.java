package com.unisoft.algotrader.provider.ib.api.model.order;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alex on 9/9/15.
 */
public enum ExerciseAction {

    UNKNOWN(-1),
    EXERCISE(1),
    LAPSE(2);

    private final int value;
    private static final Map<Integer, ExerciseAction> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final ExerciseAction action : values()) {
            MAP.put(action.getValue(), action);
        }
    }

    private ExerciseAction(final int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }

    public static final ExerciseAction fromValue(final int value) {
        if (MAP.containsKey(value)) {
            return MAP.get(value);
        }
        return UNKNOWN;
    }
}
