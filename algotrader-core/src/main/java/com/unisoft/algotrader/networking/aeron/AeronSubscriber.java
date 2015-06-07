package com.unisoft.algotrader.networking.aeron;

import com.google.common.collect.Lists;
import com.unisoft.algotrader.networking.DataHandler;
import com.unisoft.algotrader.networking.Subscriber;
import uk.co.real_logic.aeron.Aeron;
import uk.co.real_logic.aeron.FragmentAssemblyAdapter;
import uk.co.real_logic.aeron.Subscription;
import uk.co.real_logic.aeron.common.concurrent.logbuffer.Header;
import uk.co.real_logic.aeron.driver.MediaDriver;
import uk.co.real_logic.aeron.samples.SamplesUtil;
import uk.co.real_logic.agrona.CloseHelper;
import uk.co.real_logic.agrona.DirectBuffer;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by alex on 4/19/15.
 */
public class AeronSubscriber implements uk.co.real_logic.aeron.common.concurrent.logbuffer.DataHandler, Subscriber {

    private final int streamId;
    private final String channel;
    private final int fragmentCountLimit;
    private final boolean embeddedMediaDriver;
    private final MediaDriver.Context mediaDriverContext;
    private final List<DataHandler> dataHandlers = Lists.newArrayList();

    private MediaDriver driver;
    private ExecutorService executor;
    private Aeron aeron;
    private Subscription subscription;
    private AtomicBoolean connected = new AtomicBoolean(false);

    public AeronSubscriber(AeronConfig config){
        this.streamId = config.streamId;
        this.channel = config.channel;
        this.fragmentCountLimit = config.fragmentCountLimit;
        this.embeddedMediaDriver = config.embeddedMediaDriver;
        this.mediaDriverContext = config.mediaDriverContext;
    }

    @Override
    public void connect() {
        driver = embeddedMediaDriver ? MediaDriver.launchEmbedded(mediaDriverContext) : null;
        executor = Executors.newFixedThreadPool(3);
        Aeron.Context context = new Aeron.Context()
                .newConnectionHandler(SamplesUtil::printNewConnection)
                .inactiveConnectionHandler(SamplesUtil::printInactiveConnection);
        aeron = Aeron.connect(context, executor);
        subscription = aeron.addSubscription(channel, streamId, new FragmentAssemblyAdapter(this));
        connected.set(true);

        final Future future = executor.submit(
                () -> SamplesUtil.subscriberLoop(fragmentCountLimit, connected).accept(subscription));
    }

    @Override
    public void disconnect() {
        CloseHelper.quietClose(aeron);
        CloseHelper.quietClose(subscription);
        CloseHelper.quietClose(driver);
        executor.shutdown();
        connected.set(false);
    }

    @Override
    public boolean connected() {
        return connected.get();
    }

    @Override
    public void onData(DirectBuffer buffer, int offset, int length, Header header) {
        final byte[] data = new byte[length];
        buffer.getBytes(offset, data);

        for (DataHandler dataHandler : dataHandlers){
            dataHandler.onData(data);
        }
    }

    @Override
    public void subscribe(DataHandler handler) {
        dataHandlers.add(handler);
    }
}
