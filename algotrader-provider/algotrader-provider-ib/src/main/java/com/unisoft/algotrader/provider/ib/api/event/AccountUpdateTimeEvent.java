package com.unisoft.algotrader.provider.ib.api.event;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

/**
 * Created by alex on 8/26/15.
 */
public class AccountUpdateTimeEvent extends IBEvent<AccountUpdateTimeEvent>  {

    public final String time;

    public AccountUpdateTimeEvent(final String time){
        this.time = time;
    }

    public DateTime getDateTime() {
        return LocalTime.parse(time).toDateTimeToday();
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onAccountUpdateTimeEvent(this);
    }
}