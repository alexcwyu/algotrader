package com.unisoft.algotrader.provider.ib.api;

import ch.aonyx.broker.ib.api.contract.Contract;
import com.unisoft.algotrader.model.event.EventBusManager;
import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.execution.ExecutionReport;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.persistence.SampleInMemoryRefDataStore;
import com.unisoft.algotrader.provider.data.SubscriptionKey;
import com.unisoft.algotrader.provider.ib.IBConfig;
import com.unisoft.algotrader.provider.ib.api.model.CommissionReport;
import com.unisoft.algotrader.provider.ib.api.model.InstrumentSpecification;
import com.unisoft.algotrader.provider.ib.api.model.MarketScannerData;
import com.unisoft.algotrader.provider.ib.api.serializer.*;
import com.unisoft.algotrader.utils.threading.NamedThreadFactory;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by alex on 8/1/15.
 */
public class IBSession {

    private static final Logger LOG = LogManager.getLogger(IBSession.class);
    private final IBConfig ibConfig;
    private final RefDataStore refDataStore;

    private final EventBusManager eventBusManager;
    private int serverCurrentVersion;

    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private EventInputStreamConsumer inputStreamConsumer;


    private final ExecutorService executor = Executors.newFixedThreadPool(1, new NamedThreadFactory("IBConnection"));


    private Lock lock = new ReentrantLock();
    private AtomicInteger orderId = new AtomicInteger(0);
    private AtomicInteger requestId = new AtomicInteger(0);

    private RealTimeMarketDataSerializer realTimeMarketDataRequestSerializer;
    private PlaceOrderSerializer placeOrderRequestSerializer;
    private CancelOrderSerializer cancelOrderRequestSerializer;
    private HistoricalMarketDataSerializer historicalMarketDataRequestSerializer;

    public IBSession(IBConfig ibConfig, RefDataStore refDataStore, EventBusManager eventBusManager){
        this.ibConfig = ibConfig;
        this.refDataStore = refDataStore;
        this.eventBusManager = eventBusManager;
    }

    public IBConfig getIbConfig() {
        return ibConfig;
    }

    public RefDataStore getRefDataStore() {
        return refDataStore;
    }

    public EventBusManager getEventBusManager() {
        return eventBusManager;
    }

    public int getServerCurrentVersion() {
        return serverCurrentVersion;
    }
    public InputStream getInputStream() {
        return inputStream;
    }

    public void connect(){
        try {
            this.socket = new Socket(ibConfig.host, ibConfig.port);
            this.outputStream = new DataOutputStream(socket.getOutputStream());
            this.inputStream = new DataInputStream(socket.getInputStream());

            handShake();
            initSerializer();
            initInputStreamConsumer();
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

            if (serverCurrentVersion < ibConfig.minVersion) {
                final String detailedMessage = "Minimum server version required '" + ibConfig.minVersion
                        + "', current server version '" + serverCurrentVersion + "'";
                throw new Exception(detailedMessage);
            } else {
                //set client id
                builder.append(ibConfig.clientId);
                send(builder.toBytes());
                builder.clear();
            }
        } catch (final Exception e) {
            LOG.error(e);
            disconnect();
        }
    }

    private void initSerializer(){
        placeOrderRequestSerializer = new PlaceOrderSerializer(orderId, refDataStore, serverCurrentVersion);
        cancelOrderRequestSerializer = new CancelOrderSerializer(serverCurrentVersion);
        realTimeMarketDataRequestSerializer = new RealTimeMarketDataSerializer(requestId, refDataStore, serverCurrentVersion);
        historicalMarketDataRequestSerializer = new HistoricalMarketDataSerializer(orderId, refDataStore, serverCurrentVersion);
    }

    private void initInputStreamConsumer(){
        inputStreamConsumer = new EventInputStreamConsumer(this);
        executor.submit(this.inputStreamConsumer);
    }

