package com.unisoft.algotrader.networking.aeron;

import uk.co.real_logic.aeron.driver.MediaDriver;
import uk.co.real_logic.aeron.driver.ThreadingMode;
import uk.co.real_logic.aeron.samples.SampleConfiguration;
import uk.co.real_logic.agrona.concurrent.BackoffIdleStrategy;
import uk.co.real_logic.agrona.concurrent.BusySpinIdleStrategy;
import uk.co.real_logic.agrona.concurrent.IdleStrategy;
import uk.co.real_logic.agrona.concurrent.NoOpIdleStrategy;

/**
 * Created by alex on 4/19/15.
 */
public class AeronConfig {

    public static final IdleStrategy DEFAULT_IDLE_STRATEGY = new BusySpinIdleStrategy();
    public static final MediaDriver.Context LOW_LATENCY_MEDIA_DRIVER = new MediaDriver.Context()
            .threadingMode(ThreadingMode.DEDICATED)
            .conductorIdleStrategy(new BackoffIdleStrategy(1, 1, 1, 1))
            .sharedNetworkIdleStrategy(new NoOpIdleStrategy())
            .sharedIdleStrategy(new NoOpIdleStrategy())
            .receiverIdleStrategy(new NoOpIdleStrategy())
            .senderIdleStrategy(new NoOpIdleStrategy());

    public final int streamId;
    public final String channel;
    public final int messageLength;
    public final long lingerTimeoutMs;
    public final int fragmentCountLimit;
    public final boolean embeddedMediaDriver;
    public final IdleStrategy idleStrategy;
    public final MediaDriver.Context mediaDriverContext;

    public AeronConfig(AeronConfigBuilder configBuilder) {
        this.streamId = configBuilder.streamId;
        this.channel = configBuilder.channel;
        this.messageLength = configBuilder.messageLength;
        this.lingerTimeoutMs = configBuilder.lingerTimeoutMs;
        this.fragmentCountLimit = configBuilder.fragmentCountLimit;
        this.embeddedMediaDriver = configBuilder.embeddedMediaDriver;
        this.idleStrategy = configBuilder.idleStrategy;
        this.mediaDriverContext = configBuilder.mediaDriverContext;
    }

    public static class AeronConfigBuilder {
        private int streamId = SampleConfiguration.STREAM_ID;
        private String channel = SampleConfiguration.CHANNEL;
        private int messageLength = SampleConfiguration.MESSAGE_LENGTH;
        private long lingerTimeoutMs = SampleConfiguration.LINGER_TIMEOUT_MS;
        private int fragmentCountLimit = SampleConfiguration.FRAGMENT_COUNT_LIMIT;
        private boolean embeddedMediaDriver = true;
        private IdleStrategy idleStrategy = DEFAULT_IDLE_STRATEGY;
        private MediaDriver.Context mediaDriverContext = LOW_LATENCY_MEDIA_DRIVER;

        public AeronConfigBuilder streamId(int streamId) {
            this.streamId = streamId;
            return this;
        }

        public AeronConfigBuilder channel(String channel) {
            this.channel = channel;
            return this;
        }

        public AeronConfigBuilder messageLength(int messageLength) {
            this.messageLength = messageLength;
            return this;
        }

        public AeronConfigBuilder lingerTimeoutMs(long lingerTimeoutMs) {
            this.lingerTimeoutMs = lingerTimeoutMs;
            return this;
        }

        public AeronConfigBuilder fragmentCountLimit(int fragmentCountLimit) {
            this.fragmentCountLimit = fragmentCountLimit;
            return this;
        }

        public AeronConfigBuilder embeddedMediaDriver(boolean embeddedMediaDriver) {
            this.embeddedMediaDriver = embeddedMediaDriver;
            return this;
        }

        public AeronConfigBuilder idleStrategy(IdleStrategy idleStrategy) {
            this.idleStrategy = idleStrategy;
            return this;
        }

        public AeronConfigBuilder mediaDriverContext(MediaDriver.Context mediaDriverContext) {
            this.mediaDriverContext = mediaDriverContext;
            return this;
        }

        public AeronConfig build() {
            return new AeronConfig(this);
        }
    }

}
