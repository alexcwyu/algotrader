package com.unisoft.algotrader.provider.ib;

import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.data.HistoricalSubscriptionKey;
import com.unisoft.algotrader.provider.data.MarketDepthSubscriptionKey;
import com.unisoft.algotrader.provider.data.SubscriptionKey;
import com.unisoft.algotrader.provider.ib.api.deserializer.Deserializers;
import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.model.data.MarketDataType;
import com.unisoft.algotrader.provider.ib.api.model.data.MarketScannerFilter;
import com.unisoft.algotrader.provider.ib.api.model.data.ReportType;
import com.unisoft.algotrader.provider.ib.api.model.execution.ExecutionReportFilter;
import com.unisoft.algotrader.provider.ib.api.model.fa.FinancialAdvisorDataType;
import com.unisoft.algotrader.provider.ib.api.model.order.ExerciseAction;
import com.unisoft.algotrader.provider.ib.api.model.system.LogLevel;
import com.unisoft.algotrader.provider.ib.api.serializer.ByteArrayBuilder;
import com.unisoft.algotrader.provider.ib.api.serializer.Serializers;
import com.unisoft.algotrader.utils.threading.NamedThreadFactory;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by alex on 8/1/15.
 */
public class IBSocket {

    private static final Logger LOG = LogManager.getLogger(IBSocket.class);

    private final IBConfig ibConfig;
    private final IBEventHandler eventHandler;
    private final RefDataStore refDataStore;
    private final ExecutorService executor = Executors.newFixedThreadPool(1, new NamedThreadFactory("IBConnection"));

    private int serverCurrentVersion;
    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private EventInputStreamConsumer inputStreamConsumer;
    private Serializers serializers;
    private Deserializers deserializers;

    private Lock lock = new ReentrantLock();


    public IBSocket(IBConfig ibConfig, IBEventHandler eventHandler, RefDataStore refDataStore){
        this.ibConfig = ibConfig;
        this.eventHandler = eventHandler;
        this.refDataStore = refDataStore;
    }

    public int getServerCurrentVersion() {
        return serverCurrentVersion;
    }
    public InputStream getInputStream() {
        return inputStream;
    }

    public void connect(){
        connect(true);
    }

    public void connect(boolean requestAccountUpdate){
        try {
            this.socket = new Socket(ibConfig.host, ibConfig.port);
            this.outputStream = new DataOutputStream(socket.getOutputStream());
            this.inputStream = new DataInputStream(socket.getInputStream());

            handShake();
            startApi(); //set client id
            startStream();
            if(requestAccountUpdate)
                requestAccountUpdates(null);
        }
        catch (IOException e){
            LOG.error(e);
            disconnect();
        }
    }

    private void handShake() {
        try {
            ByteArrayBuilder builder = new ByteArrayBuilder();
            //set client version
            LOG.info("writing client version {}", ibConfig.currentVersion);
            builder.append(ibConfig.currentVersion);
            send(builder.toBytes());
            builder.clear();

            //read server version
            serverCurrentVersion = InputStreamUtils.readInt(inputStream);
            LOG.info("current server version: {}", serverCurrentVersion);

            //read server time
            String serverTime = InputStreamUtils.readString(inputStream);
            LOG.info("server time: {}", serverTime);

            //start Serializer
            initIOSerializers();

            if (serverCurrentVersion < ibConfig.minVersion) {
                final String detailedMessage = "Minimum server version required '" + ibConfig.minVersion
                        + "', current server version '" + serverCurrentVersion + "'";
                throw new Exception(detailedMessage);
            }
        } catch (final Exception e) {
            LOG.error(e);
            disconnect();
        }
    }

    private void initIOSerializers(){
        serializers = new Serializers(serverCurrentVersion, refDataStore);
        deserializers = new Deserializers(serverCurrentVersion, refDataStore);
    }

