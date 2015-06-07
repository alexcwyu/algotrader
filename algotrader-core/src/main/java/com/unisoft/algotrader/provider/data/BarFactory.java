package com.unisoft.algotrader.provider.data;


import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.event.Event;
import com.unisoft.algotrader.event.data.*;
import com.unisoft.algotrader.threading.AbstractEventProcessor;
import com.unisoft.algotrader.threading.YieldMultiBufferWaitStrategy;

/**
 * Created by alex on 5/21/15.
 */
public class BarFactory extends AbstractEventProcessor implements MarketDataHandler{

    private final RingBuffer<MarketDataContainer> outputRB;

    public BarFactory(RingBuffer<MarketDataContainer> inputRB, RingBuffer<MarketDataContainer> outputRB){
        super(new YieldMultiBufferWaitStrategy(),  null, inputRB);
        this.outputRB = outputRB;
    }

    @Override
    public void onMarketDataContainer(MarketDataContainer data) {
       // System.out.println("BarFactory, onMarketDataContainer=" + data);
        long sequence = outputRB.next();

        MarketDataContainer event = outputRB.get(sequence);
        event.reset();
        event.copy(data);
        outputRB.publish(sequence);
    }

    @Override
    public void onBar(Bar bar) {
        System.out.println("BarFactory, onBar=" + bar);
        long sequence = outputRB.next();

        MarketDataContainer event = outputRB.get(sequence);
        event.reset();
        event.setBar(bar);
        outputRB.publish(sequence);
    }

    @Override
    public void onQuote(Quote quote) {
        long sequence = outputRB.next();

        MarketDataContainer event = outputRB.get(sequence);
        event.reset();
        event.setQuote(quote);
        outputRB.publish(sequence);
    }

    @Override
    public void onTrade(Trade trade) {
        long sequence = outputRB.next();

        MarketDataContainer event = outputRB.get(sequence);
        event.reset();
        event.setTrade(trade);
        outputRB.publish(sequence);
    }


    @Override
    public void onEvent(Event event) {
        event.on(this);
    }
}
