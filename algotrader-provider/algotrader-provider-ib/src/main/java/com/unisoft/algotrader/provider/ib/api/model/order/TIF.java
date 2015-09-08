package com.unisoft.algotrader.provider.ib.api.model.order;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.trading.TimeInForce;
import com.unisoft.algotrader.provider.ib.api.model.system.IBModelUtils;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by alex on 8/30/15.
 */
public enum TIF {

    UNKNOWN(TimeInForce.Undefined, "UNKNOWN"),
    DAY(TimeInForce.Day, "DAY"),
    GOOD_TILL_CANCEL(TimeInForce.GTC, "GTC"),
    IMMEDIATE_OR_CANCEL(TimeInForce.FOK, "IOC"),
    GOOD_TILL_DATE(TimeInForce.GoodTillDate, "GTD");

    private static Map<TimeInForce, byte[]> map = Maps.newHashMap();
    private static Map<byte[], TimeInForce> byteArrayMap = Maps.newHashMap();
    private static Map<String, TimeInForce> stringMap = Maps.newHashMap();

    private final TimeInForce tif;
    private final byte[] bytes;
    private final String shortName;

    TIF(TimeInForce tif, String shortName) {
        this.tif = tif;
        this.shortName = shortName;
        this.bytes = shortName.getBytes();
    }

    public TimeInForce tif() {
        return tif;
    }

    public byte[] bytes() {
        return bytes;
    }

    public String shortName() {
        return shortName;
    }

    static {
        for (TIF timeInForce : TIF.values()) {
            map.put(timeInForce.tif, timeInForce.bytes);
            byteArrayMap.put(timeInForce.bytes, timeInForce.tif);
            stringMap.put(timeInForce.shortName, timeInForce.tif);
        }
    }

    public static byte[] convert(TimeInForce tif) {
        if (tif == null)
            return IBModelUtils.EMPTY_BYTES;
        if (map.containsKey(tif))
            return map.get(tif);
        return UNKNOWN.bytes;
    }

    public static TimeInForce convert(byte[] bytes) {
        if (bytes != null) {
            for (TIF timeInForce : TIF.values()) {
                if (Arrays.equals(bytes, timeInForce.bytes)) {
                    return timeInForce.tif;
                }
            }
        }
        return null;
    }

    public static TimeInForce convert(String name) {
        if (name != null) {
            if (stringMap.containsKey(name)){
                return stringMap.get(name);
            }
        }
        return null;
    }
}
