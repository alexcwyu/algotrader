package com.unisoft.algotrader.model.clock;

import javax.inject.Singleton;
import java.util.Date;

/**
 * Created by alex on 5/24/15.
 */
@Singleton
public class SimulationClock implements Clock {
    @Override
    public ClockMode getClockMode() {
        return ClockMode.Simulation;
    }

    @Override
    public long now() {
        return datetime;
    }

    private long datetime = 0;


    @Override
    public void setDateTime(long date){
        datetime = date;
    }

    @Override
    public void addReminder(Date date, Object data, ReminderHandler handler) {

    }

    @Override
    public void addReminder(long date, Object data, ReminderHandler handler) {

    }

    @Override
    public void removeReminder(Date date, ReminderHandler handler) {

    }

    @Override
    public void removeReminder(long date, ReminderHandler handler) {

    }
}
