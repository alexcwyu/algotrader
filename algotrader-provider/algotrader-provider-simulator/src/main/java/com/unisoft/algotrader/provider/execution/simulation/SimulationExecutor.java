package com.unisoft.algotrader.provider.execution.simulation;

import com.google.common.collect.Maps;
import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.config.AppConfig;
import com.unisoft.algotrader.model.clock.Clock;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.event.data.*;
import com.unisoft.algotrader.model.event.execution.*;
import com.unisoft.algotrader.model.trading.OrdStatus;
import com.unisoft.algotrader.model.trading.OrdType;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.provider.execution.ExecutionProvider;
import com.unisoft.algotrader.trading.InstrumentDataManager;
import com.unisoft.algotrader.trading.OrderManager;
import com.unisoft.algotrader.utils.threading.disruptor.MultiEventProcessor;
import com.unisoft.algotrader.utils.threading.disruptor.waitstrategy.NoWaitStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by alex on 5/18/15.
 */
@Singleton
public class SimulationExecutor extends MultiEventProcessor implements ExecutionProvider, MarketDataHandler {

    private static final Logger LOG = LogManager.getLogger(SimulationExecutor.class);

    public static final String PROVIDER_ID = "Simulated";

    private final OrderManager orderManager;
    private final InstrumentDataManager instrumentDataManager;

    //commission Provider
    //slippage provider

    private AtomicLong execId;
    private LimitOrderHandler limitOrderHandler;
    private MarketOrderHandler marketOrderHandler;
    private StopLimitOrderHandler stopLimitOrderHandler;
    private StopOrderHandler stopOrderHandler;
    private TrailingStopOrderHandler trailingStopOrderHandler;

    private Map<Long, Map<Long, Order>> orderMap = Maps.newConcurrentMap();
    private Map<Long, Quote> quoteMap = Maps.newHashMap();

    public SimulatorConfig config = new SimulatorConfig();
    private Clock clock;

    @Inject
    public SimulationExecutor(AppConfig config, @Nullable RingBuffer ... rbs){
        this(config.getProviderManager(), config.getOrderManager(), config.getInstrumentDataManager(), config.getClock(), (rbs == null || rbs.length ==0) ? new RingBuffer[]{config.getEventBusManager().marketDataRB} : rbs);
    }

    public SimulationExecutor(ProviderManager providerManager, OrderManager orderManager, InstrumentDataManager instrumentDataManager, Clock clock, @Nullable RingBuffer... rbs) {
        super(new NoWaitStrategy(), null, rbs);

        this.orderManager = orderManager;
        this.instrumentDataManager = instrumentDataManager;
        this.clock = clock;

        this.execId = new AtomicLong();

        this.limitOrderHandler = new LimitOrderHandler(config, this);
        this.marketOrderHandler = new MarketOrderHandler(config, this);
        this.stopLimitOrderHandler = new StopLimitOrderHandler(config, this);
        this.stopOrderHandler = new StopOrderHandler(config, this);
        this.trailingStopOrderHandler = new TrailingStopOrderHandler(config, this);

        providerManager.addExecutionProvider(this);
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
        report.stopPrice = order.stopPrice;
        report.ordQty = order.ordQty;


        report.filledQty = order.filledQty + qty;
        if (report.filledQty != 0) {
            report.avgPrice = (order.avgPrice * order.filledQty + price * qty) / (order.filledQty + qty);
        }

        report.lastQty = qty;
        report.lastPrice = price;

        report.text = order.text;
        report.ordStatus = status;

        orderManager.onExecutionReport(report);
    }

    @Override
    public void onOrder(Order order) {
        LOG.info("onOrder = {}", order);

        addOrder(order);

        sendExecutionReport(order, 0, 0, order.ordStatus);

        if (processNewOrder(order)){
            removeOrder(order);
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

        InstrumentDataManager.InstrumentData instrumentData = instrumentDataManager.getInstrumentData(order.instId);

        boolean executed = false;
        // market order
        if (order.ordType == OrdType.Market)
        {
            if (!executed && config.fillOnQuote && config.fillOnQuoteMode == SimulatorConfig.FillOnQuoteMode.LastQuote){
                executed = marketOrderHandler.process(order, instrumentData.quote);
            }

            if (!executed && config.fillOnTrade && config.fillOnTradeMode == SimulatorConfig.FillOnTradeMode.LastTrade){
                executed = marketOrderHandler.process(order, instrumentData.trade);
            }

            if (!executed && config.fillOnBar && config.fillOnBarMode == SimulatorConfig.FillOnBarMode.LastBarClose){
                executed = marketOrderHandler.process(order, instrumentData.bar);
            }

        }
        // close/limit orders
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
        LOG.info("onBar");
        //TODO Bar filter
        instrumentDataManager.onBar(bar);

        clock.setDateTime(bar.dateTime);

        if (config.fillOnBar && orderMap.containsKey(bar.instId)){
            for (Order order :orderMap.get(bar.instId).values()){

                boolean executed = false;

                if (config.fillOnBarMode == SimulatorConfig.FillOnBarMode.NextBarOpen)
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
        instrumentDataManager.onQuote(quote);

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
                    //case BuyMinus:
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
        instrumentDataManager.onTrade(trade);

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

