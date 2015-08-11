package com.unisoft.algotrader.provider.ib.api;

import com.unisoft.algotrader.model.event.EventBusManager;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.persistence.SampleInMemoryRefDataStore;
import com.unisoft.algotrader.provider.data.SubscriptionKey;
import com.unisoft.algotrader.provider.ib.IBConfig;
import com.unisoft.algotrader.provider.ib.api.deserializer.DeserializerFactory;
import com.unisoft.algotrader.provider.ib.api.serializer.*;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

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

    public Socket getSocket() {
        return socket;
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



            this.inputStreamConsumer = new EventInputStreamConsumer(this);
            initSerializer();

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
        outputStream.write(bytes);
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
