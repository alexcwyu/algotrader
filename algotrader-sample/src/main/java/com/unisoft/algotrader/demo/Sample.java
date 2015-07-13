package com.unisoft.algotrader.demo;

import com.unisoft.algotrader.model.refdata.Currency;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.refdata.InstrumentManager;

/**
 * Created by alex on 6/25/15.
 */
public class Sample {
    public static Instrument testInstrument =
            InstrumentManager.INSTANCE.createStock("TestInst", "TestExch", Currency.HKD.getCcyId());
}
