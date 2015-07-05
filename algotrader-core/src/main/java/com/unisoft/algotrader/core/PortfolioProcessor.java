package com.unisoft.algotrader.core;

import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.event.Event;
import com.unisoft.algotrader.event.data.Bar;
import com.unisoft.algotrader.event.data.MarketDataHandler;
import com.unisoft.algotrader.event.data.Quote;
import com.unisoft.algotrader.event.data.Trade;
import com.unisoft.algotrader.event.execution.ExecutionHandler;
import com.unisoft.algotrader.event.execution.ExecutionReport;
import com.unisoft.algotrader.event.execution.Order;
import com.unisoft.algotrader.event.execution.OrderHandler;
import com.unisoft.algotrader.threading.MultiEventProcessor;
import com.unisoft.algotrader.threading.disruptor.waitstrategy.NoWaitStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by alex on 7/1/15.
 */
public class PortfolioProcessor extends MultiEventProcessor implements MarketDataHandler, OrderHandler, ExecutionHandler {

    private static final Logger LOG = LogManager.getLogger(Portfolio.class);
    private final Portfolio portfolio;


    public PortfolioProcessor(Portfolio portfolio, RingBuffer... providers) {
        super(new NoWaitStrategy(), null, providers);
        this.portfolio = portfolio;
    }

    @Override
    public void onEvent(Event event) {
        event.on(this);
    }

    @Override
    public void onBar(Bar bar) {
        if (portfolio.getPositions().containsKey(bar.instId)){
            portfolio.getPositions().get(bar.instId).onBar(bar);
        }
    }

    @Override
    public void onQuote(Quote quote) {
        if (portfolio.getPositions().containsKey(quote.instId)){
            portfolio.getPositions().get(quote.instId).onQuote(quote);
        }
    }

    @Override
    public void onTrade(Trade trade) {
        if (portfolio.getPositions().containsKey(trade.instId)){
            portfolio.getPositions().get(trade.instId).onTrade(trade);
        }
    }


    @Override
    public void onExecutionReport(ExecutionReport executionReport) {
    }

    @Override
    public void onOrder(Order order) {
        portfolio.add(order);
    }

}
