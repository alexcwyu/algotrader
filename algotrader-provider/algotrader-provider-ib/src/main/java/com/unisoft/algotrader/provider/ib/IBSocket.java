package com.unisoft.algotrader.provider.ib;

import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.data.SubscriptionKey;
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

    private final IBProvider ibProvider;
    private final ExecutorService executor = Executors.newFixedThreadPool(1, new NamedThreadFactory("IBConnection"));

    private int serverCurrentVersion;
    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private EventInputStreamConsumer inputStreamConsumer;

    private Lock lock = new ReentrantLock();

    private AccountUpdateSubscriptionRequestSerializer accountUpdateSubscriptionRequestSerializer;
    private RealTimeMarketDataSubscriptionRequestSerializer realTimeMarketDataRequestSerializer;
    private RealTimeMarketDataUnsubscriptionRequestSerializer realTimeMarketDataUnsubscriptionRequestSerializer;
    private PlaceOrderSerializer placeOrderRequestSerializer;
    private CancelOrderSerializer cancelOrderRequestSerializer;
    private HistoricalMarketDataSubscriptionRequestSerializer historicalMarketDataRequestSerializer;
    private HistoricalMarketDataUnsubscriptionRequestSerializer historicalMarketDataUnsubscriptionRequestSerializer;

    public IBSocket(IBProvider ibProvider){
        this.ibProvider = ibProvider;
    }

    public int getServerCurrentVersion() {
        return serverCurrentVersion;
    }
    public InputStream getInputStream() {
        return inputStream;
    }

    public void connect(){
        try {
            IBConfig ibConfig = ibProvider.getConfig();
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
            IBConfig ibConfig = ibProvider.getConfig();
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
        RefDataStore refDataStore = ibProvider.getRefDataStore();
        accountUpdateSubscriptionRequestSerializer = new AccountUpdateSubscriptionRequestSerializer(serverCurrentVersion);
        placeOrderRequestSerializer = new PlaceOrderSerializer(refDataStore, serverCurrentVersion);
        cancelOrderRequestSerializer = new CancelOrderSerializer(serverCurrentVersion);
        realTimeMarketDataRequestSerializer = new RealTimeMarketDataSubscriptionRequestSerializer(refDataStore, serverCurrentVersion);
        realTimeMarketDataUnsubscriptionRequestSerializer = new RealTimeMarketDataUnsubscriptionRequestSerializer(serverCurrentVersion);
        historicalMarketDataRequestSerializer = new HistoricalMarketDataSubscriptionRequestSerializer(refDataStore, serverCurrentVersion);
        historicalMarketDataUnsubscriptionRequestSerializer = new HistoricalMarketDataUnsubscriptionRequestSerializer(serverCurrentVersion);
    }

    private void initInputStreamConsumer(){
        inputStreamConsumer = new EventInputStreamConsumer(ibProvider, this);
        executor.submit(this.inputStreamConsumer);
    }

    public void subscribeRealTimeData(SubscriptionKey subscriptionKey) {
        try {
            send(realTimeMarketDataRequestSerializer.serialize(subscriptionKey));
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public void unsubscribeRealTimeData(SubscriptionKey subscriptionKey) {
        try {
            send(realTimeMarketDataUnsubscriptionRequestSerializer.serialize(subscriptionKey.getSubscriptionId()));
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

    public void unsubscribeHistoricalData(SubscriptionKey subscriptionKey) {
        try {
            send(historicalMarketDataUnsubscriptionRequestSerializer.serialize(subscriptionKey.getSubscriptionId()));
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public void requestAccountUpdate(String accountName) {
        try {
            send(accountUpdateSubscriptionRequestSerializer.serialize(accountName));
        } catch (IOException e) {
            LOG.error(e);
        }
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
}
