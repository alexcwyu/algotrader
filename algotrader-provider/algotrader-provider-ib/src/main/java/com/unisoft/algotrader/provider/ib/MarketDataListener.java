package com.unisoft.algotrader.provider.ib;

import ch.aonyx.broker.ib.api.EventListener;
import ch.aonyx.broker.ib.api.Session;
import com.unisoft.algotrader.model.event.EventBus;
import com.unisoft.algotrader.provider.data.SubscriptionKey;

/**
 * Created by alex on 7/27/15.
 */
public abstract class MarketDataListener implements EventListener {

    public final Session session;
    public final SubscriptionKey subscriptionKey;
    public final EventBus.MarketDataEventBus eventBus;

    public MarketDataListener(Session session, SubscriptionKey subscriptionKey, EventBus.MarketDataEventBus eventBus){
        this.session = session;
        this.subscriptionKey = subscriptionKey;
        this.eventBus = eventBus;
    }

    public void publish(final String dateTime,
            final double open,
            final double high,
            final double low,
            final double close,
            final int volume){
       publish(IBUtils.convertDate(dateTime), open, high, low, close, volume);
    }

    public void publish(final long dateTime,
                        final double open,
                        final double high,
                        final double low,
                        final double close,
                        final int volume){
        eventBus.publishBar(subscriptionKey.instId, subscriptionKey.barSize, dateTime,
                open, high, low, close, volume, 0);
    }

    public void subscribe(){
        session.registerListener(this);
    }

    public void unsubscribe(){
        session.unregisterListener(this);
    }
}
