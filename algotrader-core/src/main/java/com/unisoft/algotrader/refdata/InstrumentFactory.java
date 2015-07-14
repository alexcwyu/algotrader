package com.unisoft.algotrader.refdata;

import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by alex on 7/14/15.
 */
public class InstrumentFactory {

    private final RefDataStore refDataStore;

    private final Lock lock = new ReentrantLock();

    public InstrumentFactory(RefDataStore refDataStore){
        this.refDataStore = refDataStore;
    }

    public Instrument createInstrument(Instrument.InstType type, String symbol, String exchId, String ccyId){
        lock.lock();
        try{
            Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange(symbol, exchId);
            if (instrument != null) {
                return instrument;
            }

            instrument = new Instrument(refDataStore.nextId(), type, symbol, exchId, ccyId);
            refDataStore.saveInstrument(instrument);
            return instrument;
        }finally {
            lock.unlock();
        }
    }

    public Instrument createStock(String symbol, String exchId, String ccyId){
        return createInstrument(Instrument.InstType.Stock, symbol, exchId, ccyId);
    }

    public Instrument createFX(String symbol, String exchId, String ccyId){
        return createInstrument(Instrument.InstType.FX, symbol, exchId, ccyId);

    }
    public Instrument createFuture(String symbol, String exchId, String ccyId){
        return createInstrument(Instrument.InstType.Future, symbol, exchId, ccyId);
    }

    public Instrument createIndex(String symbol, String exchId, String ccyId){
        return createInstrument(Instrument.InstType.Index, symbol, exchId, ccyId);
    }

    public Instrument createETF(String symbol, String exchId, String ccyId){
        return createInstrument(Instrument.InstType.ETF, symbol, exchId, ccyId);
    }

    public Instrument createOption(String symbol, String exchId, String ccyId){
        return createInstrument(Instrument.InstType.Option, symbol, exchId, ccyId);
    }
}