    private void startStream(){
        inputStreamConsumer = new EventInputStreamConsumer(eventHandler, this, deserializers);
        executor.submit(this.inputStreamConsumer);
    }

    protected void startApi(){
        byte [] bytes = serializers.startAPISerializer().serialize(ibConfig.clientId);
        if (LOG.isDebugEnabled())
            LOG.debug("startApi, {}", new String(bytes));
        send(bytes);
    }
    public void subscribeNewsBulletin(boolean includeExistingDailyNews){
        byte [] bytes = serializers.newsBulletinSubscriptionRequestSerializer().serialize(includeExistingDailyNews);
        if (LOG.isDebugEnabled())
            LOG.debug("subscribeNewsBulletin, {}", new String(bytes));
        send(bytes);
    }

    public void unsubscribeNewsBulletin(){
        byte [] bytes = serializers.newsBulletinUnsubscriptionRequestSerializer().serialize();
        if (LOG.isDebugEnabled())
            LOG.debug("unsubscribeNewsBulletin, {}", new String(bytes));
        send(bytes);
    }

    public void subscribeScanner(long requestId, MarketScannerFilter filter){
        byte [] bytes = serializers.marketScannerSubscriptionRequestSerializer().serialize(requestId, filter);
        if (LOG.isDebugEnabled())
            LOG.debug("subscribeScanner, {}", new String(bytes));
        send(bytes);
    }

    public void unsubscribeScanner(long requestId){
        byte [] bytes = serializers.marketScannerUnsubscriptionRequestSerializer().serialize(requestId);
        if (LOG.isDebugEnabled())
            LOG.debug("unsubscribeScanner, {}", new String(bytes));
        send(bytes);
    }

    public void subscribeMarketData(long requestId, SubscriptionKey subscriptionKey, boolean snapshot){
        byte [] bytes = serializers.marketDataSubscriptionRequestSerializer().serialize(requestId, subscriptionKey, snapshot);
        if (LOG.isDebugEnabled())
            LOG.debug("subscribeMarketData, {}", new String(bytes));
        send(bytes);
    }

    public void unsubscribeMarketData(long requestId) {
        byte [] bytes = serializers.marketDataUnsubscriptionRequestSerializer().serialize(requestId);
        if (LOG.isDebugEnabled())
            LOG.debug("unsubscribeMarketData, {}", new String(bytes));
        send(bytes);
    }

    public void subscribeRealTimeData(long requestId, SubscriptionKey subscriptionKey) {
        byte [] bytes = serializers.realTimeMarketDataSubscriptionRequestSerializer().serialize(requestId, subscriptionKey);
        if (LOG.isDebugEnabled())
            LOG.debug("subscribeRealTimeData, {}", new String(bytes));
        send(bytes);
    }

    public void unsubscribeRealTimeData(long requestId) {
        byte [] bytes = serializers.realTimeMarketDataUnsubscriptionRequestSerializer().serialize(requestId);
        if (LOG.isDebugEnabled())
            LOG.debug("unsubscribeRealTimeData, {}", new String(bytes));
        send(bytes);
    }

    public void subscribeHistoricalData(long requestId, HistoricalSubscriptionKey subscriptionKey) {
        byte [] bytes = serializers.historicalMarketDataSubscriptionRequestSerializer().serialize(requestId, subscriptionKey);
        if (LOG.isDebugEnabled())
            LOG.debug("subscribeHistoricalData, {}", new String(bytes));
        send(bytes);
    }

    public void unsubscribeHistoricalData(long requestId) {
        byte [] bytes = serializers.historicalMarketDataUnsubscriptionRequestSerializer().serialize(requestId);
        if (LOG.isDebugEnabled())
            LOG.debug("unsubscribeHistoricalData, {}", new String(bytes));
        send(bytes);
    }

