package com.unisoft.algotrader.provider.ib.api.model.data;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by alex on 9/9/15.
 */
public enum ReportType {

    UNKNOWN("UNKNOWN"),
    EMPTY(""),
    ESTIMATES("Estimates"),
    FINANCIAL_STATEMENTS("Financial Statements"),
    SUMMARY("Summary");

    private final String label;
    private static final Map<String, ReportType> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final ReportType reportType : values()) {
            MAP.put(reportType.getLabel().toUpperCase(), reportType);
        }
    }

    private ReportType(final String label) {
        this.label = label;
    }

    public final String getLabel() {
        return label;
    }

    public static final ReportType fromLabel(final String label) {
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
