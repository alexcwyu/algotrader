package com.unisoft.algotrader.provider.ib.api.event;

/**
 * Created by alex on 8/26/15.
 */
public class AccountUpdateValueEndEvent extends IBEvent<AccountUpdateValueEndEvent>  {

    public final String accountName;

    public AccountUpdateValueEndEvent(final String accountName){
        this.accountName = accountName;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onAccountUpdateValueEndEvent(this);
    }
}