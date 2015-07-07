package com.unisoft.algotrader.persistence.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.unisoft.algotrader.model.refdata.Currency;
import com.unisoft.algotrader.model.refdata.Exchange;
import com.unisoft.algotrader.model.refdata.Instrument;

/**
 * Created by alex on 7/7/15.
 */
public class CassandraRefDataStore implements RefDataStore{

    private Cluster cluster;
    private String keySpace;
    private Session session;
    private MappingManager mappingManager;
    private CurrencyAccessor currencyAccessor;
    private ExchangeAccessor exchangeAccessor;

    public CassandraRefDataStore() {
        this(Cluster.builder().withProtocolVersion(ProtocolVersion.V3).addContactPoint("localhost").build(), "refdata");
    }

    public CassandraRefDataStore(Cluster cluster, String keySpace) {
        this.cluster = cluster;
        this.keySpace = keySpace;
    }

    public void connect() {
        this.session = cluster.connect(keySpace);
        this.mappingManager = new MappingManager(session);
        this.currencyAccessor = mappingManager.createAccessor(CurrencyAccessor.class);
        this.exchangeAccessor = mappingManager.createAccessor(ExchangeAccessor.class);
    }

    public void saveCurrency(Currency currency) {
        Mapper<Currency> mapper = mappingManager.mapper(Currency.class);
        long time = System.currentTimeMillis();
        currency.setBusinesstime(time);
        currency.setSystemtime(time);
        mapper.save(currency);
    }

    public Currency getCurrency(String ccyId) {
        return currencyAccessor.getOne(ccyId);
    }

    public void saveExchange(Exchange exchange) {
        Mapper<Exchange> mapper = mappingManager.mapper(Exchange.class);
        long time = System.currentTimeMillis();
        exchange.setBusinesstime(time);
        exchange.setSystemtime(time);
        mapper.save(exchange);
    }

    public Exchange getExchange(String exchId) {
        return exchangeAccessor.getOne(exchId);
    }

    public void saveInstrument(Instrument instrument) {
        Mapper<Instrument> mapper = mappingManager.mapper(Instrument.class);
//            long time = System.currentTimeMillis();
//            instrument.setBusinessTime(time);
//            instrument.setSystemtime(time);
        mapper.save(instrument);
    }

    public Instrument getInstrument(int instId) {
        Mapper<Instrument> mapper = mappingManager.mapper(Instrument.class);
        return mapper.get(instId);
    }
}