package com.unisoft.algotrader.model.trading;

import org.msgpack.annotation.OrdinalEnum;

/**
 * Created by alex on 5/21/15.
 */
@OrdinalEnum
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
