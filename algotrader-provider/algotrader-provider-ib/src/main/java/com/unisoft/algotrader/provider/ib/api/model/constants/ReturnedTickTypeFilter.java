package com.unisoft.algotrader.provider.ib.api.model.constants;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alex on 8/30/15.
 */
public enum ReturnedTickTypeFilter {

    UNKNOWN(-1),
    OPTION_VOLUME(100),
    OPTION_OPEN_INTEREST(101),
    HISTORICAL_VOLATILITY(104),
    OPTION_IMPLIED_VOLATILITY(106),
    INDEX_FUTURE_PREMIUM(162),
    MISCELLANEOUS_STATS(165),
    MARK_PRICE(221),
    AUCTION_VALUES(225),
    REAL_TIME_VOLUME(233),
    SHORTABLE(236),
    INVENTORY(256),
    FUNDAMENTAL_RATIOS(258),
    REAL_TIME_HISTORICAL_VOLATILITY(411);

    private final int id;
    private static final Map<Integer, ReturnedTickTypeFilter> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final ReturnedTickTypeFilter filterReturnedTickType : values()) {
            MAP.put(filterReturnedTickType.getId(), filterReturnedTickType);
        }
    }

    private ReturnedTickTypeFilter(final int id) {
        this.id = id;
    }

    public final int getId() {
        return id;
    }

    public static final ReturnedTickTypeFilter fromId(final int id) {
        if (MAP.containsKey(id)) {
            return MAP.get(id);
        }
        return UNKNOWN;
    }
}
