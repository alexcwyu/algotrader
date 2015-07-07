package com.unisoft.algotrader.persistence.cassandra;

import com.unisoft.algotrader.model.refdata.Currency;
import com.unisoft.algotrader.model.refdata.Exchange;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.model.refdata.InstrumentManager;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by alex on 7/8/15.
 */
public class CassandraRefDataStoreIntegrationTest {

    private static CassandraRefDataStore store;

    @BeforeClass
    public static void init(){
        store = new CassandraRefDataStore();
        store.connect();
    }

    @Test
    public void testSaveLoadCurrency() throws Exception {
        Currency currency = new Currency("USD", "US Dollar");
        store.saveCurrency(currency);
        Currency currency2 = store.getCurrency(currency.getCcyId());

        assertEquals(currency, currency2);
    }

    @Test
    public void testSaveLoadExchange() throws Exception {
        Exchange exchange = new Exchange("SEHK", "Hong Kong Stock Exchange");
        store.saveExchange(exchange);
        Exchange exchange1 = store.getExchange(exchange.getExchId());

        assertEquals(exchange, exchange1);
    }

    @Test
    public void testSaveLoadInstrument() throws Exception {
        Instrument instrument = InstrumentManager.INSTANCE.createStock("2628.HK","SEHK","HKD");

        store.saveInstrument(instrument);
        Instrument instrument1 = store.getInstrument(instrument.getInstId());

        assertEquals(instrument, instrument1);
    }
}
