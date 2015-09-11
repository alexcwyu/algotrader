package com.unisoft.algotrader.provider.ib;

import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.data.HistoricalSubscriptionKey;
import com.unisoft.algotrader.provider.data.SubscriptionKey;
import com.unisoft.algotrader.provider.ib.api.deserializer.Deserializers;
import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.model.fa.FinancialAdvisorDataType;
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
        try {
            this.socket = new Socket(ibConfig.host, ibConfig.port);
            this.outputStream = new DataOutputStream(socket.getOutputStream());
            this.inputStream = new DataInputStream(socket.getInputStream());

            handShake();
            startStream();
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
            initIOSerializers();

            if (serverCurrentVersion < ibConfig.minVersion) {
                final String detailedMessage = "Minimum server version required '" + ibConfig.minVersion
                        + "', current server version '" + serverCurrentVersion + "'";
                throw new Exception(detailedMessage);
            } else {
                //set client id
                send(serializers.startAPISerializer().serialize(ibConfig.clientId));
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


    public void subscribeRealTimeData(SubscriptionKey subscriptionKey) {
        try {
            byte [] bytes = serializers.realTimeMarketDataSubscriptionRequestSerializer().serialize(subscriptionKey);
            LOG.debug("subscribeRealTimeData, {}", new String(bytes));
            send(bytes);
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public void unsubscribeRealTimeData(SubscriptionKey subscriptionKey) {
        try {
            byte [] bytes = serializers.realTimeMarketDataUnsubscriptionRequestSerializer().serialize(subscriptionKey.getSubscriptionId());
            LOG.debug("unsubscribeRealTimeData, {}", new String(bytes));
            send(bytes);
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public void subscribeHistoricalData(HistoricalSubscriptionKey subscriptionKey) {
        try {

            byte [] bytes = serializers.historicalMarketDataSubscriptionRequestSerializer().serialize(subscriptionKey);
            LOG.debug("subscribeHistoricalData, {}", new String(bytes));
            send(bytes);
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public void unsubscribeHistoricalData(long subscriptionId) {
        try {

            byte [] bytes = serializers.historicalMarketDataUnsubscriptionRequestSerializer().serialize(subscriptionId);
            LOG.debug("unsubscribeHistoricalData, {}", new String(bytes));
            send(bytes);
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public void requestAccountUpdate(String accountName) {
        try {
            byte [] bytes = serializers.accountUpdateSubscriptionRequestSerializer().serialize(accountName);
            LOG.debug("requestAccountUpdate, {}", new String(bytes));
            send(bytes);
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public void requestFA(FinancialAdvisorDataType dataType){
        try {
            byte [] bytes = serializers.faConfigurationRequestSerializer().serialize(dataType);
            LOG.debug("requestFA, {}", new String(bytes));
            send(bytes);
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public void sendOrder(Order order){
        try {
            byte [] bytes = serializers.placeOrderSerializer().serialize(order);
            LOG.debug("sendOrder, {}", new String(bytes));
            send(bytes);
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public void cancelOrder(long orderId){
        try {
            byte [] bytes = serializers.cancelOrderSerializer().serialize(orderId);
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
