package com.unisoft.algotrader.utils.threading;

/**
 * Created by alex on 7/13/15.
 */
import java.util.concurrent.ThreadFactory;

/**
 * Thread Factory for use with Executors that instantiates named threads.
 */

public class NamedThreadFactory implements ThreadFactory {

    final private String threadName;
    private int instanceCount = 0;

    /**
     * Create the factory which appears a name + instance number to each thread.
     */
    public NamedThreadFactory(String threadName) {
        this.threadName = threadName;
    }

    /**
     * name of thread based on passed class name, plus the word "-worker", plus an instance number.
     */
    public NamedThreadFactory(Class<?> clazz) {
        this.threadName = clazz.getSimpleName() + "-worker";
    }

    /**
     * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
     */
    synchronized public Thread newThread(Runnable runnable) {
        return new Thread(runnable, this.threadName + "-" + (++instanceCount));
    }
}