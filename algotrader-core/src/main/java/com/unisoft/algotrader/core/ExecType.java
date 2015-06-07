package com.unisoft.algotrader.core;

/**
 * Created by alex on 5/21/15.
 */
public enum ExecType {
    Undefined,
    New,
    PartialFill,
    Fill,
    DoneForDay,
    Cancelled,
    Replace,
    PendingCancel,
    Stopped,
    Rejected,
    Suspended,
    PendingNew,
    Calculated,
    Expired,
    Restarted,
    PendingReplace,
    Trade,
    TradeCorrect,
    TradeCancel,
    OrderStatus
}
