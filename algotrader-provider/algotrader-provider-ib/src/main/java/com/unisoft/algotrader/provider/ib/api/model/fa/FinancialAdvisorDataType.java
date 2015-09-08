package com.unisoft.algotrader.provider.ib.api.model.fa;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alex on 8/30/15.
 */
public enum FinancialAdvisorDataType {

    UNKNOWN(-1),
    GROUPS(1),
    PROFILE(2),
    ACCOUNT_ALIASES(3);

    private final int value;
    private static final Map<Integer, FinancialAdvisorDataType> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final FinancialAdvisorDataType dataType : values()) {
            MAP.put(dataType.getValue(), dataType);
        }
    }

    private FinancialAdvisorDataType(final int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }

    public static final FinancialAdvisorDataType fromValue(final int value) {
        if (MAP.containsKey(value)) {
            return MAP.get(value);
        }
        return UNKNOWN;
    }
}
