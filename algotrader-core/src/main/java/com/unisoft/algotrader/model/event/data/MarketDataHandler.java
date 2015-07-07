package com.unisoft.algotrader.model.event.data;

import com.unisoft.algotrader.model.event.EventHandler;

/**
 * Created by alex on 4/16/15.
 */
public interface MarketDataHandler extends EventHandler {

    default void onMarketDataContainer(MarketDataContainer container){
        if (container.hasBar()){
            onBar(container.bar);
        }
        if (container.hasQuote()){
            onQuote(container.quote);
        }
        if (container.hasTrade()){
            onTrade(container.trade);
        }
    }

    void onBar(Bar bar);

    void onQuote(Quote quote);

    void onTrade(Trade trade);

}

