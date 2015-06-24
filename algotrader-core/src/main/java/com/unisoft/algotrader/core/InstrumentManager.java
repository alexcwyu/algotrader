package com.unisoft.algotrader.core;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.unisoft.algotrader.core.Instrument.InstId;
import static com.unisoft.algotrader.core.Instrument.InstType;
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


    private final AtomicInteger counter = new AtomicInteger(0);
    private final Lock lock = new ReentrantLock();
    private Map<Instrument.InstId, Instrument> instIdMap = Maps.newConcurrentMap();
    private Map<Integer, Instrument> idMap = Maps.newConcurrentMap();


    public boolean add(Instrument instrument){
        InstId instId = new InstId(instrument.symbol, instrument.exchId);

        if (!instIdMap.containsKey(instId)) {
            instIdMap.put(instId, instrument);
            idMap.put(instrument.instId, instrument);
            return true;
        }
        return false;
    }

    public Instrument get(Instrument.InstId instId){
        return instIdMap.get(instId);
    }

    public Instrument get(int id){
        return idMap.get(id);
    }

    public Instrument createInstrument(InstType type, String symbol, String exchId, String ccyId){
        InstId instId = new InstId(symbol, exchId);
        lock.lock();
        try{
            if (instIdMap.containsKey(instId)) {
                return instIdMap.get(instId);
            }

            Instrument instrument = new Instrument(counter.incrementAndGet(), type, symbol, exchId, ccyId);
            add(instrument);
            return instrument;
        }finally {
            lock.unlock();
        }
    }

    public Instrument createStock(String symbol, String exchId, String ccyId){
        return createInstrument(InstType.Stock, symbol, exchId, ccyId);
    }

    public Instrument createFX(String symbol, String exchId, String ccyId){
        return createInstrument(InstType.FX, symbol, exchId, ccyId);

    }
    public Instrument createFuture(String symbol, String exchId, String ccyId){
        return createInstrument(InstType.Future, symbol, exchId, ccyId);
    }

    public Instrument createIndex(String symbol, String exchId, String ccyId){
        return createInstrument(InstType.Index, symbol, exchId, ccyId);
    }

    public Instrument createETF(String symbol, String exchId, String ccyId){
        return createInstrument(InstType.ETF, symbol, exchId, ccyId);
    }

    public Instrument createOption(String symbol, String exchId, String ccyId){
        return createInstrument(InstType.Option, symbol, exchId, ccyId);
    }
}
