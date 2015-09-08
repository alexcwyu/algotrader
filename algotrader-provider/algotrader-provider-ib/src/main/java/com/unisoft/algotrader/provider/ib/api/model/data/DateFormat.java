package com.unisoft.algotrader.provider.ib.api.model.data;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alex on 9/3/15.
 */
public enum DateFormat {

    UNKNOWN(0), SECONDS_TIMESTAMP(2), YYYYMMDD__HH_MM_SS(1);

    private final int value;
    private static final Map<Integer, DateFormat> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final DateFormat dateFormat : values()) {
            MAP.put(dateFormat.getValue(), dateFormat);
        }
    }

    private DateFormat(final int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }

    public static final DateFormat fromValue(final int value) {
        if (MAP.containsKey(value)) {
            return MAP.get(value);
        }
        return UNKNOWN;
    }
}
