package com.unisoft.algotrader.provider.ib.api.model.constants;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.event.data.DataType;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by alex on 9/3/15.
 */
public enum HistoricalDataType {

    UNKNOWN("UNKNOWN"),
    EMPTY(""),
    TRADES("TRADES"),
    MID_POINT("MIDPOINT"),
    BID("BID"),
    ASK("ASK"),
    BID_ASK("BID_ASK"),
    HISTORICAL_VOLATILITY("HISTORICAL_VOLATILITY"),
    OPTION_IMPLIED_VOLATILITY("OPTION_IMPLIED_VOLATILITY");

    private final String label;
    private static final Map<String, HistoricalDataType> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final HistoricalDataType dataType : values()) {
            MAP.put(dataType.getLabel(), dataType);
        }
    }

    private HistoricalDataType(final String label) {
        this.label = label;
    }

    public final String getLabel() {
        return label;
    }

    public static final HistoricalDataType fromLabel(final String label) {
        if (StringUtils.isBlank(label)) {
            return EMPTY;
        }
        final String labelUpperCase = label.toUpperCase();
        if (MAP.containsKey(labelUpperCase)) {
            return MAP.get(labelUpperCase);
        }
        return UNKNOWN;
    }

    public static final String from(final DataType dataType){
        switch (dataType){
            case Bar:
                return HistoricalDataType.TRADES.label;
            case Quote:
                return HistoricalDataType.BID_ASK.label;
            case Trade:
                return HistoricalDataType.TRADES.label;
            default:
                return HistoricalDataType.MID_POINT.label;
        }
    }
}
