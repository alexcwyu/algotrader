package com.unisoft.algotrader.provider.ib.api.event;


import com.unisoft.algotrader.provider.ib.api.IBConstants;

/**
 * Created by alex on 8/26/15.
 */
public class NewsBulletinUpdateEvent extends IBEvent<NewsBulletinUpdateEvent>  {

    public final int newsBulletinId;
    public final IBConstants.NewsBulletinType newsBulletinType;
    public final String message;
    public final String exchange;

    public NewsBulletinUpdateEvent(final int newsBulletinId, final IBConstants.NewsBulletinType newsBulletinType,
                                   final String message, final String exchange){
        this.newsBulletinId = newsBulletinId;
        this.newsBulletinType = newsBulletinType;
        this.message = message;
        this.exchange = exchange;
    }
}