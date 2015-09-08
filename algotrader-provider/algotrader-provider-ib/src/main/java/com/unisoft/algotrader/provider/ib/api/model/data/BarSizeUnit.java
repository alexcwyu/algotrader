package com.unisoft.algotrader.provider.ib.api.model.data;

/**
 * Created by alex on 9/3/15.
 */

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public enum BarSizeUnit {

    UNKNOWN("unknown"),
    EMPTY(""),
    SECONDS("secs"),
    MINUTE("min"),
    MINUTES("mins"),
    HOUR("hour"),
    DAY("day");

    private final String abbreviation;
    private static final Map<String, BarSizeUnit> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final BarSizeUnit barSizeUnit : values()) {
            MAP.put(barSizeUnit.getAbbreviation().toUpperCase(), barSizeUnit);
        }
    }

    private BarSizeUnit(final String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public final String getAbbreviation() {
        return abbreviation;
    }

    public static final BarSizeUnit fromAbbreviation(final String abbreviation) {
        if (StringUtils.isBlank(abbreviation)) {
            return EMPTY;
        }
        final String abbreviationUpperCase = abbreviation.toUpperCase();
        if (MAP.containsKey(abbreviationUpperCase)) {
            return MAP.get(abbreviationUpperCase);
        }
        return UNKNOWN;
    }
}
