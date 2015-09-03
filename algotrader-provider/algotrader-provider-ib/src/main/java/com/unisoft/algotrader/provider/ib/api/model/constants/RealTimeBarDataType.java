package com.unisoft.algotrader.provider.ib.api.model.constants;

import com.unisoft.algotrader.model.event.data.DataType;

/**
 * Created by alex on 8/30/15.
 */
public enum RealTimeBarDataType {
    UNKNOWN("UNKNOWN"),
    EMPTY(""),
    TRADES("TRADES"),
    BID("BID"),
    ASK("ASK"),
    MID_POINT("MIDPOINT");

    private final byte[] bytes;

    RealTimeBarDataType(String type) {
        this.bytes = type.getBytes();
    }

    public byte[] getBytes() {
        return bytes;
    }

    public static final byte[] from(final DataType dataType){
        switch (dataType){
            case Bar:
                return RealTimeBarDataType.MID_POINT.bytes;
            case Quote:
                return RealTimeBarDataType.MID_POINT.bytes;
            case Trade:
                return RealTimeBarDataType.TRADES.bytes;
            default:
                return RealTimeBarDataType.MID_POINT.bytes;
        }
    }
}
