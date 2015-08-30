package com.unisoft.algotrader.provider.ib.api.model.constants;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alex on 8/30/15.
 */
public enum BookSide {

    UNKNOWN(-1),
    ASK(0),
    BID(1);

    private final int value;
    private static final Map<Integer, BookSide> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final BookSide bookSide : values()) {
            MAP.put(bookSide.getValue(), bookSide);
        }
    }

    private BookSide(final int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }

    public static final BookSide fromValue(final int value) {
        if (MAP.containsKey(value)) {
            return MAP.get(value);
        }
        return UNKNOWN;
    }
}
