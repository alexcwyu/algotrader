package com.unisoft.algotrader.model.trading;

import org.msgpack.annotation.OrdinalEnum;

/**
 * Created by alex on 5/21/15.
 */
@OrdinalEnum
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
