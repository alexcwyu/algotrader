package com.unisoft.algotrader.provider;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alex on 9/20/15.
 */
public enum ProviderId {
    Dummy(0),
    Simulation(1),
    Google(2),
    Yahoo(3),
    CSV(4),
    Cassandra(5),
    InfluxDB(6),
    KDB(7),
    IB(8);

    public final int id;

    ProviderId(int id){
        this.id = id;
    }

    private static Map<Integer, ProviderId> map = Maps.newHashMap();

    static {
        for (ProviderId providerId : ProviderId.values()) {
            map.put(providerId.id, providerId);
        }
    }

    public static ProviderId get(int id){
        return map.get(id);
    }
}
