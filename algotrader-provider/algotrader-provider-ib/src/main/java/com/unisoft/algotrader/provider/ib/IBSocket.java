package com.unisoft.algotrader.provider.ib;

import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.data.HistoricalSubscriptionKey;
import com.unisoft.algotrader.provider.data.SubscriptionKey;
import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.model.fa.FinancialAdvisorDataType;
import com.unisoft.algotrader.provider.ib.api.serializer.*;
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

    private Lock lock = new ReentrantLock();

    private StartAPISerializer startAPISerializer;
    private AccountUpdateSubscriptionRequestSerializer accountUpdateSubscriptionRequestSerializer;
    private FAConfigurationRequestSerializer faConfigurationRequestSerializer;
    private RealTimeMarketDataSubscriptionRequestSerializer realTimeMarketDataRequestSerializer;
    private RealTimeMarketDataUnsubscriptionRequestSerializer realTimeMarketDataUnsubscriptionRequestSerializer;
    private PlaceOrderSerializer placeOrderRequestSerializer;
    private CancelOrderSerializer cancelOrderRequestSerializer;
    private HistoricalMarketDataSubscriptionRequestSerializer historicalMarketDataRequestSerializer;
    private HistoricalMarketDataUnsubscriptionRequestSerializer historicalMarketDataUnsubscriptionRequestSerializer;

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
        try {
            this.socket = new Socket(ibConfig.host, ibConfig.port);
            this.outputStream = new DataOutputStream(socket.getOutputStream());
            this.inputStream = new DataInputStream(socket.getInputStream());

            handShake();
            initInputStreamConsumer();
            //requestAccountUpdate(null);
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
            initSerializer();

            if (serverCurrentVersion < ibConfig.minVersion) {
                final String detailedMessage = "Minimum server version required '" + ibConfig.minVersion
                        + "', current server version '" + serverCurrentVersion + "'";
                throw new Exception(detailedMessage);
            } else {
                //set client id
                if (serverCurrentVersion < 70) {
                    builder.append(ibConfig.clientId);
                    send(builder.toBytes());
                    builder.clear();
                }
                else{
                    send(startAPISerializer.serialize(ibConfig.clientId));
                }
            }
        } catch (final Exception e) {
            LOG.error(e);
            disconnect();
        }
    }

    private void initSerializer(){
        startAPISerializer = new StartAPISerializer(serverCurrentVersion);
        accountUpdateSubscriptionRequestSerializer = new AccountUpdateSubscriptionRequestSerializer(serverCurrentVersion);
        faConfigurationRequestSerializer = new FAConfigurationRequestSerializer(serverCurrentVersion);
        placeOrderRequestSerializer = new PlaceOrderSerializer(refDataStore, serverCurrentVersion);
        cancelOrderRequestSerializer = new CancelOrderSerializer(serverCurrentVersion);
        realTimeMarketDataRequestSerializer = new RealTimeMarketDataSubscriptionRequestSerializer(refDataStore, serverCurrentVersion);
        realTimeMarketDataUnsubscriptionRequestSerializer = new RealTimeMarketDataUnsubscriptionRequestSerializer(serverCurrentVersion);
        historicalMarketDataRequestSerializer = new HistoricalMarketDataSubscriptionRequestSerializer(refDataStore, serverCurrentVersion);
        historicalMarketDataUnsubscriptionRequestSerializer = new HistoricalMarketDataUnsubscriptionRequestSerializer(serverCurrentVersion);
    }

    private void initInputStreamConsumer(){
        inputStreamConsumer = new EventInputStreamConsumer(eventHandler, this, serverCurrentVersion, refDataStore);
        executor.submit(this.inputStreamConsumer);
    }

    public void subscribeRealTimeData(SubscriptionKey subscriptionKey) {
        try {
            byte [] bytes = realTimeMarketDataRequestSerializer.serialize(subscriptionKey);
            LOG.debug("subscribeRealTimeData, {}", new String(bytes));
            send(bytes);
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public void unsubscribeRealTimeData(SubscriptionKey subscriptionKey) {
        try {
            byte [] bytes = realTimeMarketDataUnsubscriptionRequestSerializer.serialize(subscriptionKey.getSubscriptionId());
            LOG.debug("unsubscribeRealTimeData, {}", new String(bytes));
            send(bytes);
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public void subscribeHistoricalData(HistoricalSubscriptionKey subscriptionKey) {
        try {

            byte [] bytes = historicalMarketDataRequestSerializer.serialize(subscriptionKey);
            LOG.debug("subscribeHistoricalData, {}", new String(bytes));
            send(bytes);
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public void unsubscribeHistoricalData(long subscriptionId) {
        try {

            byte [] bytes = historicalMarketDataUnsubscriptionRequestSerializer.serialize(subscriptionId);
            LOG.debug("unsubscribeHistoricalData, {}", new String(bytes));
            send(bytes);
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public void requestAccountUpdate(String accountName) {
        try {
            byte [] bytes = accountUpdateSubscriptionRequestSerializer.serialize(accountName);
            LOG.debug("requestAccountUpdate, {}", new String(bytes));
            send(bytes);
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public void requestFA(FinancialAdvisorDataType dataType){
        try {
            byte [] bytes = faConfigurationRequestSerializer.serialize(dataType);
            LOG.debug("requestFA, {}", new String(bytes));
            send(bytes);
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public void sendOrder(Order order){
        try {
            byte [] bytes = placeOrderRequestSerializer.serialize(order);
            LOG.debug("sendOrder, {}", new String(bytes));
            send(bytes);
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public void cancelOrder(long orderId){
        try {
            byte [] bytes = cancelOrderRequestSerializer.serialize(orderId);
            LOG.debug("cancelOrder, {}", new String(bytes));
            send(bytes);
        } catch (IOException e) {
            LOG.error(e);
        }
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
        inputStreamConsumer.stop();
        IOUtils.closeQuietly(outputStream);
        IOUtils.closeQuietly(socket);
    }

    public boolean isConnected(){
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
}
