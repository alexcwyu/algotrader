package com.unisoft.algotrader.utils.actor;


import com.unisoft.algotrader.utils.threading.NamedThreadFactory;

import java.util.concurrent.*;


/**
 * Created by alex on 7/13/15.
 */
public abstract class Actor<Q, R>  {

    // MessageQs per class
    private final static ConcurrentMap<Class<?>,NamedThreadFactory> messageQNameFactory = new ConcurrentHashMap<>();

    // Back queue
    protected final ExecutorService messageQ;

    /**
     * Create it with a message queue
     */
    protected Actor() {
        Class<?> clazz = this.getClass();
        if (!messageQNameFactory.containsKey(clazz)) {
            messageQNameFactory.putIfAbsent(clazz, new NamedThreadFactory("MessageQ-" + clazz.getSimpleName()));
        }
        this.messageQ = Executors.newSingleThreadExecutor(messageQNameFactory.get(clazz));
    }

    /**
     * Send a Q message to the actress. Returns a Future<R>.
     * occurred during processing.
     */
    public Future<R> send(final Q message) {
        return messageQ.submit(() -> onMessage(message));
    }

    /**
     * Send a message to the actress and wait for your result.
     */
    public R sendAndWait(final Q message) {
        try {
            return send(message).get();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted Exception: " + e, e);
        } catch (ExecutionException e) {
            throw new RuntimeException("Execution Exception: " + e, e);
        }
    }

    /**
     * Shutdown the actor
     */
    public void stop() {
        messageQ.shutdown();
    }

    /**
     * Override this for message processing
     */
    protected abstract R onMessage(Q msg) throws Exception;

}
