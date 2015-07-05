package com.unisoft.algotrader.persistence.cassandra.objectmapper;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.unisoft.algotrader.core.Currency;
import com.unisoft.algotrader.core.Exchange;

/**
 * Created by alex on 6/28/15.
 */
public class ObjectMapperTester2 {

    public static void test1(){

        SimpleClient client = new SimpleClient();
        client.connect("127.0.0.1");
        MappingManager manager = new MappingManager(client.getSession());
        Mapper<Currency> currencyMapper = manager.mapper(Currency.class);
        Mapper<Exchange> exchangeMapper = manager.mapper(Exchange.class);
        //Mapper<Instrument> instrumentMapper = manager.mapper(Instrument.class);

//      UDTMapper<Address> addressUDTMapper = new MappingManager(client.getSession())
//                .udtMapper(Address.class);


        //Phone phone = new Phone(Phone.Type.Home, "707-555-3537");
        //List<Phone> phones = new ArrayList<Phone>();
        //phones.add(phone);
        Currency currency = new Currency("HKD", "HK Dollar");
        currencyMapper.save(currency);

        Exchange exchange = new Exchange("SEHK", "HKEX");
        exchangeMapper.save(exchange);

        System.out.println("Done");
    }

    @Accessor
    interface CurrencyAccesor {

        @Query("SELECT * FROM refdata.currency where ccy_id = :ccy_id")
        Currency byCCyId(@Param("ccy_id") String ccy_id);

        @Query("SELECT * FROM refdata.currency")
        Result<Currency> getAll();

    }

    public static void main(String [] args){
        test1();
    }
}
