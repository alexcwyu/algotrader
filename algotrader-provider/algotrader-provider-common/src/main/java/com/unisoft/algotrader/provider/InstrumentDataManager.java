package com.unisoft.algotrader.provider;

import com.google.common.collect.Maps;
import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.event.EventBusManager;
import com.unisoft.algotrader.model.event.data.*;
import com.unisoft.algotrader.threading.MultiEventProcessor;
import com.unisoft.algotrader.threading.disruptor.waitstrategy.NoWaitStrategy;

import java.util.Map;

/**
 * Created by alex on 5/21/15.
 */
public class InstrumentDataManager extends MultiEventProcessor implements MarketDataHandler {

    @Override
    public void onMarketDataContainer(MarketDataContainer data) {
        System.out.println("InstrumentDataManager, onMarketDataContainer=" + data);
        if (data.bitset.get(MarketDataContainer.BAR_BIT))
            onBar(data.bar);
        if (data.bitset.get(MarketDataContainer.QUOTE_BIT))
            onQuote(data.quote);
        if (data.bitset.get(MarketDataContainer.TRADE_BIT))
            onTrade(data.trade);
    }

    public InstrumentData getInstrumentData(int instId){
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
        public final int instId;
        public Bar bar;
        public Quote quote;
        public Trade trade;

        public InstrumentData(int instId){
            this.instId = instId;
        }
    }

    public Map<Integer, InstrumentData> map = Maps.newHashMap();

    public static final InstrumentDataManager INSTANCE;

    static {
        INSTANCE = new InstrumentDataManager();
    }


    public InstrumentDataManager(){
        this(EventBusManager.INSTANCE.marketDataRB);
    }
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