    public void subscribeRealTimeData(SubscriptionKey subscriptionKey) {
        try {
            send(realTimeMarketDataRequestSerializer.serialize(subscriptionKey));
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public void subscribeHistoricalData(SubscriptionKey subscriptionKey) {
        try {
            send(historicalMarketDataRequestSerializer.serialize(subscriptionKey));
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public void requestAccountUpdate(){
        //TODO
    }



    public void sendOrder(Order order){
        try {
            send(placeOrderRequestSerializer.serialize(order));
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public void cancelOrder(long orderId){
        try {
            send(cancelOrderRequestSerializer.serialize(orderId));
        } catch (IOException e) {
            LOG.error(e);
        }
    }


    public void onTickPrice(final int requestId, final int tickType, final double price, final int autoExecute){

    }

    public void onTickSize(final int requestId, final int tickType, final int size){

    }

    public void onTickGenericEvent(final int requestId, final IBConstants.TickType tickType, final double value){

    }

    public void onTickStringEvent(final int requestId, final IBConstants.TickType tickType, final String value){

    }

    public void onServerCurrentTime(final long timestamp){

    }

    public void onRealTimeBarEvent(final int requestId, final long timestamp, final double open,
                                   final double high, final double low, final double close, final long volume,
                                   final double weightedAveragePrice, final int tradeNumber){

    }

    public void onFundamentalDataEvent(final int requestId, final String xml){

    }

    public void onRetrieveOpenOrderEndEvent(){

    }

    public void onContractSpecificationEndEvent(final int requestId){

    }

    public void onAccountUpdateValueEndEvent(final String accountName){
    }

    public void onExecutionReportEndEvent(final int requestId){
    }


    public void onDeltaNeutralValidationEvent(final int requestId,
        final int instId,
        final double delta,
        final double price){
    }


    public void onTickSnapshotEndEvent(final int requestId){

    }

    public void onMarketDataTypeEvent(final int requestId, IBConstants.MarketDataType marketDataType){

    }

    public void onCommissionReport(final CommissionReport commissionReport){

    }

    public void onPosition(){
        //TODO
    }
    public void onPositionEnd(){

    }

    public void onAccountSummary(int value, String errorText, String open, String completedIndicator, String high){

    }

    public void onAccountSummaryEnd(int value){

    }


    public void onVerifyMessageAPI(String message){

    }


    public void onVerifyCompleted(boolean bool, String errorText){

    }
    public void onDisplayGroupList(final int requestId, final String contractInfo){

    }

    public void onDisplayGroupUpdated(final int requestId, final String contractInfo){

    }


    public void onTickEfpEvent(final int requestId, final IBConstants.TickType tickType, final double basisPoints,
                               final String formattedBasisPoints, final double impliedFuturePrice, final int holdDays,
                               final String futureExpiry, final double dividendImpact, final double dividendToExpiry){

    }

    public void onOrderStatus(final int orderId, final String orderStatus, final int filledQuantity,
                              final int remainingQuantity, final double averageFilledPrice, final int permanentId,
                              final int parentOrderId, final double lastFilledPrice, final int clientId, final String heldCause){

    }

    public void onMarketDepthUpdate(final int requestId, final int rowId, final int operation,
                                    final int bookSide, final double price, final int size){

    }

    public void onTickOptionComputationEvent(final int requestId, final int tickType,
                   final double impliedVolatility, final double delta, final double price, final double presentValueDividend,
                   final double gamma, final double vega, final double theta, final double underlyingPrice){

    }

    public void onMarketDepthL2Update(final int requestId, final int rowId,
                                      final String marketMakerName, final int operation, final int bookSide, final double price, final int size){

    }
    public void onNewsBulletinUpdate(final int newsBulletinId, final int newBulletinTypeValue,
                                     final String message, final String exchange){

    }

    public void onMarketScannerValidParameters(final String xml){

    }


    public void onMarketScannerData(int requestId, List<MarketScannerData> marketScannerData){

    }

    public void onManagedAccountList(String commaSeparatedAccountList){

    }

    public void onFinancialAdvisorConfiguration(int dataType, String xml){

    }

    public void onhistoricalDataEvents(int requestId, List<Bar> historicalDataEvents){

    }

    public void onMessage(final int requestId, final int code, final String message){

    }

    public void onUpdateAccountValue(final String key, final String value, final String currency,
                                     final String accountName){

    }

    public void onUpdateAccountTime(final String time){

    }

    public void onOpenOrder(Order order){

    }

    public void onExecutionReport(int requestId, ExecutionReport executionReport){

    }

    public void onPortfolioUpdateEvent(final Instrument instrument, final int position, final double marketPrice,
                                       final double marketValue, final double averageCost, final double unrealizedProfitAndLoss,
                                       final double realizedProfitAndLoss, final String accountName){

    }

    public void onNextValidOrderId(final int nextValidOrderId){

    }

    public void onInstrumentSpecification(final int requestId, final InstrumentSpecification instrumentSpecification){

    }

    private void send(final byte[] bytes) throws IOException {
        lock.lock();
        try {
            outputStream.write(bytes);
        }
        finally {
            lock.unlock();
        }
    }

    public void disconnect(){
        IOUtils.closeQuietly(outputStream);
        IOUtils.closeQuietly(socket);
    }

    public boolean isConnected(){
        return socket != null && socket.isConnected();
    }


    public static void main(String ... args)throws Exception{
        IBConfig ibConfig = new IBConfig("localhost", 7496, 1, null, null, 59, 38);
        IBSession session = new IBSession(ibConfig, new SampleInMemoryRefDataStore(), null);
        session.connect();

        Thread.sleep(1000);
    }
}
