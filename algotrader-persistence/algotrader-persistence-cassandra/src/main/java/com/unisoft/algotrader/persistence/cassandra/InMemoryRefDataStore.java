package com.unisoft.algotrader.persistence.cassandra;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.unisoft.algotrader.model.refdata.Currency;
import com.unisoft.algotrader.model.refdata.Exchange;
import com.unisoft.algotrader.model.refdata.Instrument;

import java.util.List;

/**
 * Created by alex on 7/9/15.
 */
public class InMemoryRefDataStore implements RefDataStore {

    private final RefDataStore delegateDataStore;

    private LoadingCache<String, Currency> currencyCache = CacheBuilder.newBuilder()
            .build(
                    new CacheLoader<String, Currency>() {
                        public Currency load(String ccyId) {
                            return delegateDataStore.getCurrency(ccyId);
                        }
                    });

    private LoadingCache<String, Exchange> exchangeCache = CacheBuilder.newBuilder()
            .build(
                    new CacheLoader<String, Exchange>() {
                        public Exchange load(String exchId) {
                            return delegateDataStore.getExchange(exchId);
                        }
                    });

    private LoadingCache<Integer, Instrument> instrumentCache = CacheBuilder.newBuilder()
            .build(
                    new CacheLoader<Integer, Instrument>() {
                        public Instrument load(Integer instId) {
                            return delegateDataStore.getInstrument(instId);
                        }
                    });

    public InMemoryRefDataStore(RefDataStore delegateDataStore){
        this.delegateDataStore = delegateDataStore;
    }

    @Override
    public void saveCurrency(Currency currency) {
        delegateDataStore.saveCurrency(currency);
        currencyCache.put(currency.getCcyId(), currency);
    }

    @Override
    public Currency getCurrency(String ccyId) {
        return currencyCache.getUnchecked(ccyId);
    }

    @Override
    public List<Currency> getAllCurrencies() {
        return Lists.newArrayList(currencyCache.asMap().values());
    }

    @Override
    public void saveExchange(Exchange exchange) {
        delegateDataStore.saveExchange(exchange);
        exchangeCache.put(exchange.getExchId(), exchange);

    }

    @Override
    public Exchange getExchange(String exchId) {
        return exchangeCache.getUnchecked(exchId);
    }

    @Override
    public List<Exchange> getAllExchanges() {
        return Lists.newArrayList(exchangeCache.asMap().values());
    }

    @Override
    public void saveInstrument(Instrument instrument) {
        delegateDataStore.saveInstrument(instrument);
        instrumentCache.put(instrument.getInstId(), instrument);
    }

    @Override
    public Instrument getInstrument(int instId) {
        return instrumentCache.getUnchecked(instId);
    }

    @Override
    public List<Instrument> getAllInstruments() {
        return Lists.newArrayList(instrumentCache.asMap().values());
    }
}
