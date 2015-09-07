package com.unisoft.algotrader.provider.ib.neoib;

import ch.aonyx.broker.ib.api.NeoIbApiClient;
import ch.aonyx.broker.ib.api.contract.Contract;
import ch.aonyx.broker.ib.api.data.bar.RealTimeBarDataType;
import ch.aonyx.broker.ib.api.data.bar.RealTimeBarSubscriptionRequest;
import ch.aonyx.broker.ib.api.net.ConnectionParameters;
import ch.aonyx.broker.ib.api.order.Order;
import ch.aonyx.broker.ib.api.order.*;
import ch.aonyx.broker.ib.api.account.*;
import ch.aonyx.broker.ib.api.execution.*;
/**
 * Created by alex on 6/24/15.
 */
public class NewOrderSample {
    public static void main(String [] args) throws Exception{
        NeoIbApiClient apiClient = new NeoIbApiClient(new MyClientCallback());

        MyConnectionCallback connectionCallback = new MyConnectionCallback();
        apiClient.connect(new ConnectionParameters("localhost", 4001, 2), connectionCallback);

        connectionCallback.registerListener(new PortfolioUpdateEventLoggingListener());
        connectionCallback.registerListener(new RetrieveOpenOrderEndEventLoggingListener());
        connectionCallback.registerListener(new RetrieveOpenOrderEventLoggingListener());
        connectionCallback.registerListener(new ExecutionReportEventLoggingListener());
        connectionCallback.registerListener(new ExecutionReportEndEventLoggingListener());

        Contract contract = ContractUtil.getForexContract("EUR");
        Order order = new Order();
        order.setAccountName("");
        order.setAction(OrderAction.BUY);
        order.setLimitPrice(1.1);
        order.setOrderType(OrderType.LIMIT);
        order.setTimeInForce(TimeInForce.DAY);
        order.setTotalQuantity(1000000);

        PlaceOrderRequest request=new PlaceOrderRequest(order, contract);
        connectionCallback.orderRequest(request);


        Thread.sleep(2000);
        connectionCallback.orderRequest(new CancelOrderRequest(request.getId()));
        Thread.sleep(2000);

    }
}
