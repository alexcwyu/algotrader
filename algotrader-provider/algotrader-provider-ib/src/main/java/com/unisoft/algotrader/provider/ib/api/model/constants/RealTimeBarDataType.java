package com.unisoft.algotrader.provider.ib.api.model.constants;

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
}
