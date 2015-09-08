package com.unisoft.algotrader.provider.ib.api.model.order;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alex on 9/9/15.
 */
public enum ShortSaleSlotInstitutional {

    INAPPLICABLE(0), CLEARING_BROKER(1), THIRD_PARTY(2);

    private final int value;
    private static final Map<Integer, ShortSaleSlotInstitutional> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final ShortSaleSlotInstitutional shortSaleSlot : values()) {
            MAP.put(shortSaleSlot.getValue(), shortSaleSlot);
        }
    }

    private ShortSaleSlotInstitutional(final int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }

    public static final ShortSaleSlotInstitutional fromValue(final int value) {
        if (MAP.containsKey(value)) {
            return MAP.get(value);
        }
        return INAPPLICABLE;
    }
}
