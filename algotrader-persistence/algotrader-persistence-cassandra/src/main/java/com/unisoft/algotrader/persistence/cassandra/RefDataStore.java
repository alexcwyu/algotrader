package com.unisoft.algotrader.persistence.cassandra;

import com.unisoft.algotrader.model.refdata.Currency;
import com.unisoft.algotrader.model.refdata.Exchange;
import com.unisoft.algotrader.model.refdata.Instrument;

/**
 * Created by alex on 7/7/15.
 */
public interface RefDataStore {

    void saveCurrency(Currency currency);

    Currency getCurrency(String ccyId);

    void saveExchange(Exchange exchange);

    Exchange getExchange(String exchId);

    void saveInstrument(Instrument instrument);

    Instrument getInstrument(int instId);
}
