package com.unisoft.algotrader.demo;

import com.unisoft.algotrader.model.refdata.Currency;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.InMemoryRefDataStore;
import com.unisoft.algotrader.refdata.InstrumentFactory;

/**
 * Created by alex on 6/25/15.
 */
public class Sample {
    public static InMemoryRefDataStore refDataStore = new InMemoryRefDataStore();
    public static InstrumentFactory instrumentFactory = new InstrumentFactory(refDataStore);
    public static Instrument testInstrument =
            instrumentFactory.createStock("TestInst", "TestExch", Currency.HKD.getCcyId());
}
