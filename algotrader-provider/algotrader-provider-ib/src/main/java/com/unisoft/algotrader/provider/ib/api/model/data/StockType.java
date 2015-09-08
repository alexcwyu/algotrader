package com.unisoft.algotrader.provider.ib.api.model.data;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by alex on 9/9/15.
 */
public enum StockType {

    UNKNOWN("UNKNOWN"), ALL("ALL"), EXCLUDE_ETF("STOCK"), EXCLUDE_STOCK("ETF"), EMPTY("");

    private final String label;
    private static final Map<String, StockType> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final StockType stockType : values()) {
            MAP.put(stockType.getLabel(), stockType);
        }
    }

    private StockType(final String label) {
        this.label = label;
    }

    public final String getLabel() {
        return label;
    }

    public static final StockType fromLabel(final String label) {
        if (StringUtils.isBlank(label)) {
            return EMPTY;
        }
        final String labelUpperCase = label.toUpperCase();
        if (MAP.containsKey(labelUpperCase)) {
            return MAP.get(labelUpperCase);
        }
        return UNKNOWN;
    }
}
