package com.unisoft.algotrader.demo;

import com.unisoft.algotrader.core.Currency;
import com.unisoft.algotrader.core.Instrument;
import com.unisoft.algotrader.core.InstrumentManager;

/**
 * Created by alex on 6/25/15.
 */
public class Sample {
    public static Instrument testInstrument =
            InstrumentManager.INSTANCE.createStock("TestInst", "TestExch", Currency.HKD.ccyId);
}
