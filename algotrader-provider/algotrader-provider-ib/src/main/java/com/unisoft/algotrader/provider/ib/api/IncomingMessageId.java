package com.unisoft.algotrader.provider.ib.api;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * Created by alex on 8/2/15.
 */
public enum IncomingMessageId {

    FINISH(-1),
    UNKNOWN(0),
    ACCOUNT_UPDATE_VALUE(6),
    ACCOUNT_UPDATE_TIME(8),
    MANAGED_ACCOUNT_LIST(15),
    ACCOUNT_UPDATE_VALUE_END(54),
    SERVER_MESSAGE(4),
    NEXT_VALID_ORDER_ID(9),
    TICK_PRICE(1),
    TICK_SIZE(2),
    TICK_GENERIC(45),
    TICK_STRING(46),
    TICK_OPTION_COMPUTATION(21),
    TICK_EXCHANFE_FOR_PHYSICAL(47),
    TICK_SNAPSHOT_END(57),
    SERVER_CURRENT_TIME(49),
    EXECUTION_REPORT(11),
    EXECUTION_REPORT_END(55),
    CONTRACT_SPECIFICATION(10),
    CONTRACT_SPECIFICATION_END(52),
    BOND_CONTRACT_SPECIFICATION(18),
    MARKET_DEPTH_UPDATE(12),
    MARKET_DEPTH_LEVEL_TWO_UPDATE(13),
    NEWS_BULLETIN_UPDATE(14),
    FINANCIAL_ADVISOR_CONFIGURATION(16),
    MARKET_SCANNER_VALID_PARAMETERS(19),
    MARKET_SCANNER_DATA(20),
    HISTORICAL_DATA(17),
    REAL_TIME_BAR(50),
    FUNDAMENTAL_DATA(51),
    ORDER_STATE_UPDATE(3),
    RETRIEVE_OPEN_ORDER(5),
    PORTFOLIO_UPDATE(7),
    RETRIEVE_OPEN_ORDER_END(53),
    DELTA_NEUTRAL_VALIDATION(56),
    MARKET_DATA_TYPE(58),
    COMMISSION_REPORT(59);

    private final int id;
    private static final TIntObjectHashMap<IncomingMessageId> MAP = new TIntObjectHashMap();

    static {
        for (final IncomingMessageId incomingMessageId : values()) {
            MAP.put(incomingMessageId.id, incomingMessageId);
        }
    }

    private IncomingMessageId(final int id) {
        this.id = id;
    }

    public final int getId() {
        return id;
    }

    public static final IncomingMessageId fromId(final int id) {
        if (MAP.containsKey(id)) {
            return MAP.get(id);
        }
        return UNKNOWN;
    }
}
