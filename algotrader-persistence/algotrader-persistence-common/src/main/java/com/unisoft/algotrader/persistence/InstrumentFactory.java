package com.unisoft.algotrader.persistence;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.refdata.Instrument;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by alex on 7/14/15.
 */
@Singleton
public class InstrumentFactory {

    private final RefDataStore refDataStore;

    private final Lock lock = new ReentrantLock();

    @Inject
    public InstrumentFactory(RefDataStore refDataStore){
        this.refDataStore = refDataStore;
    }

    public Instrument createInstrument(Instrument.InstType type, String symbol, String name, String exchId, String ccyId){
        return createInstrument(type, symbol, name, exchId, ccyId, Maps.newHashMap(), Maps.newHashMap());
    }

    public Instrument createInstrument(Instrument.InstType type, String symbol, String name, String exchId, String ccyId, Map<String, String> altSymbols, Map<String, String> altExchIds){
        lock.lock();
        try{
            Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange(symbol, exchId);
            if (instrument != null) {
                return instrument;
            }

            instrument = new Instrument(refDataStore.nextId(), type, symbol, name, exchId, ccyId, altSymbols, altExchIds);
            refDataStore.saveInstrument(instrument);
            return instrument;
        }finally {
            lock.unlock();
        }
    }

    public Instrument createStock(String symbol, String name, String exchId, String ccyId){
        return createInstrument(Instrument.InstType.Stock, symbol, name, exchId, ccyId);
    }

    public Instrument createFX(String symbol, String name, String exchId, String ccyId){
        return createInstrument(Instrument.InstType.FX, symbol, name, exchId, ccyId);

    }
    public Instrument createFuture(String symbol, String name, String exchId, String ccyId){
        return createInstrument(Instrument.InstType.Future, symbol, name, exchId, ccyId);
    }

    public Instrument createIndex(String symbol, String name, String exchId, String ccyId){
        return createInstrument(Instrument.InstType.Index, symbol, name, exchId, ccyId);
    }

    public Instrument createETF(String symbol, String name, String exchId, String ccyId){
        return createInstrument(Instrument.InstType.ETF, symbol, name, exchId, ccyId);
    }

    public Instrument createOption(String symbol, String name, String exchId, String ccyId){
        return createInstrument(Instrument.InstType.Option, symbol, name, exchId, ccyId);
    }


    public Instrument createStock(String symbol, String name, String exchId, String ccyId, Map<String, String> altSymbols, Map<String, String> altExchIds){
        return createInstrument(Instrument.InstType.Stock, symbol, name, exchId, ccyId, altSymbols, altExchIds);
    }

    public Instrument createFX(String symbol, String name, String exchId, String ccyId, Map<String, String> altSymbols, Map<String, String> altExchIds){
        return createInstrument(Instrument.InstType.FX, symbol, name, exchId, ccyId, altSymbols, altExchIds);

    }
    public Instrument createFuture(String symbol, String name, String exchId, String ccyId, Map<String, String> altSymbols, Map<String, String> altExchIds){
        return createInstrument(Instrument.InstType.Future, symbol, name, exchId, ccyId, altSymbols, altExchIds);
    }

    public Instrument createIndex(String symbol, String name, String exchId, String ccyId, Map<String, String> altSymbols, Map<String, String> altExchIds){
        return createInstrument(Instrument.InstType.Index, symbol, name, exchId, ccyId, altSymbols, altExchIds);
    }

    public Instrument createETF(String symbol, String name, String exchId, String ccyId, Map<String, String> altSymbols, Map<String, String> altExchIds){
        return createInstrument(Instrument.InstType.ETF, symbol, name, exchId, ccyId, altSymbols, altExchIds);
    }

    public Instrument createOption(String symbol, String name, String exchId, String ccyId, Map<String, String> altSymbols, Map<String, String> altExchIds){
        return createInstrument(Instrument.InstType.Option, symbol, name, exchId, ccyId, altSymbols, altExchIds);
    }
}
