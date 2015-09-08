package com.unisoft.algotrader.provider.ib.api.model.order;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by alex on 9/9/15.
 */
public enum Rule80A {
    UNKNOWN("O"),
    EMPTY(""),
    INDIVIDUAL("I"),
    AGENCY("A"),
    AGENT_OTHER_MEMBER("W"),
    INDIVIDUAL_PTIA("J"),
    AGENCY_PTIA("U"),
    AGENT_OTHER_MEMBER_PTIA("M"),
    INDIVIDUAL_PT("K"),
    AGENCY_PT("Y"),
    AGENT_OTHER_MEMBER_PT("N");

    private final String initial;
    private static final Map<String, Rule80A> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final Rule80A rule80a : values()) {
            MAP.put(rule80a.getInitial(), rule80a);
        }
    }

    private Rule80A(final String initial) {
        this.initial = initial;
    }

    public final String getInitial() {
        return initial;
    }

    public static final Rule80A fromInitial(final String initial) {
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
