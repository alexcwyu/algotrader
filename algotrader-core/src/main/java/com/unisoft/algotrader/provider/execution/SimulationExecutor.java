package com.unisoft.algotrader.provider.execution;

import com.google.common.collect.Maps;
import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.clock.Clock;
import com.unisoft.algotrader.clock.SimulationClock;
import com.unisoft.algotrader.core.OrdStatus;
import com.unisoft.algotrader.core.OrdType;
import com.unisoft.algotrader.event.Event;
import com.unisoft.algotrader.event.data.*;
import com.unisoft.algotrader.event.execution.*;
import com.unisoft.algotrader.order.OrderManager;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.provider.data.InstrumentDataManager;
import com.unisoft.algotrader.provider.execution.simulation.*;
import com.unisoft.algotrader.threading.AbstractEventProcessor;
import com.unisoft.algotrader.threading.YieldMultiBufferWaitStrategy;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import static com.unisoft.algotrader.provider.execution.SimulatorConfig.FillOnBarMode;
import static com.unisoft.algotrader.provider.execution.SimulatorConfig.FillOnQuoteMode;
import static com.unisoft.algotrader.provider.execution.SimulatorConfig.FillOnTradeMode;
/**
 * Created by alex on 5/18/15.
 */
public class SimulationExecutor extends AbstractEventProcessor implements ExecutionProvider, MarketDataHandler {

    public static final String PROVIDER_ID = "Simulated";

    private final OrderManager orderManager;

    //commission Provider
    //slippage provider

    private AtomicLong execId;
    private LimitOrderHandler limitOrderHandler;
    private MarketOrderHandler marketOrderHandler;
    private StopLimitOrderHandler stopLimitOrderHandler;
    private StopOrderHandler stopOrderHandler;
    private TrailingStopOrderHandler trailingStopOrderHandler;

    private Map<String, Map<Long, Order>> orderMap = Maps.newConcurrentMap();
    private Map<String, Quote> quoteMap = Maps.newHashMap();

    public SimulatorConfig config = new SimulatorConfig();
    private Clock clock = new SimulationClock();

    public SimulationExecutor(OrderManager orderManager, RingBuffer... rbs) {
        super(new YieldMultiBufferWaitStrategy(),  null, rbs);
        this.orderManager = orderManager;
        ProviderManager.INSTANCE.registerExecutionProvider(this);

        this.execId = new AtomicLong();

        this.limitOrderHandler = new LimitOrderHandler(config, this);
        this.marketOrderHandler = new MarketOrderHandler(config, this);
        this.stopLimitOrderHandler = new StopLimitOrderHandler(config, this);
        this.stopOrderHandler = new StopOrderHandler(config, this);
        this.trailingStopOrderHandler = new TrailingStopOrderHandler(config, this);

    }

    @Override
    public String providerId() {
        return PROVIDER_ID;
    }

    public void sendExecutionReport(Order order, double qty, double price, OrdStatus status){

        ExecutionReport report = new ExecutionReport();
        report.execId = execId.getAndIncrement();
        report.orderId = order.orderId;
        report.instId = order.instId;
        report.ordType = order.ordType;
        report.side = order.side;
        report.limitPrice = order.limitPrice;
        report.stopPx = order.stopPx;
        report.ordQty = order.ordQty;


        report.filledQty = order.filledQty + qty;
        if (report.filledQty != 0) {
            report.avgPx = (order.avgPx * order.filledQty + price * qty) / (order.filledQty + qty);
        }

        report.lastQty = qty;
        report.lastPrice = price;

        report.text = order.text;
        report.ordStatus = status;

        orderManager.onExecutionReport(report);
    }

    @Override
    public void onOrder(Order order) {
        sendExecutionReport(order, 0, 0, order.ordStatus);

        if (!processNewOrder(order)){
            addOrder(order);
        }
    }

    private void addOrder(Order order){
        Map<Long, Order> orders = orderMap.get(order.instId);
        if (orders == null){
            orders = Maps.newConcurrentMap();
            orderMap.put(order.instId, orders);
        }
        orders.put(order.orderId, order);
    }

    private void removeOrder(Order order){
        Map<Long, Order> orders = orderMap.get(order.instId);
        if (orders != null){
            orders.remove(order.orderId);
        }
//        else
//        {
//            throw new RuntimeException("order not found!");
//        }
    }


    private void cancel(Order order){
        //TODO
    }

    private void replace(Order order){
        //TODO
    }

    public void onOrderCancelRequest(OrderCancelRequest request){
        //TODO
    }

    public void onOrderCancelReplaceRequest(OrderCancelReplaceRequest request){
        //TODO
        // process replace
    }

    public void onOrderStatusRequest(OrderStatusRequest request){
        throw new UnsupportedOperationException();
    }


