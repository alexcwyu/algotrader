package com.unisoft.algotrader.provider.ib.api.model.data;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.event.data.MDSide;

import java.util.Map;

/**
 * Created by alex on 8/30/15.
 */
public enum BookSide {

    UNKNOWN(MDSide.Unknown, -1),
    ASK(MDSide.Ask, 0),
    BID(MDSide.Bid, 1);

    public final MDSide mdSide;
    private final int value;
    private static final Map<Integer, BookSide> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final BookSide bookSide : values()) {
            MAP.put(bookSide.getValue(), bookSide);
        }
    }

    private BookSide(final MDSide mdSide, final int value) {
        this.mdSide = mdSide;
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
