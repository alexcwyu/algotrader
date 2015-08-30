package com.unisoft.algotrader.provider.ib.api.model.constants;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.refdata.Instrument;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by alex on 8/30/15.
 */
public enum SecType {

    STOCK(Instrument.InstType.Stock, "STK"),
    OPTION(Instrument.InstType.Option, "OPT"),
    FUTURE(Instrument.InstType.Future, "FUT"),
    INDEX(Instrument.InstType.Index, "IND"),
    FUTURE_ON_OPTION(Instrument.InstType.FutureOption, "FOP"),
    FOREX(Instrument.InstType.FX, "CASH"),
    COMBO(Instrument.InstType.Combo, "BAG");

    private static Map<Instrument.InstType, byte[]> map = Maps.newHashMap();
    private static Map<byte[], Instrument.InstType> byteArrayMap = Maps.newHashMap();
    private static Map<String, Instrument.InstType> stringMap = Maps.newHashMap();

    private final Instrument.InstType instType;
    private final byte[] bytes;
    private final String shortName;

    SecType(Instrument.InstType instType, String shortName) {
        this.instType = instType;
        this.bytes = shortName.getBytes();
        this.shortName = shortName;
    }

    public Instrument.InstType instType() {
        return instType;
    }

    public byte[] bytes() {
        return bytes;
    }

    public String shortName() {
        return shortName;
    }

    static {
        for (SecType secType : SecType.values()) {
            map.put(secType.instType, secType.bytes);
            byteArrayMap.put(secType.bytes, secType.instType);
            stringMap.put(secType.shortName, secType.instType);
        }
    }

    public static byte[] convert(Instrument.InstType instType) {
        if (instType == null)
            return IBModelUtils.EMPTY_BYTES;
        if (instType == Instrument.InstType.ETF)
            return STOCK.bytes;
        if (map.containsKey(instType))
            return map.get(instType);
        return IBModelUtils.UNKNOWN_BYTES;
    }

    public static Instrument.InstType convert(String name) {
        if (name != null) {
            if (stringMap.containsKey(name)){
                return stringMap.get(name);
            }
        }
        return null;
    }

    public static Instrument.InstType convert(byte[] bytes) {
        if (bytes != null) {
            for (SecType secType : SecType.values()) {
                if (Arrays.equals(bytes, secType.bytes)) {
                    return secType.instType;
                }
            }
        }
        return null;
    }
}