    private boolean processNewOrder(Order order){

        InstrumentDataManager.InstrumentData instrumentData = InstrumentDataManager.INSTANCE.getInstrumentData(order.instId);

        boolean executed = false;
        // market order
        if (order.ordType == OrdType.Market)
        {
            if (!executed && config.fillOnQuote && config.fillOnQuoteMode == FillOnQuoteMode.LastQuote){
                executed = marketOrderHandler.process(order, instrumentData.quote);
            }

            if (!executed && config.fillOnTrade && config.fillOnTradeMode == FillOnTradeMode.LastTrade){
                executed = marketOrderHandler.process(order, instrumentData.trade);
            }

            if (!executed && config.fillOnBar && config.fillOnBarMode == FillOnBarMode.LastBarClose){
                executed = marketOrderHandler.process(order, instrumentData.bar);
            }

        }
        // stop/limit orders
        else{
            if (!executed && instrumentData.quote != null && config.fillOnQuote){
                executed = process(order, instrumentData.quote);
            }

            if (!executed && instrumentData.trade != null && config.fillOnTrade){
                executed = process(order, instrumentData.trade);
            }

            if (!executed && instrumentData.bar != null && config.fillOnBar){
                executed = process(order, instrumentData.bar.close, order.ordQty);
            }
        }


        //TODO handle good till time, by adding clock and reminder to cancel order

        return executed;
    }

    public boolean process(Order order, MarketData data){
        if (order.ordType == OrdType.Market){
            return marketOrderHandler.process(order, data);
        }
        else if (order.ordType == OrdType.Limit){
            return limitOrderHandler.process(order, data);
        }
        else if (order.ordType == OrdType.StopLimit){
            return stopLimitOrderHandler.process(order, data);
        }
        else if (order.ordType == OrdType.Stop){
            return stopOrderHandler.process(order, data);
        }
        else if (order.ordType == OrdType.TrailingStop){
            return trailingStopOrderHandler.process(order, data);
        }
        return false;
    }

    public boolean process(Order order, double price, double qty){
        if (order.ordType == OrdType.Market){
            return marketOrderHandler.process(order, price, qty);
        }
        else if (order.ordType == OrdType.Limit){
            return limitOrderHandler.process(order, price, qty);
        }
        else if (order.ordType == OrdType.StopLimit){
            return stopLimitOrderHandler.process(order, price, qty);
        }
        else if (order.ordType == OrdType.Stop){
            return stopOrderHandler.process(order, price, qty);
        }
        else if (order.ordType == OrdType.TrailingStop){
            return trailingStopOrderHandler.process(order, price, qty);
        }
        return false;
    }


    public boolean execute(Order order, double filledPrice, double qty){
        if (order.isDone())
            return false;

        if (qty < order.leaveQty()){
            // partially filled
            sendExecutionReport(order, qty, filledPrice, OrdStatus.PartiallyFilled);
            return false;
        }
        else{
            // fully filled

            double filledQty = order.leaveQty();
            sendExecutionReport(order, filledQty, filledPrice, OrdStatus.Filled);
            removeOrder(order);
            return true;
        }
    }

    @Override
    public void onBar(Bar bar) {
        //TODO Bar filter

        clock.setDateTime(bar.dateTime);

        if (config.fillOnBar && orderMap.containsKey(bar.instId)){
            for (Order order :orderMap.get(bar.instId).values()){

                boolean executed = false;

                if (config.fillOnBarMode == FillOnBarMode.NextBarOpen)
                    //try bar open first
                    executed = process(order, bar.open, order.ordQty);

                //then try others
                if(!executed){
                    executed = process(order, bar);
                }

            }
        }
    }

    @Override
    public void onQuote(Quote quote) {
        clock.setDateTime(quote.dateTime);

        boolean diffAsk = true;
        boolean diffBid = true;

        if (quoteMap.containsKey(quote.instId)){
            diffAsk = quoteMap.get(quote.instId).ask !=quote.ask ||
                    quoteMap.get(quote.instId).askSize !=quote.askSize;
            diffBid = quoteMap.get(quote.instId).bid !=quote.bid ||
                    quoteMap.get(quote.instId).bidSize !=quote.bidSize;
        }

        quoteMap.put(quote.instId, quote);

        if (config.fillOnQuote && orderMap.containsKey(quote.instId)){
            for (Order order :orderMap.get(quote.instId).values()) {
                switch (order.side){
                    case Buy:
                    case BuyMinus:
                        if (diffAsk)
                            process(order, quote);
                        break;
                    case Sell:
                    case SellShort:
                        if (diffBid)
                            process(order, quote);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void onTrade(Trade trade) {
        clock.setDateTime(trade.dateTime);
        if (config.fillOnTrade && orderMap.containsKey(trade.instId)){
            for (Order order :orderMap.get(trade.instId).values()) {
                process(order, trade);
            }
        }
    }

    @Override
    public void onEvent(Event event) {
        event.on(this);
    }

    @Override
    public boolean connected(){
        return true;
    }
}

