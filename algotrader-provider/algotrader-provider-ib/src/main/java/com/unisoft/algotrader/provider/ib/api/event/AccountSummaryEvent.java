package com.unisoft.algotrader.provider.ib.api.event;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

/**
 * Created by alex on 8/26/15.
 */
public class AccountSummaryEvent extends IBEvent<AccountSummaryEvent>  {

    public final int reqId;
    public final String account;
    public final String tag;
    public final String value;
    public final String currency;

    public AccountSummaryEvent(final int reqId, final String account, final String tag, final String value, final String currency){
        this.reqId = reqId;
        this.account = account;
        this.tag = tag;
        this.value = value;
        this.currency = currency;
    }

}