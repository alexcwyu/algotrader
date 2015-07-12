package com.unisoft.algotrader.model.clock;

import java.util.Date;

/**
 * Created by alex on 5/24/15.
 */
public interface Clock {

    public static enum ClockMode{
        Realtime,
        Simulation
    }

    ClockMode getClockMode();

    long now();

    default void setDateTime(Date date){
        setDateTime(date.getTime());
    }

    default void setDateTime(long date){
        throw new UnsupportedOperationException();
    }

    void addReminder(Date date, Object data, ReminderHandler handler);

    void addReminder(long date, Object data, ReminderHandler handler);

    void removeReminder(Date date,ReminderHandler handler);

    void removeReminder(long date,ReminderHandler handler);

    public static Clock CLOCK = new SimulationClock();
}


