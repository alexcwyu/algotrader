package com.unisoft.algotrader.threading;

import com.lmax.disruptor.*;

import java.util.Queue;

/**
 * Created by alex on 4/12/15.
 */
public interface MultiBufferWaitStrategy extends WaitStrategy{

    default boolean hasNext(long[] sequences, SequenceBarrier[] barriers, Queue queue){
        final int barrierLength = barriers.length;
        for (int i = 0; i < barrierLength; i++)
        {

            try {
                long sequence = sequences[i];
                //long nextSequence = sequence.get() + 1L;
                //long available = barriers[i].waitFor(nextSequence);


                long available = barriers[i].waitFor(-1);

                if (available > sequence) {
                    return true;
                }
            }
            catch(Exception e){

            }

        }
        return queue != null && queue.peek() != null;
    }

    public abstract void waitNext(long[] sequences, SequenceBarrier[] barriers, Queue queue)throws AlertException, InterruptedException, TimeoutException;

    default long waitFor(long sequence, Sequence cursor, Sequence dependentSequence, SequenceBarrier barrier)
            throws AlertException, InterruptedException, TimeoutException{
        waitNext(new long[] {sequence}, new SequenceBarrier[]{barrier}, null);
        return dependentSequence.get();
    }

    public abstract void signalAllWhenBlocking();
}
