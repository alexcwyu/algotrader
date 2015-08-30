package com.unisoft.algotrader.provider.ib.api.model.constants;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by alex on 8/30/15.
 */
public enum OrderStatus {

    UNKNOWN("UNKNOWN"),
    PENDING_SUBMIT("PendingSubmit"),
    PENDING_CANCEL("PendingCancel"),
    PRE_SUBMITTED("PreSubmitted"),
    SUBMITTED("Submitted"),
    CANCELLED("Cancelled"),
    FILLED("Filled"),
    INACTIVE("Inactive"),
    EMPTY("");

    private final String label;
    private static final Map<String, OrderStatus> MAP;

    private OrderStatus(final String label) {
        this.label = label;
    }

    static {
        MAP = Maps.newHashMap();
        for (final OrderStatus orderStatus : values()) {
            MAP.put(orderStatus.label.toUpperCase(), orderStatus);
        }
    }

    public static final OrderStatus fromLabel(final String label) {
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
