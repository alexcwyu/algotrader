package com.unisoft.algotrader.provider.ib.api.model.constants;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.trading.Side;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by alex on 8/30/15.
 */
public enum IBSide {

    BUY(Side.Buy, "BOT"),
    SELL(Side.Sell, "SLD"),
    UNKNOWN(Side.Undefined, "UNKNOWN");

    private static Map<Side, byte[]> sideMap = Maps.newHashMap();
    private static Map<byte[], Side> byteArrayMap = Maps.newHashMap();
    private static Map<String, Side> stringMap = Maps.newHashMap();

    private final Side side;
    private final byte[] bytes;
    private final String shortName;

    IBSide(Side side, String shortName) {
        this.side = side;
        this.bytes = shortName.getBytes();
        this.shortName = shortName;
    }

    public Side getSide() {
        return side;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public String shortName() {
        return shortName;
    }

    static {
        for (IBSide ibSide : IBSide.values()) {
            sideMap.put(ibSide.side, ibSide.bytes);
            byteArrayMap.put(ibSide.bytes, ibSide.side);
            stringMap.put(ibSide.shortName, ibSide.side);
        }
    }

    public static byte[] convert(Side side) {
        if (side == null)
            return IBModelUtils.EMPTY_BYTES;
        if (sideMap.containsKey(side))
            return sideMap.get(side);
        return UNKNOWN.bytes;
    }

    public static Side convert(byte[] bytes) {
        if (bytes != null) {
            for (IBSide ibSide : IBSide.values()) {
                if (Arrays.equals(bytes, ibSide.bytes)) {
                    return ibSide.side;
                }
            }
        }
        return Side.Undefined;
    }

    public static Side convert(String name) {
        if (name != null) {
            if (stringMap.containsKey(name)){
                return stringMap.get(name);
            }
        }
        return Side.Undefined;
    }
}
