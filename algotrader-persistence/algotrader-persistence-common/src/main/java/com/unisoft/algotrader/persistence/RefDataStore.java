package com.unisoft.algotrader.persistence;

import com.unisoft.algotrader.model.refdata.Currency;
import com.unisoft.algotrader.model.refdata.Exchange;
import com.unisoft.algotrader.model.refdata.Instrument;

import java.util.List;

/**
 * Created by alex on 7/7/15.
 */
public interface RefDataStore {

    void connect();

    void saveCurrency(Currency currency);

    Currency getCurrency(String ccyId);

    List<Currency> getAllCurrencies();

    void saveExchange(Exchange exchange);

    Exchange getExchange(String exchId);

    List<Exchange> getAllExchanges();

    void saveInstrument(Instrument instrument);

    Instrument getInstrument(int instId);

    List<Instrument> getAllInstruments();

    long nextId();
}
