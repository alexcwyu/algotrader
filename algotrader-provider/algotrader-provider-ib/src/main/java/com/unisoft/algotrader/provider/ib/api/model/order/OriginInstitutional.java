package com.unisoft.algotrader.provider.ib.api.model.order;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alex on 9/9/15.
 */
public enum OriginInstitutional {

    UNKNOWN(-1),
    CUSTOMER(0),
    FIRM(1);

    private final int value;
    private static final Map<Integer, OriginInstitutional> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final OriginInstitutional origin : values()) {
            MAP.put(origin.getValue(), origin);
        }
    }

    private OriginInstitutional(final int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }

    public static final OriginInstitutional fromValue(final int value) {
        if (MAP.containsKey(value)) {
            return MAP.get(value);
        }
        return UNKNOWN;
    }
}
