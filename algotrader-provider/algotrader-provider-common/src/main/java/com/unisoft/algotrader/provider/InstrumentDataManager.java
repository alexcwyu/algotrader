package com.unisoft.algotrader.provider;

import com.google.common.collect.Maps;
import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.event.data.*;
import com.unisoft.algotrader.utils.threading.disruptor.MultiEventProcessor;
import com.unisoft.algotrader.utils.threading.disruptor.waitstrategy.NoWaitStrategy;

import javax.inject.Singleton;
import java.util.Map;

/**
 * Created by alex on 5/21/15.
 */
@Singleton
public class InstrumentDataManager extends MultiEventProcessor implements MarketDataHandler {

    @Override
    public void onMarketDataContainer(MarketDataContainer data) {
        if (data.bitset.get(MarketDataContainer.BAR_BIT))
            onBar(data.bar);
        if (data.bitset.get(MarketDataContainer.QUOTE_BIT))
            onQuote(data.quote);
        if (data.bitset.get(MarketDataContainer.TRADE_BIT))
            onTrade(data.trade);
    }

    public InstrumentData getInstrumentData(long instId){
        InstrumentData data = map.get(instId);
        if (data == null){
            data = new InstrumentData(instId);
            map.put(instId, data);
        }
        return data;
    }

    @Override
    public void onBar(Bar bar) {
        InstrumentData data = getInstrumentData(bar.instId);
        data.bar = bar;
    }

    @Override
    public void onQuote(Quote quote) {
        InstrumentData data = getInstrumentData(quote.instId);
        data.quote = quote;
    }

    @Override
    public void onTrade(Trade trade) {
        InstrumentData data = getInstrumentData(trade.instId);
        data.trade = trade;
    }

    public static class InstrumentData {
        public final long instId;
        public Bar bar;
        public Quote quote;
        public Trade trade;

        public InstrumentData(long instId){
            this.instId = instId;
        }
    }

    public Map<Long, InstrumentData> map = Maps.newHashMap();

    public InstrumentDataManager(RingBuffer ringBuffer){
        super(new NoWaitStrategy(),  null, ringBuffer);
    }

    @Override
    public void onEvent(Event event) {
        event.on(this);
    }

    public void clear(){
        this.map.clear();
    }
}
