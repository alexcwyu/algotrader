package com.unisoft.algotrader.provider.ib.api.model.order;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by alex on 9/9/15.
 */
public enum OpenCloseInstitutional {

    UNKNOWN("U"),
    EMPTY(""),
    OPEN("O"),
    CLOSE("C");

    private final String initial;
    private static final Map<String, OpenCloseInstitutional> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final OpenCloseInstitutional openClose : values()) {
            MAP.put(openClose.getInitial(), openClose);
        }
    }

    private OpenCloseInstitutional(final String initial) {
        this.initial = initial;
    }

    public final String getInitial() {
        return initial;
    }

    public static final OpenCloseInstitutional fromInitial(final String initial) {
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
