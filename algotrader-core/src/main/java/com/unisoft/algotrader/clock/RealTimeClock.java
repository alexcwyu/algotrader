package com.unisoft.algotrader.clock;

import java.util.Date;

/**
 * Created by alex on 5/24/15.
 */
public class RealTimeClock implements Clock {
    @Override
    public ClockMode getClockMode() {
        return ClockMode.Realtime;
    }

    @Override
    public long now() {
        return System.currentTimeMillis();
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