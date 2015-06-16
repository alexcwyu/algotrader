package com.unisoft.algotrader.threading.disruptor.waitstrategy;


import com.lmax.disruptor.AlertException;
import com.lmax.disruptor.SequenceBarrier;

class DummySequenceBarrier implements SequenceBarrier
{

    private final SequenceUpdater sequenceUpdater;

    DummySequenceBarrier(SequenceUpdater sequenceUpdater){
        this.sequenceUpdater = sequenceUpdater;
    }
    @Override
    public long waitFor(long sequence) throws AlertException, InterruptedException
    {
        return sequenceUpdater.sequence.get();
    }

    @Override
    public long getCursor()
    {
        return 0;
    }

    @Override
    public boolean isAlerted()
    {
        return false;
    }

    @Override
    public void alert()
    {
    }

    @Override
    public void clearAlert()
    {
    }

    @Override
    public void checkAlert() throws AlertException
    {
    }
}