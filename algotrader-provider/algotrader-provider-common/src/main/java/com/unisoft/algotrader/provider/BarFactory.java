package com.unisoft.algotrader.provider;


import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.event.data.*;
import com.unisoft.algotrader.utils.threading.disruptor.MultiEventProcessor;
import com.unisoft.algotrader.utils.threading.disruptor.waitstrategy.NoWaitStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by alex on 5/21/15.
 */
public class BarFactory extends MultiEventProcessor implements MarketDataHandler {

    private static final Logger LOG = LogManager.getLogger(BarFactory.class);

    private final RingBuffer<MarketDataContainer> outputRB;

    public BarFactory(RingBuffer<MarketDataContainer> inputRB, RingBuffer<MarketDataContainer> outputRB){
        super(new NoWaitStrategy(), inputRB);
        this.outputRB = outputRB;
    }

    @Override
    public void onMarketDataContainer(MarketDataContainer data) {
        LOG.info("onMarketDataContainer");
        long sequence = outputRB.next();

        MarketDataContainer event = outputRB.get(sequence);
        event.reset();
        event.copy(data);
        outputRB.publish(sequence);
    }

    @Override
    public void onBar(Bar bar) {
        LOG.info("onBar");
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
