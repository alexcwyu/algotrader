package com.unisoft.algotrader.provider.ib.api.model.bulletin;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alex on 8/30/15.
 */
public enum NewsBulletinType {

    UNKNOWN(-1),
    REGULAR(1),
    EXCHANGE_NON_AVAILABLE(2),
    EXCHANGE_AVAILABLE(3);

    private final int value;
    private static final Map<Integer, NewsBulletinType> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final NewsBulletinType newsBulletinType : values()) {
            MAP.put(newsBulletinType.getValue(), newsBulletinType);
        }
    }

    private NewsBulletinType(final int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }

    public static final NewsBulletinType fromValue(final int value) {
        if (MAP.containsKey(value)) {
            return MAP.get(value);
        }
        return UNKNOWN;
    }
}
