package com.unisoft.algotrader.provider.ib.api.model.order;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.trading.OrdType;
import com.unisoft.algotrader.provider.ib.api.model.system.IBModelUtils;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by alex on 8/30/15.
 */
public enum OrderType {

    MARKET(OrdType.Market, "MKT"),
    LIMIT(OrdType.Limit, "LMT"),
    STOP(OrdType.Stop, "STP"),
    STOP_LIMIT(OrdType.StopLimit, "STPLMT"),
    MARKET_ON_CLOSE(OrdType.MarketOnClose, "MOC"),
    LIMIT_ON_CLOSE(OrdType.LimitOnClose, "LOC"),
    TRAILING_STOP(OrdType.TrailingStop, "TRAIL"),
    MARKET_TO_LIMIT(OrdType.MarketWithLeftoverAsLimit, "MTL"),
    MARKET_IF_PRICE_TOUCHED(OrdType.MIT, "MIT"),
    MARKET_ON_OPEN(OrdType.OnClose, "MOO"),
    UNKNOWN(OrdType.Undefined, "UNKNOWN");

    private static Map<OrdType, byte[]> ordTypeMap = Maps.newHashMap();
    private static Map<byte[], OrdType> byteArrayMap = Maps.newHashMap();
    private static Map<String, OrdType> stringMap = Maps.newHashMap();

    private final OrdType ordType;
    private final byte[] bytes;
    private final String shortName;


    OrderType(OrdType ordType, String shortName) {
        this.ordType = ordType;
        this.shortName = shortName;
        this.bytes = shortName.getBytes();
    }

    public OrdType ordType() {
        return ordType;
    }

    public byte[] bytes() {
        return bytes;
    }

    public String shortName() {
        return shortName;
    }

    static {
        for (OrderType orderType : OrderType.values()) {
            ordTypeMap.put(orderType.ordType, orderType.bytes);
            byteArrayMap.put(orderType.bytes, orderType.ordType);
            stringMap.put(orderType.shortName, orderType.ordType);
        }
    }

    public static byte[] convert(OrdType ordType) {
        if (ordType == null)
            return IBModelUtils.EMPTY_BYTES;
        if (ordTypeMap.containsKey(ordType))
            return ordTypeMap.get(ordType);
        return UNKNOWN.bytes;
    }

    public static OrdType convert(byte[] bytes) {
        if (bytes != null) {
            for (OrderType orderType : OrderType.values()) {
                if (Arrays.equals(bytes, orderType.bytes)) {
                    return orderType.ordType;
                }
            }
        }
        return OrdType.Undefined;
    }

    public static OrdType convert(String name) {
        if (name != null) {
            if (stringMap.containsKey(name)){
                return stringMap.get(name);
            }
        }
        return OrdType.Undefined;
    }
}
