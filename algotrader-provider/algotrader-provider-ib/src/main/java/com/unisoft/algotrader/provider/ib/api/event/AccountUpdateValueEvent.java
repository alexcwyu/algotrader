package com.unisoft.algotrader.provider.ib.api.event;

/**
 * Created by alex on 8/26/15.
 */
public class AccountUpdateValueEvent extends IBEvent<AccountUpdateValueEvent>  {

    public final String key;
    public final String value;
    public final String currency;
    public final String accountName;

    public AccountUpdateValueEvent(final String key, final String value, final String currency, final String accountName){
        this.key = key;
        this.value = value;
        this.currency = currency;
        this.accountName = accountName;
    }

}