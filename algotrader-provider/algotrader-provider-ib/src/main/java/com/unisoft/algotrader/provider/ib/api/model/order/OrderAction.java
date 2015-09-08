package com.unisoft.algotrader.provider.ib.api.model.order;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.trading.Side;
import com.unisoft.algotrader.provider.ib.api.model.system.IBModelUtils;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by alex on 8/30/15.
 */
public enum OrderAction {

    BUY(Side.Buy, "BUY"),
    SELL(Side.Sell, "SELL"),
    SSHORT(Side.SellShort, "SSHORT"),
    UNKNOWN(Side.Undefined, "UNKNOWN");

    private static Map<Side, byte[]> sideMap = Maps.newHashMap();
    private static Map<byte[], Side> byteArrayMap = Maps.newHashMap();
    private static Map<String, Side> stringMap = Maps.newHashMap();

    private final Side side;
    private final byte[] bytes;
    private final String shortName;

    OrderAction(Side side, String shortName) {
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
        for (OrderAction orderAction : OrderAction.values()) {
            sideMap.put(orderAction.side, orderAction.bytes);
            byteArrayMap.put(orderAction.bytes, orderAction.side);
            stringMap.put(orderAction.shortName, orderAction.side);
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
            for (OrderAction orderAction : OrderAction.values()) {
                if (Arrays.equals(bytes, orderAction.bytes)) {
                    return orderAction.side;
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