    public void subscribeFundamentalData(long requestId, ReportType reportType, Instrument instrument) {
        byte [] bytes = serializers.fundamentalDataSubscriptionRequestSerializer().serialize(requestId, reportType, instrument);
        if (LOG.isDebugEnabled())
            LOG.debug("subscribeFundamentalData, {}", new String(bytes));
        send(bytes);
    }

    public void unsubscribeFundamentalData(long requestId) {
        byte [] bytes = serializers.fundamentalDataUnsubscriptionRequestSerializer().serialize(requestId);
        if (LOG.isDebugEnabled())
            LOG.debug("unsubscribeFundamentalData, {}", new String(bytes));
        send(bytes);
    }

    public void subscribeMarketDepth(long requestId, MarketDepthSubscriptionKey subscriptionKey) {
        byte [] bytes = serializers.marketDepthSubscriptionRequestSerializer().serialize(requestId, subscriptionKey);
        if (LOG.isDebugEnabled())
            LOG.debug("subscribeMarketDepth, {}", new String(bytes));
        send(bytes);
    }

    public void unsubscribeMarketDepth(long requestId) {
        byte [] bytes = serializers.marketDepthUnsubscriptionRequestSerializer().serialize(requestId);
        if (LOG.isDebugEnabled())
            LOG.debug("unsubscribeMarketDepth, {}", new String(bytes));
        send(bytes);
    }


    public void subscribeOptionImpliedVolatility(final long requestId, final Instrument instrument, final double optionPrice, final double underlyingPrice) {
        byte [] bytes = serializers.optionImpliedVolatilitySubscriptionRequestSerializer().serialize(requestId, instrument, optionPrice, underlyingPrice);
        if (LOG.isDebugEnabled())
            LOG.debug("subscribeOptionImpliedVolatility, {}", new String(bytes));
        send(bytes);
    }

    public void unsubscribeOptionImpliedVolatility(final long requestId) {
        byte [] bytes = serializers.optionImpliedVolatilityUnsubscriptionRequestSerializer().serialize(requestId);
        if (LOG.isDebugEnabled())
            LOG.debug("unsubscribeOptionImpliedVolatility, {}", new String(bytes));
        send(bytes);
    }


    public void subscribeOptionPrice(final long requestId, final Instrument instrument, final double volatility, final double underlyingPrice) {
        byte [] bytes = serializers.optionPriceSubscriptionRequestSerializer().serialize(requestId, instrument, volatility, underlyingPrice);
        if (LOG.isDebugEnabled())
            LOG.debug("subscribeOptionPrice, {}", new String(bytes));
        send(bytes);
    }

    public void unsubscribeOptionPrice(final long requestId) {
        byte [] bytes = serializers.optionPriceUnsubscriptionRequestSerializer().serialize(requestId);
        if (LOG.isDebugEnabled())
            LOG.debug("unsubscribeOptionPrice, {}", new String(bytes));
        send(bytes);
    }

    public void subscribeAccountSummary(long requestId, String group, String tags) {
        byte [] bytes = serializers.accountSummarySubscriptionRequestSerializer().serialize(requestId, group, tags);
        if (LOG.isDebugEnabled())
            LOG.debug("subscribeAccountSummary, {}", new String(bytes));
        send(bytes);
    }

    public void unsubscribeAccountSummary(long requestId) {
        byte [] bytes = serializers.accountSummaryUnsubscriptionRequestSerializer().serialize(requestId);
        if (LOG.isDebugEnabled())
            LOG.debug("unsubscribeAccountSummary, {}", new String(bytes));
        send(bytes);
    }

    public void subscribeGroupEvents(long requestId, int groupId) {
        byte [] bytes = serializers.groupEventsSubscriptionRequestSerializer().serialize(requestId, groupId);
        if (LOG.isDebugEnabled())
            LOG.debug("subscribeGroupEvents, {}", new String(bytes));
        send(bytes);
    }

