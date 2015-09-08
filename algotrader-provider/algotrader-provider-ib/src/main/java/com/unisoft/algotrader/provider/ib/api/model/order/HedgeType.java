package com.unisoft.algotrader.provider.ib.api.model.order;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by alex on 9/9/15.
 */
public enum HedgeType {

    UNKNOWN("U"), EMPTY(""), DELTA("D"), BETA("B"), FX("F"), PAIR("P");

    private final String initial;
    private static final Map<String, HedgeType> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final HedgeType hedgeType : values()) {
            MAP.put(hedgeType.getInitial(), hedgeType);
        }
    }

    private HedgeType(final String initial) {
        this.initial = initial;
    }

    public final String getInitial() {
        return initial;
    }

    public static final HedgeType fromInitial(final String initial) {
        if (StringUtils.isBlank(initial)) {
            return EMPTY;
        }
        final String initialUpperCase = initial.toUpperCase();
        if (MAP.containsKey(initialUpperCase)) {
            return MAP.get(initialUpperCase);
        }
        return UNKNOWN;
    }
}
