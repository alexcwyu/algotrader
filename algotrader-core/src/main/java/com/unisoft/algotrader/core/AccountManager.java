package com.unisoft.algotrader.core;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alex on 5/27/15.
 */
public class AccountManager {

    public static final AccountManager INSTANCE;


    public static final Account DEFAULT_ACCOUNT = new Account("Test", "Testing Account", Currency.USD, 1000000);

    static {
        INSTANCE = new AccountManager();
        INSTANCE.add(DEFAULT_ACCOUNT);
    }


    private AccountManager(){
    }

    private Map<String, Account> map = Maps.newConcurrentMap();

    public void add(Account account){
        map.put(account.getAccountId(), account);
    }

    public Account get(String name){
        return map.get(name);
    }
}
