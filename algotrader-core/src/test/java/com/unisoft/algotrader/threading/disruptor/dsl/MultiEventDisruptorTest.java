package com.unisoft.algotrader.threading.disruptor.dsl;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.ProducerType;
import com.unisoft.algotrader.threading.MultiEventProcessor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

/**
 * Created by alex on 6/11/15.
 */
public class MultiEventDisruptorTest {

    private static final int TIMEOUT_IN_SECONDS = 2;
    private MultiEventDisruptor<TestEvent> disruptor;
    private StubExecutor executor;
    private RingBuffer<TestEvent> ringBuffer;
    private TestEvent lastPublishedEvent;

    private final Collection<DelayEventProcessor> delayedEventHandlers = new ArrayList<DelayEventProcessor>();
    private final Collection<TestWorkHandler> testWorkHandlers = new ArrayList<TestWorkHandler>();

    @Before
    public void setup(){
        executor = new StubExecutor();
        disruptor = new MultiEventDisruptor(ProducerType.SINGLE, TestEvent.EVENT_FACTORY, 4, executor);
    }

    @After
    public void tearDown() throws Exception
    {
        for (DelayEventProcessor delayedEventHandler : delayedEventHandlers)
        {
            delayedEventHandler.stopWaiting();
        }
        for (TestWorkHandler testWorkHandler : testWorkHandlers)
        {
            testWorkHandler.stopWaiting();
        }

        disruptor.halt();
        executor.joinAllThreads();
    }

    @Test
    public void shouldCreateEventProcessorGroupForFirstEventProcessors() throws Exception{
        executor.ignoreExecutions();
        final SleepingEventProcessor eventHandler1 = new SleepingEventProcessor();
        SleepingEventProcessor eventHandler2 = new SleepingEventProcessor();

        final MultiEventHandlerGroup<TestEvent> eventHandlerGroup =
                disruptor.handleEventsWith(eventHandler1, eventHandler2);
        disruptor.start();

        assertNotNull(eventHandlerGroup);
        assertThat(Integer.valueOf(executor.getExecutionCount()), equalTo(Integer.valueOf(2)));
    }

    @Test
    public void shouldMakeEntriesAvailableToFirstHandlersImmediately() throws Exception
    {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        MultiEventProcessor multiEventProcessor = new StubEventProcessor(countDownLatch);

        disruptor.handleEventsWith(createDelayedEventHandler(), multiEventProcessor);

        ensureTwoEventsProcessedAccordingToDependencies(countDownLatch);
    }



    private DelayEventProcessor createDelayedEventHandler()
    {
        final DelayEventProcessor delayedEventHandler = new DelayEventProcessor();
        delayedEventHandlers.add(delayedEventHandler);
        return delayedEventHandler;
    }

    private void ensureTwoEventsProcessedAccordingToDependencies(final CountDownLatch countDownLatch,
                                                                 final DelayEventProcessor... dependencies)
            throws InterruptedException, BrokenBarrierException
    {
        publishEvent();
        publishEvent();

        for (DelayEventProcessor dependency : dependencies)
        {
            assertThatCountDownLatchEquals(countDownLatch, 2L);
            dependency.processEvent();
            dependency.processEvent();
        }

        assertThatCountDownLatchIsZero(countDownLatch);
    }


    private TestEvent publishEvent() throws InterruptedException, BrokenBarrierException
    {
        if (ringBuffer == null)
        {
            ringBuffer = disruptor.start();

            for (DelayEventProcessor eventHandler : delayedEventHandlers)
            {
                eventHandler.awaitStart();
            }
        }

        disruptor.publishEvent(new EventTranslator<TestEvent>()
        {
            @Override
            public void translateTo(final TestEvent event, final long sequence)
            {
                lastPublishedEvent = event;
            }
        });

        return lastPublishedEvent;
    }


    private void assertThatCountDownLatchEquals(final CountDownLatch countDownLatch,
                                                final long expectedCountDownValue)
    {
        assertThat(Long.valueOf(countDownLatch.getCount()), equalTo(Long.valueOf(expectedCountDownValue)));
    }

    private void assertThatCountDownLatchIsZero(final CountDownLatch countDownLatch)
            throws InterruptedException
    {
        boolean released = countDownLatch.await(TIMEOUT_IN_SECONDS, SECONDS);
        assertTrue("Batch handler did not receive entries: " + countDownLatch.getCount(), released);
    }


}
