package com.unisoft.algotrader.persistence;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.refdata.Currency;

import javax.inject.Singleton;

import java.util.Collections;

import static com.unisoft.algotrader.model.refdata.Currency.*;
import static com.unisoft.algotrader.model.refdata.Exchange.*;

/**
 * Created by alex on 7/14/15.
 */
@Singleton
public class SampleInMemoryRefDataStore extends InMemoryRefDataStore {
    public SampleInMemoryRefDataStore(){
        super();
        this.saveCurrency(USD);
        this.saveCurrency(HKD);

        this.saveCurrency(CNY);
        this.saveCurrency(RUR);
        this.saveCurrency(AUD);
        this.saveCurrency(NZD);
        this.saveCurrency(CAD);

        this.saveCurrency(GBP);
        this.saveCurrency(EUR);
        this.saveCurrency(JPY);
        this.saveCurrency(CHF);
        this.saveCurrency(SGD);
        this.saveCurrency(KRW);
        this.saveCurrency(INR);

        this.saveExchange(HKEX);
        this.saveExchange(HKFE);
        this.saveExchange(SEHKNTL);
        this.saveExchange(NSE);
        this.saveExchange(CHIXJ);
        this.saveExchange(OSE);
        this.saveExchange(TSEJ);
        this.saveExchange(SGX);
        this.saveExchange(KSE);
        this.saveExchange(ASX);
        this.saveExchange(IDEAL);
        this.saveExchange(IDEALPRO);
        this.saveExchange(LSE);
        this.saveExchange(SWX);
        this.saveExchange(FWB);
        this.saveExchange(CFE);
        this.saveExchange(ECBOT);
        this.saveExchange(CBOE);
        this.saveExchange(CHX);
        this.saveExchange(GLOBEX);
        this.saveExchange(NYBOT);
        this.saveExchange(ICEUS);
        this.saveExchange(ISE);
        this.saveExchange(NASDAQ);
        this.saveExchange(AMEX);
        this.saveExchange(ARCA);
        this.saveExchange(PSE);
        this.saveExchange(NYMEX);
        this.saveExchange(NYSE);
        this.saveExchange(TSE);

        InstrumentFactory instrumentFactory = new InstrumentFactory(this);
        instrumentFactory.createIndex("HSI", "HSI Index", HKEX.getExchId(), Currency.HKD.getCcyId());
        instrumentFactory.createFuture("HSIF", "HSI Future", HKFE.getExchId(), Currency.HKD.getCcyId(), Collections.singletonMap("IB", "HISF"), Maps.newHashMap());
        instrumentFactory.createFuture("HSCEIF", "HSCEI Future", HKFE.getExchId(), Currency.HKD.getCcyId(), Collections.singletonMap("IB", "HHI.HK"), Maps.newHashMap());

        instrumentFactory.createStock("0005.HK", "HSBC HOLDINGS", HKEX.getExchId(), Currency.HKD.getCcyId(), Collections.singletonMap("IB", "5"), Collections.singletonMap("IB", "SEHK"));
        instrumentFactory.createStock("0959.HK", "AMAX INT HOLD", HKEX.getExchId(), Currency.HKD.getCcyId(), Collections.singletonMap("IB", "959"), Collections.singletonMap("IB", "SEHK"));
        instrumentFactory.createStock("2628.HK", "China Life", HKEX.getExchId(), Currency.HKD.getCcyId(), Collections.singletonMap("IB", "2628"), Collections.singletonMap("IB", "SEHK"));

        instrumentFactory.createStock("AAPL", "Apple Inc.", NASDAQ.getExchId(), Currency.USD.getCcyId());
        instrumentFactory.createStock("GOOG", "Google Inc.", NASDAQ.getExchId(), Currency.USD.getCcyId());
        instrumentFactory.createStock("FB", "Facebook, Inc.", NASDAQ.getExchId(), Currency.USD.getCcyId());
        instrumentFactory.createStock("TSLA", "Tesla Motors, Inc.", NASDAQ.getExchId(), Currency.USD.getCcyId());

        instrumentFactory.createStock("IBM", "International Business Machines Corporation", NYSE.getExchId(), Currency.USD.getCcyId());
    }
}
