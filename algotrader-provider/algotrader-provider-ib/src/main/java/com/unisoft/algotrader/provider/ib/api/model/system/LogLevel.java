package com.unisoft.algotrader.provider.ib.api.model.system;

/**
 * Created by alex on 9/9/15.
 */
public enum LogLevel {

    SYSTEM(1),
    ERROR(2),
    WARNING(3),
    INFO(4),
    DETAIL(5);

    private final int value;

    private LogLevel(final int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    };
}
