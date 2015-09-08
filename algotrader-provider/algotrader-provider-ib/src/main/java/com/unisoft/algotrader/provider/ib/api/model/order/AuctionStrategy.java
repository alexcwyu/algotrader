package com.unisoft.algotrader.provider.ib.api.model.order;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alex on 9/9/15.
 */
public enum AuctionStrategy {

    INAPPLICABLE(0), MATCH(1), IMPROVEMENT(2), TRANSPARENT(3);

    private final int value;
    private static final Map<Integer, AuctionStrategy> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final AuctionStrategy strategy : values()) {
            MAP.put(strategy.getValue(), strategy);
        }
    }

    private AuctionStrategy(final int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }

    public static final AuctionStrategy fromValue(final int value) {
        if (MAP.containsKey(value)) {
            return MAP.get(value);
        }
        return INAPPLICABLE;
    }
}