    public void unsubscribeGroupEvents(long requestId) {
        byte [] bytes = serializers.groupEventsUnsubscriptionRequestSerializer().serialize(requestId);
        if (LOG.isDebugEnabled())
            LOG.debug("unsubscribeGroupEvents, {}", new String(bytes));
        send(bytes);
    }

    public void requestPositions() {
        byte [] bytes = serializers.positionsRequestSerializer().serialize();
        if (LOG.isDebugEnabled())
            LOG.debug("requestPositions, {}", new String(bytes));
        send(bytes);
    }

    public void cancelPositions() {
        byte [] bytes = serializers.positionsCancellationRequestSerializer().serialize();
        if (LOG.isDebugEnabled())
            LOG.debug("cancelPositions, {}", new String(bytes));
        send(bytes);
    }

    public void exerciseOptions(final long requestId, final Instrument instrument, final ExerciseAction action,
                                final int quantity, final String accountName, final boolean override) {
        byte [] bytes = serializers.exerciseOptionRequestSerializer().serialize(requestId, instrument, action, quantity, accountName, override);
        if (LOG.isDebugEnabled())
            LOG.debug("exerciseOptions, {}", new String(bytes));
        send(bytes);
    }

    public void requestAutoOpenOrders(boolean bindNewlyCreatedOrder){
        byte [] bytes = serializers.bindNewlyCreatedOpenOrderRequestSerializer().serialize(bindNewlyCreatedOrder);
        if (LOG.isDebugEnabled())
            LOG.debug("requestAutoOpenOrders, {}", new String(bytes));
        send(bytes);
    }

    public void requestMarketDataType(MarketDataType type){
        byte [] bytes = serializers.marketDataTypeRequestSerializer().serialize(type);
        if (LOG.isDebugEnabled())
            LOG.debug("requestMarketDataType, {}", new String(bytes));
        send(bytes);
    }

    public void requestScannerParameters(){
        byte [] bytes = serializers.marketScannerValidParametersRequestSerializer().serialize();
        if (LOG.isDebugEnabled())
            LOG.debug("requestScannerParameters, {}", new String(bytes));
        send(bytes);
    }

    public void requestManagedAccounts() {
        byte [] bytes = serializers.managedAccountListRequestSerializer().serialize();
        if (LOG.isDebugEnabled())
            LOG.debug("requestManagedAccounts, {}", new String(bytes));
        send(bytes);
    }

    public void requestAccountUpdates(String accountName) {
        byte [] bytes = serializers.accountUpdateSubscriptionRequestSerializer().serialize(accountName);
        if (LOG.isDebugEnabled())
            LOG.debug("requestAccountUpdate, {}", new String(bytes));
        send(bytes);
    }

    public void requestExecutions(long requestId, ExecutionReportFilter filter) {
        byte [] bytes = serializers.executionReportRequestSerializer().serialize(requestId, filter);
        if (LOG.isDebugEnabled())
            LOG.debug("requestExecutions, {}", new String(bytes));
        send(bytes);
    }

    public void requestAllOpenOrders() {
        byte [] bytes = serializers.retrieveAllOpenOrderRequestSerializer().serialize();
        if (LOG.isDebugEnabled())
            LOG.debug("requestOpenOrders, {}", new String(bytes));
        send(bytes);
    }

    public void requestOpenOrders() {
        byte [] bytes = serializers.retrieveOpenOrderRequestSerializer().serialize();
        if (LOG.isDebugEnabled())
            LOG.debug("requestOpenOrders, {}", new String(bytes));
        send(bytes);
    }

    public void requestContractSpecification(long requestId, Instrument instrument) {
        byte [] bytes = serializers.contractSpecificationRequestSerializer().serialize(requestId, instrument);
        if (LOG.isDebugEnabled())
            LOG.debug("requestContractSpecification, {}", new String(bytes));
        send(bytes);
    }

    public void requestNextValidOrderId(){
        byte [] bytes = serializers.nextValidOrderIdRequestSerializer().serialize();
        if (LOG.isDebugEnabled())
            LOG.debug("requestNextValidOrderId, {}", new String(bytes));
        send(bytes);
    }

