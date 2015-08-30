package com.unisoft.algotrader.provider.ib.api.model.constants;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.refdata.Instrument;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by alex on 8/30/15.
 */
public enum OptionRight {

    PUT(Instrument.PutCall.Put, "PUT"),
    CALL(Instrument.PutCall.Call, "CALL");

    private static Map<Instrument.PutCall, byte[]> map = Maps.newHashMap();
    private static Map<byte[], Instrument.PutCall> byteArrayMap = Maps.newHashMap();
    private static Map<String, Instrument.PutCall> stringMap = Maps.newHashMap();

    private final Instrument.PutCall putCall;
    private final byte[] bytes;
    private final String shortName;

    OptionRight(Instrument.PutCall putCall, String shortName) {
        this.putCall = putCall;
        this.bytes = shortName.getBytes();
        this.shortName = shortName;
    }

    public Instrument.PutCall putCall() {
        return putCall;
    }

    public byte[] bytes() {
        return bytes;
    }

    public String shortName() {
        return shortName;
    }

    static {
        for (OptionRight optionRight : OptionRight.values()) {
            map.put(optionRight.putCall, optionRight.bytes);
            byteArrayMap.put(optionRight.bytes, optionRight.putCall);
            stringMap.put(optionRight.shortName, optionRight.putCall);
        }
    }

    public static byte[] convert(Instrument.PutCall putCall) {
        if (putCall == null)
            return IBModelUtils.EMPTY_BYTES;
        if (map.containsKey(putCall))
            return map.get(putCall);
        return IBModelUtils.UNKNOWN_BYTES;
    }

    public static Instrument.PutCall convert(byte[] bytes) {
        if (bytes != null) {
            for (OptionRight optionRight : OptionRight.values()) {
                if (Arrays.equals(bytes, optionRight.bytes)) {
                    return optionRight.putCall;
                }
            }
        }
        return null;
    }

    public static Instrument.PutCall convert(String name) {
        if (name != null) {
            if (stringMap.containsKey(name)){
                return stringMap.get(name);
            }
        }
        return null;
    }
}
