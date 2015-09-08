package com.unisoft.algotrader.provider.ib.api.model.order;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alex on 9/9/15.
 */
public enum OpenCloseComboLeg {

    SAME(0),
    OPEN(1),
    CLOSE(2),
    UNKNOWN(3);

    private final int value;
    private static final Map<Integer, OpenCloseComboLeg> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final OpenCloseComboLeg openCloseComboLeg : values()) {
            MAP.put(openCloseComboLeg.getValue(), openCloseComboLeg);
        }
    }

    private OpenCloseComboLeg(final int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }

    public static final OpenCloseComboLeg fromValue(final int value) {
        if (MAP.containsKey(value)) {
            return MAP.get(value);
        }
        return UNKNOWN;
    }
}
