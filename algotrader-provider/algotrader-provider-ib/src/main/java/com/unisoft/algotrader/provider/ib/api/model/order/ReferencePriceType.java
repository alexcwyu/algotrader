package com.unisoft.algotrader.provider.ib.api.model.order;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alex on 9/9/15.
 */
public enum ReferencePriceType {

    INAPPLICABLE(Integer.MAX_VALUE),
    NBBO_AVERAGE(1),
    NBB_OR_NBO(2);

    private final int value;
    private static final Map<Integer, ReferencePriceType> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final ReferencePriceType referencePriceType : values()) {
            MAP.put(referencePriceType.getValue(), referencePriceType);
        }
    }

    private ReferencePriceType(final int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }

    public static final ReferencePriceType fromValue(final int value) {
        if (MAP.containsKey(value)) {
            return MAP.get(value);
        }
        return INAPPLICABLE;
    }
}
