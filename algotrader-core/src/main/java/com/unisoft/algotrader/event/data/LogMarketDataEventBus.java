package com.unisoft.algotrader.event.data;

import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.core.id.InstId;
import com.unisoft.algotrader.event.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by alex on 6/18/15.
 */
public class LogMarketDataEventBus implements EventBus.MarketDataEventBus {

    private static final Logger LOG = LogManager.getLogger(LogMarketDataEventBus.class);

    public LogMarketDataEventBus(){
    }

    @Override
    public void publishBar(InstId instId, int size, long dateTime, double open, double high, double low, double close, long volume, long openInt) {
        LOG.info("publishBar, instId = {}, size = {}, dateTime = {}, open = {}, high = {}, low = {}, close = {}, volume = {}, openInt = {}"
                , instId, size, dateTime, open, high, low, close, volume, openInt);

    }

    @Override
    public void publishQuote(InstId instId, long dateTime, double bid, double ask, int bidSize, int askSize) {
        LOG.info("publishQuote, instId = {}, dateTime = {}, bid = {}, ask = {}, bidSize = {}, askSize = {}"
                , instId, dateTime, bid, ask, bidSize, askSize);
    }

    @Override
    public void publishTrade(InstId instId, long dateTime, double price, int size) {
        LOG.info("publishTrade, instId = {}, dateTime = {}, price = {}, size = {}"
                , instId, dateTime, price, size);
    }
}
