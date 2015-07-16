package com.unisoft.algotrader.utils.networking.aeron;

import com.unisoft.algotrader.utils.networking.Publisher;
import uk.co.real_logic.aeron.Aeron;
import uk.co.real_logic.aeron.Publication;
import uk.co.real_logic.aeron.driver.MediaDriver;
import uk.co.real_logic.agrona.CloseHelper;
import uk.co.real_logic.agrona.concurrent.IdleStrategy;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by alex on 4/19/15.
 */
public class AeronPublisher implements Publisher {

    private final int streamId;
    private final String channel;
    private final int messageLength;
    private final long lingerTimeoutMs;
    private final MediaDriver.Context mediaDriverContext;

    private final UnsafeBuffer unsafeBuffer;
    private final IdleStrategy idleStrategy;
    private final boolean embeddedMediaDriver;

    private MediaDriver driver;
    private ExecutorService executor;
    private Aeron aeron;
    private Publication publication;
    private AtomicBoolean connected = new AtomicBoolean(false);


    public AeronPublisher(AeronConfig config){
        this.streamId = config.streamId;
        this.channel = config.channel;
        this.messageLength = config.messageLength;
        this.lingerTimeoutMs = config.lingerTimeoutMs;
        this.embeddedMediaDriver = config.embeddedMediaDriver;
        this.mediaDriverContext = config.mediaDriverContext;
        this.unsafeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(messageLength));
        this.idleStrategy = config.idleStrategy;
    }

    @Override
    public void connect() {
        driver = embeddedMediaDriver ? MediaDriver.launchEmbedded(mediaDriverContext) : null;
        executor = Executors.newFixedThreadPool(2);
        Aeron.Context context = new Aeron.Context();
        aeron = Aeron.connect(context);
        publication = aeron.addPublication(channel, streamId);
        connected.set(true);
    }

    @Override
    public void disconnect() {
        CloseHelper.quietClose(aeron);
        CloseHelper.quietClose(publication);
        CloseHelper.quietClose(driver);
        executor.shutdown();
        connected.set(false);
    }

    @Override
    public boolean connected() {
        return connected.get();
    }

    @Override
    public boolean publish(byte[] bytes) {
        unsafeBuffer.putBytes(0, bytes);
        while (publication.offer(unsafeBuffer, 0, bytes.length) < 0L)
        {
            idleStrategy.idle(0);
        }
        return true;
    }


    @Override
    public boolean publish(ByteBuffer byteBuffer, int length) {
        unsafeBuffer.putBytes(0, byteBuffer, length);
        while (publication.offer(unsafeBuffer, 0, length) < 0L)
        {
            idleStrategy.idle(0);
        }
        return true;
    }
}
