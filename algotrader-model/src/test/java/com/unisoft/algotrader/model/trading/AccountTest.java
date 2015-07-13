package com.unisoft.algotrader.model.trading;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.refdata.Currency;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by alex on 5/31/15.
 */
public class AccountTest {

    Account account;
    @Before
    public void setup(){
        account = new Account("Test", "", Currency.USD, 100000);
    }

    @Test
    public void test_add(){

        Map<String, Double> expResult =  Maps.newHashMap();
        expResult.put(Currency.USD.getCcyId(), 100000.0);

        assertEquals(100000, account.value(Currency.USD), 0.0);
        assertEquals(expResult, account.valueAsMap());

        account.add(new AccountTransaction(System.currentTimeMillis(), Currency.USD, 500, ""));
        expResult.clear();
        expResult.put(Currency.USD.getCcyId(), 100500.0);
        assertEquals(100500, account.value(Currency.USD), 0.0);
        assertEquals(expResult, account.valueAsMap());

        account.add(new AccountTransaction(System.currentTimeMillis(), Currency.USD, -1500, ""));
        expResult.clear();
        expResult.put(Currency.USD.getCcyId(), 99000.0);
        assertEquals(99000, account.value(Currency.USD), 0.0);
        assertEquals(expResult, account.valueAsMap());


        account.add(new AccountTransaction(System.currentTimeMillis(), Currency.HKD, 19500, ""));
        expResult.clear();
        expResult.put(Currency.USD.getCcyId(), 99000.0);
        expResult.put(Currency.HKD.getCcyId(), 19500.0);
        assertEquals(99000.0, account.value(Currency.USD), 0.0);
        assertEquals(19500, account.value(Currency.HKD), 0.0);
        assertEquals(expResult, account.valueAsMap());

        account.add(new AccountTransaction(System.currentTimeMillis(), Currency.HKD, -500, ""));
        expResult.clear();
        expResult.put(Currency.USD.getCcyId(), 99000.0);
        expResult.put(Currency.HKD.getCcyId(), 19000.0);
        assertEquals(99000.0, account.value(Currency.USD), 0.0);
        assertEquals(19000.0, account.value(Currency.HKD), 0.0);
        assertEquals(expResult, account.valueAsMap());

    }

    @Test
    public void test_withdraw(){
        Map<String, Double> expResult =  Maps.newHashMap();
        expResult.put(Currency.USD.getCcyId(), 100000.0);

        assertEquals(100000, account.value(Currency.USD), 0.0);
        assertEquals(expResult, account.valueAsMap());

        account.withdraw(System.currentTimeMillis(), Currency.USD, 500, "");
        expResult.clear();
        expResult.put(Currency.USD.getCcyId(), 99500.0);
        assertEquals(99500, account.value(Currency.USD), 0.0);
        assertEquals(expResult, account.valueAsMap());

        account.withdraw(System.currentTimeMillis(), Currency.USD, 1500, "");
        expResult.clear();
        expResult.put(Currency.USD.getCcyId(), 98000.0);
        assertEquals(98000, account.value(Currency.USD), 0.0);
        assertEquals(expResult, account.valueAsMap());

        account.withdraw(System.currentTimeMillis(), Currency.HKD, 19500, "");
        expResult.clear();
        expResult.put(Currency.USD.getCcyId(), 98000.0);
        expResult.put(Currency.HKD.getCcyId(), -19500.0);
        assertEquals(98000.0, account.value(Currency.USD), 0.0);
        assertEquals(-19500.0, account.value(Currency.HKD), 0.0);
        assertEquals(expResult, account.valueAsMap());

        account.withdraw(System.currentTimeMillis(), Currency.HKD, 500, "");
        expResult.clear();
        expResult.put(Currency.USD.getCcyId(), 98000.0);
        expResult.put(Currency.HKD.getCcyId(), -20000.0);
        assertEquals(98000.0, account.value(Currency.USD), 0.0);
        assertEquals(-20000.0, account.value(Currency.HKD), 0.0);
        assertEquals(expResult, account.valueAsMap());

    }

    @Test
    public void test_deposit(){
        Map<String, Double> expResult =  Maps.newHashMap();
        expResult.put(Currency.USD.getCcyId(), 100000.0);

        assertEquals(100000, account.value(Currency.USD), 0.0);
        assertEquals(expResult, account.valueAsMap());

        account.deposit(System.currentTimeMillis(), Currency.USD, 500, "");
        expResult.clear();
        expResult.put(Currency.USD.getCcyId(), 100500.0);
        assertEquals(100500, account.value(Currency.USD), 0.0);
        assertEquals(expResult, account.valueAsMap());

        account.deposit(System.currentTimeMillis(), Currency.USD, 1500, "");
        expResult.clear();
        expResult.put(Currency.USD.getCcyId(), 102000.0);
        assertEquals(102000, account.value(Currency.USD), 0.0);
        assertEquals(expResult, account.valueAsMap());


        account.deposit(System.currentTimeMillis(), Currency.HKD, 19500, "");
        expResult.clear();
        expResult.put(Currency.USD.getCcyId(), 102000.0);
        expResult.put(Currency.HKD.getCcyId(), 19500.0);
        assertEquals(102000, account.value(Currency.USD), 0.0);
        assertEquals(19500, account.value(Currency.HKD), 0.0);
        assertEquals(expResult, account.valueAsMap());

        account.deposit(System.currentTimeMillis(), Currency.HKD, 500, "");
        expResult.clear();
        expResult.put(Currency.USD.getCcyId(), 102000.0);
        expResult.put(Currency.HKD.getCcyId(), 20000.0);
        assertEquals(102000, account.value(Currency.USD), 0.0);
        assertEquals(20000.0, account.value(Currency.HKD), 0.0);
        assertEquals(expResult, account.valueAsMap());

    }
}