    public void requestNextValidOrderId(long suggestId){
        byte [] bytes = serializers.nextValidOrderIdRequestSerializer().serialize(suggestId);
        if (LOG.isDebugEnabled())
            LOG.debug("requestNextValidOrderId, {}", new String(bytes));
        send(bytes);
    }

    public void requestFA(FinancialAdvisorDataType dataType){
        byte [] bytes = serializers.faConfigurationRequestSerializer().serialize(dataType);
        if (LOG.isDebugEnabled())
            LOG.debug("requestFA, {}", new String(bytes));
        send(bytes);
    }

    public void requestCurrentTime(){
        byte [] bytes = serializers.serverCurrentTimeRequestSerializer().serialize();
        if (LOG.isDebugEnabled())
            LOG.debug("requestCurrentTime, {}", new String(bytes));
        send(bytes);
    }

    public void replaceFA(FinancialAdvisorDataType dataType, String xml){
        byte [] bytes = serializers.faReplaceConfigurationRequestSerializer().serialize(dataType, xml);
        if (LOG.isDebugEnabled())
            LOG.debug("replaceFA, {}", new String(bytes));
        send(bytes);
    }

    public void placeOrder(Order order) {
        byte[] bytes = serializers.placeOrderSerializer().serialize(order);
        if (LOG.isDebugEnabled())
            LOG.debug("placeOrder, {}", new String(bytes));
        send(bytes);
    }

    public void cancelOrder(long orderId) {
        byte[] bytes = serializers.cancelOrderSerializer().serialize(orderId);
        if (LOG.isDebugEnabled())
            LOG.debug("cancelOrder, {}", new String(bytes));
        send(bytes);
    }
    public void cancelAllOrders() {
        byte[] bytes = serializers.cancelAllOrdersRequestSerializer().serialize();
        if (LOG.isDebugEnabled())
            LOG.debug("cancelAllOrders, {}", new String(bytes));
        send(bytes);
    }

    public void setServerLogLevel(LogLevel logLevel) {
        byte[] bytes = serializers.serverLogLevelRequestSerializer().serialize(logLevel);
        if (LOG.isDebugEnabled())
            LOG.debug("setServerLogLevel, {}", new String(bytes));
        send(bytes);
    }

    public void verifyRequest(String apiName, String apiVersion) {
        byte [] bytes = serializers.verifyRequestSerializer().serialize(apiName, apiVersion);
        if (LOG.isDebugEnabled())
            LOG.debug("verifyRequest, {}", new String(bytes));
        send(bytes);
    }

    public void verifyMessage(String apiData) {
        byte [] bytes = serializers.verifyMessageRequestSerializer().serialize(apiData);
        if (LOG.isDebugEnabled())
            LOG.debug("verifyMessage, {}", new String(bytes));
        send(bytes);
    }

    public void queryDisplayGroups(long requestId) {
        byte [] bytes = serializers.displayGroupsQueryRequestSerializer().serialize(requestId);
        if (LOG.isDebugEnabled())
            LOG.debug("queryDisplayGroups, {}", new String(bytes));
        send(bytes);
    }

    public void updateDisplayGroups(long requestId, String contractInfo) {
        byte [] bytes = serializers.displayGroupUpdateRequestSerializer().serialize(requestId, contractInfo);
        if (LOG.isDebugEnabled())
            LOG.debug("updateDisplayGroups, {}", new String(bytes));
        send(bytes);
    }

    private void send(final byte[] bytes){
        lock.lock();
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            LOG.error(e);
        } finally {
            lock.unlock();
        }
    }

    public void disconnect(){
        inputStreamConsumer.stop();
        IOUtils.closeQuietly(outputStream);
        IOUtils.closeQuietly(socket);
    }

    public boolean isConnected(){
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
}
