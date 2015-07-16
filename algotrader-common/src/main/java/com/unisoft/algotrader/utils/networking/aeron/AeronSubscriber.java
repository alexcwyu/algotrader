package com.unisoft.algotrader.utils.networking.aeron;

import com.google.common.collect.Lists;
import com.unisoft.algotrader.utils.networking.DataHandler;
import com.unisoft.algotrader.utils.networking.Subscriber;
import uk.co.real_logic.aeron.Aeron;
import uk.co.real_logic.aeron.Subscription;
import uk.co.real_logic.aeron.driver.MediaDriver;
import uk.co.real_logic.aeron.logbuffer.Header;
import uk.co.real_logic.aeron.samples.SampleConfiguration;
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
public class AeronSubscriber implements Subscriber {

    private final int streamId;
    private final String channel;
    private final int fragmentCountLimit;
    private final boolean embeddedMediaDriver;
   // private final MediaDriver.Context mediaDriverContext;
    private static final int FRAGMENT_COUNT_LIMIT = SampleConfiguration.FRAGMENT_COUNT_LIMIT;
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
        //this.mediaDriverContext = config.mediaDriverContext;
    }

    @Override
    public void connect() {
        executor = Executors.newFixedThreadPool(1);

        driver = embeddedMediaDriver ? MediaDriver.launchEmbedded() : null;
        Aeron.Context context = new Aeron.Context()
                .newImageHandler(SamplesUtil::printNewImage)
                .inactiveImageHandler(SamplesUtil::printInactiveImage);
        if (embeddedMediaDriver)
        {
            context.dirName(driver.contextDirName());
        }

        aeron = Aeron.connect(context);
        subscription = aeron.addSubscription(channel, streamId);
        connected.set(true);

        final Future future = executor.submit(
                () -> SamplesUtil.subscriberLoop(this::onData, FRAGMENT_COUNT_LIMIT, connected).accept(subscription));
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
