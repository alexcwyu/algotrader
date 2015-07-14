package com.unisoft.algotrader.persistence;

import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/**
 * Created by alex on 7/14/15.
 */
public class RangeCachingIdSupplier implements IdSupplier<Long>{
    private static final double exhaust_factor = 0.2;

    private final ExecutorService pool = Executors.newSingleThreadExecutor();

    private final BlockingQueue<Long> queue = new LinkedBlockingQueue<>();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final int threshold;

    private final GetSequenceRange range;
    private final int size;

    public RangeCachingIdSupplier(GetSequenceRange range, int size) {
        this.range = range;
        this.size = size;
        this.threshold = (int) Math.max(size * exhaust_factor, 1.0);
    }

    @Override
    public Long next() {
        lock.writeLock().lock();
        try {
            if (queue.size() <= threshold) {
                pool.submit(getNextRangeIfNeeded);
            }
            return take();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int remaining() {
        return queue.size();
    }

    void shutdown() throws Exception {
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);
    }

    private final Runnable getNextRangeIfNeeded = new Runnable() {
        @Override
        public void run() {
            if (queue.size() <= threshold) try {
                Sequence sequence = range.next(size);
                for (long id : sequence.stream().toArray()) {
                    queue.put(id);
                }
            } catch (InterruptedException ignored) {
            }
        }
    };

    private long take() {
        final AtomicInteger attempts = new AtomicInteger(0);
        Optional<Long> id = Optional.empty();
        while (attempts.incrementAndGet() < 60 && !id.isPresent()) {
            try {
                id = Optional.ofNullable(queue.poll(1, TimeUnit.SECONDS));
            } catch (InterruptedException ignore) {
            }
        }
        return id.orElseThrow(() -> new RuntimeException(
                String.format("Failed to retrieve id after %d attempts", attempts.get())
        ));
    }
}
