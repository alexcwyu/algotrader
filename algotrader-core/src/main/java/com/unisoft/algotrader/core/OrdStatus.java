package com.unisoft.algotrader.core;

/**
 * Created by alex on 5/21/15.
 */
public enum OrdStatus {
    Undefined,
    New,
    PartiallyFilled,
    Filled,
    DoneForDay,
    Cancelled,
    Replaced,
    PendingCancel,
    Stopped,
    Rejected,
    Suspended,
    PendingNew,
    Calculated,
    Expired,
    AcceptedForBidding,
    PendingReplace
}
