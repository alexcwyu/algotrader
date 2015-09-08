package com.unisoft.algotrader.provider.ib.api.model.contract;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by alex on 9/9/15.
 */
public enum SecurityIdentifierCode {

    SIN("SIN"),
    CUSIP("CUSIP"),
    SEDOL("SEDOL"),
    RIC("RIC"),
    UNKNOWN("UNKNOWN"),
    EMPTY("");

    private final String acronym;
    private static final Map<String, SecurityIdentifierCode> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final SecurityIdentifierCode code : values()) {
            MAP.put(code.getAcronym(), code);
        }
    }

    private SecurityIdentifierCode(final String acronym) {
        this.acronym = acronym;
    }

    public final String getAcronym() {
        return acronym;
    }

    public static final SecurityIdentifierCode fromAcronym(final String acronym) {
        if (StringUtils.isBlank(acronym)) {
            return EMPTY;
        }
        final String acronymUpperCase = acronym.toUpperCase();
        if (MAP.containsKey(acronymUpperCase)) {
            return MAP.get(acronymUpperCase);
        }
        return UNKNOWN;
    }
}
