package com.unisoft.algotrader.provider.ib.api.event;

import org.joda.time.DateTime;

/**
 * Created by alex on 8/26/15.
 */
public class ServerCurrentTimeEvent extends IBEvent<ServerCurrentTimeEvent>  {

    public final long timestamp;

    public ServerCurrentTimeEvent(final long timestamp){
        this.timestamp = timestamp;
    }

    public DateTime getDateTime() {
        return new DateTime(timestamp * 1000);
    }

}