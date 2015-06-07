package com.unisoft.algotrader.core;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alex on 5/27/15.
 */
public class InstrumentManager {

    public static final InstrumentManager INSTANCE;

    static {
        INSTANCE = new InstrumentManager();
    }


    private InstrumentManager(){

    }

    private Map<String, Instrument> map = Maps.newConcurrentMap();

    public void add(Instrument instrument){
        map.put(instrument.instId, instrument);
    }

    public Instrument get(String instId){
        return map.get(instId);
    }
}
