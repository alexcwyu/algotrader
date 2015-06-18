package com.unisoft.algotrader.core;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.core.id.InstId;

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

    private Map<InstId, Instrument> map = Maps.newConcurrentMap();

    public void add(Instrument instrument){
        map.put(instrument.instId, instrument);
    }

    public Instrument get(InstId instId){
        return map.get(instId);
    }
}
