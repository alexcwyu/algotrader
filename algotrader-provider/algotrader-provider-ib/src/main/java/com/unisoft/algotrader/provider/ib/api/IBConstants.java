package com.unisoft.algotrader.provider.ib.api;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.model.trading.OrdType;
import com.unisoft.algotrader.model.trading.Side;
import com.unisoft.algotrader.model.trading.TimeInForce;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by alex on 8/3/15.
 */
public class IBConstants {

    public static final String EMPTY = "";
    public static final String UNKNOWN = "UNKNOWN";
    public static final byte[] EMPTY_BYTES = EMPTY.getBytes();
    public static final byte[] UNKNOWN_BYTES = UNKNOWN.getBytes();

    public enum SecType{

        STOCK(Instrument.InstType.Stock, "STK"),
        OPTION(Instrument.InstType.Option, "OPT"),
        FUTURE(Instrument.InstType.Future, "FUT"),
        INDEX(Instrument.InstType.Index, "IND"),
        FUTURE_ON_OPTION(Instrument.InstType.FutureOption, "FOP"),
        FOREX(Instrument.InstType.FX, "CASH"),
        COMBO(Instrument.InstType.Combo, "BAG");

        private static Map<Instrument.InstType, byte[]> map = Maps.newHashMap();
        private static Map<byte[], Instrument.InstType> byteArrayMap = Maps.newHashMap();
        private static Map<String, Instrument.InstType> stringMap = Maps.newHashMap();

        private final Instrument.InstType instType;
        private final byte[] bytes;
        private final String shortName;

        SecType(Instrument.InstType instType, String shortName){
            this.instType = instType;
            this.bytes = shortName.getBytes();
            this.shortName = shortName;
        }

        public Instrument.InstType instType() {
            return instType;
        }

        public byte[] bytes() {
            return bytes;
        }

        public String shortName() {
            return shortName;
        }

        static {
            for (SecType secType : SecType.values()) {
                map.put(secType.instType, secType.bytes);
                byteArrayMap.put(secType.bytes, secType.instType);
                stringMap.put(secType.shortName, secType.instType);
            }
        }

        public static byte[] convert(Instrument.InstType instType){
            if (instType == null)
                return EMPTY_BYTES;
            if (instType == Instrument.InstType.ETF)
                return STOCK.bytes;
            if (map.containsKey(instType))
                return map.get(instType);
            return UNKNOWN_BYTES;
        }

        public static Instrument.InstType convert(String name){
            if (name!= null) {
                for (SecType secType : SecType.values()) {
                    if (secType.shortName.endsWith(name)) {
                        return secType.instType;
                    }
                }
            }
            return null;
        }
        public static Instrument.InstType convert(byte[] bytes){
            if (bytes!= null) {
                for (SecType secType : SecType.values()) {
                    if (Arrays.equals(bytes, secType.bytes)) {
                        return secType.instType;
                    }
                }
            }
            return null;
        }
    }

    public enum TIF{

        UNKNOWN(TimeInForce.Undefined, "UNKNOWN"),
        DAY(TimeInForce.Day, "DAY"),
        GOOD_TILL_CANCEL(TimeInForce.GTC, "GTC"),
        IMMEDIATE_OR_CANCEL(TimeInForce.FOK, "IOC"),
        GOOD_TILL_DATE(TimeInForce.GoodTillDate, "GTD");

        private static Map<TimeInForce, byte[]> map = Maps.newHashMap();
        private static Map<byte[], TimeInForce> byteArrayMap = Maps.newHashMap();
        private static Map<String, TimeInForce> stringMap = Maps.newHashMap();

        private final TimeInForce tif;
        private final byte[] bytes;
        private final String shortName;

        TIF(TimeInForce tif, String shortName){
            this.tif = tif;
            this.shortName = shortName;
            this.bytes = shortName.getBytes();
        }

        public TimeInForce tif() {
            return tif;
        }

        public byte[] bytes() {
            return bytes;
        }

        public String shortName() {
            return shortName;
        }
        static {
            for (TIF timeInForce : TIF.values()) {
                map.put(timeInForce.tif, timeInForce.bytes);
                byteArrayMap.put(timeInForce.bytes, timeInForce.tif);
            }
        }

        public static byte[] convert(TimeInForce tif){
            if (tif == null)
                return EMPTY_BYTES;
            if (map.containsKey(tif))
                return map.get(tif);
            return UNKNOWN.bytes;
        }

        public static TimeInForce convert(byte[] bytes){
            if (bytes!= null) {
                for (TIF timeInForce : TIF.values()) {
                    if (Arrays.equals(bytes, timeInForce.bytes)) {
                        return timeInForce.tif;
                    }
                }
            }
            return null;
        }
        public static TimeInForce convert(String name){
            if (name!= null) {
                for (TIF timeInForce : TIF.values()) {
                    if (timeInForce.shortName.equals(name)) {
                        return timeInForce.tif;
                    }
                }
            }
            return null;
        }
    }

    public enum OptionRight{

        PUT(Instrument.PutCall.Put, "PUT"),
        CALL(Instrument.PutCall.Call, "CALL");

        private static Map<Instrument.PutCall, byte[]> map = Maps.newHashMap();
        private static Map<byte[], Instrument.PutCall> byteArrayMap = Maps.newHashMap();
        private static Map<String, Instrument.PutCall> stringMap = Maps.newHashMap();

        private final Instrument.PutCall putCall;
        private final byte[] bytes;
        private final String shortName;

        OptionRight(Instrument.PutCall putCall, String shortName){
            this.putCall = putCall;
            this.bytes = shortName.getBytes();
            this.shortName = shortName;
        }

        public Instrument.PutCall putCall() {
            return putCall;
        }

        public byte[] bytes() {
            return bytes;
        }

        public String shortName() {
            return shortName;
        }
        static {
            for (OptionRight optionRight : OptionRight.values()) {
                map.put(optionRight.putCall, optionRight.bytes);
                byteArrayMap.put(optionRight.bytes, optionRight.putCall);
                stringMap.put(optionRight.shortName, optionRight.putCall);
            }
        }

        public static byte[] convert(Instrument.PutCall putCall){
            if (putCall == null)
                return EMPTY_BYTES;
            if (map.containsKey(putCall))
                return map.get(putCall);
            return UNKNOWN_BYTES;
        }

        public static Instrument.PutCall convert(byte[] bytes){
            if (bytes!= null) {
                for (OptionRight optionRight : OptionRight.values()) {
                    if (Arrays.equals(bytes, optionRight.bytes)) {
                        return optionRight.putCall;
                    }
                }
            }
            return null;
        }

        public static Instrument.PutCall convert(String name){
            if (name!= null) {
                for (OptionRight optionRight : OptionRight.values()) {
                    if (optionRight.shortName.equals(name)) {
                        return optionRight.putCall;
                    }
                }
            }
            return null;
        }
    }

    public enum OrderStatus{

        UNKNOWN("UNKNOWN"),
        PENDING_SUBMIT("PendingSubmit"),
        PENDING_CANCEL("PendingCancel"),
        PRE_SUBMITTED("PreSubmitted"),
        SUBMITTED("Submitted"),
        CANCELLED("Cancelled"),
        FILLED("Filled"),
        INACTIVE("Inactive"),
        EMPTY("");

        private final String label;
        private static final Map<String, OrderStatus> MAP;

        private OrderStatus(final String label) {
            this.label = label;
        }

        static {
            MAP = Maps.newHashMap();
            for (final OrderStatus orderStatus : values()) {
                MAP.put(orderStatus.label.toUpperCase(), orderStatus);
            }
        }
        public static final OrderStatus fromLabel(final String label) {
            if (StringUtils.isBlank(label)) {
                return EMPTY;
            }
            final String labelUpperCase = label.toUpperCase();
            if (MAP.containsKey(labelUpperCase)) {
                return MAP.get(labelUpperCase);
            }
            return UNKNOWN;
        }
    }

    public enum OrderType{

        MARKET(OrdType.Market, "MARKET"),
        LIMIT(OrdType.Limit, "LIMIT"),
        STOP(OrdType.Stop, "STOP"),
        STOP_LIMIT(OrdType.StopLimit, "STOP_LIMIT"),
        MARKET_ON_CLOSE(OrdType.MarketOnClose, "MARKET_ON_CLOSE"),
        LIMIT_ON_CLOSE(OrdType.LimitOnClose, "LIMIT_ON_CLOSE"),
        TRAILING_STOP(OrdType.TrailingStop, "TRAILING_STOP"),
        MARKET_TO_LIMIT(OrdType.MarketWithLeftoverAsLimit, "MARKET_TO_LIMIT"),
        MARKET_IF_PRICE_TOUCHED(OrdType.MIT, "MARKET_IF_PRICE_TOUCHED"),
        MARKET_ON_OPEN(OrdType.OnClose, "MARKET_ON_OPEN"),
        UNKNOWN(OrdType.Undefined, "UNKNOWN");

        private static Map<OrdType, byte[]> ordTypeMap = Maps.newHashMap();
        private static Map<byte[], OrdType> byteArrayMap = Maps.newHashMap();
        private static Map<String, OrdType> stringMap = Maps.newHashMap();

        private final OrdType ordType;
        private final byte[] bytes;
        private final String shortName;


        OrderType(OrdType ordType, String shortName){
            this.ordType = ordType;
            this.shortName = shortName;
            this.bytes = shortName.getBytes();
        }

        public OrdType ordType() {
            return ordType;
        }

        public byte[] bytes() {
            return bytes;
        }

        public String shortName() {
            return shortName;
        }
        static {
            for (OrderType orderType : OrderType.values()) {
                ordTypeMap.put(orderType.ordType, orderType.bytes);
                byteArrayMap.put(orderType.bytes, orderType.ordType);
                stringMap.put(orderType.shortName, orderType.ordType);
            }
        }

        public static byte[] convert(OrdType ordType){
            if (ordType == null)
                return EMPTY_BYTES;
            if (ordTypeMap.containsKey(ordType))
                return ordTypeMap.get(ordType);
            return UNKNOWN.bytes;
        }

        public static OrdType convert(byte[] bytes){
            if (bytes!= null) {
                for (OrderType orderType : OrderType.values()) {
                    if (Arrays.equals(bytes, orderType.bytes)) {
                        return orderType.ordType;
                    }
                }
            }
            return OrdType.Undefined;
        }
        public static OrdType convert(String name){
            if (name!= null) {
                for (OrderType orderType : OrderType.values()) {
                    if (orderType.shortName.equals(name)) {
                        return orderType.ordType;
                    }
                }
            }
            return OrdType.Undefined;
        }
    }

    public enum Action{

        BUY(Side.Buy, "BUY"),
        SELL(Side.Sell, "SELL"),
        SSHORT(Side.SellShort, "SSHORT"),
        UNKNOWN(Side.Undefined, "UNKNOWN");

        private static Map<Side, byte[]> sideMap = Maps.newHashMap();
        private static Map<byte[], Side> byteArrayMap = Maps.newHashMap();
        private static Map<String, Side> stringArrayMap = Maps.newHashMap();

        private final Side side;
        private final byte[] bytes;
        private final String shortName;

        Action(Side side, String shortName){
            this.side = side;
            this.bytes = shortName.getBytes();
            this.shortName = shortName;
        }

        public Side getSide() {
            return side;
        }

        public byte[] getBytes() {
            return bytes;
        }

        public String shortName() {
            return shortName;
        }
        static {
            for (Action action : Action.values()) {
                sideMap.put(action.side, action.bytes);
                byteArrayMap.put(action.bytes, action.side);
                stringArrayMap.put(action.shortName, action.side);
            }
        }

        public static byte[] convert(Side side){
            if (side == null)
                return EMPTY_BYTES;
            if (sideMap.containsKey(side))
                return sideMap.get(side);
            return UNKNOWN.bytes;
        }

        public static Side convert(byte[] bytes){
            if (bytes!= null) {
                for (Action action : Action.values()) {
                    if (Arrays.equals(bytes, action.bytes)) {
                        return action.side;
                    }
                }
            }
            return Side.Undefined;
        }

        public static Side convert(String name){
            if (name!= null) {
                for (Action action : Action.values()) {
                    if (action.shortName.equals(name)) {
                        return action.side;
                    }
                }
            }
            return Side.Undefined;
        }
    }


    public enum IBSide{

        BUY(Side.Buy, "BOT"),
        SELL(Side.Sell, "SLD"),
        UNKNOWN(Side.Undefined, "UNKNOWN");

        private static Map<Side, byte[]> sideMap = Maps.newHashMap();
        private static Map<byte[], Side> byteArrayMap = Maps.newHashMap();
        private static Map<String, Side> stringArrayMap = Maps.newHashMap();

        private final Side side;
        private final byte[] bytes;
        private final String shortName;

        IBSide(Side side, String shortName){
            this.side = side;
            this.bytes = shortName.getBytes();
            this.shortName = shortName;
        }

        public Side getSide() {
            return side;
        }

        public byte[] getBytes() {
            return bytes;
        }

        public String shortName() {
            return shortName;
        }
        static {
            for (Action action : Action.values()) {
                sideMap.put(action.side, action.bytes);
                byteArrayMap.put(action.bytes, action.side);
                stringArrayMap.put(action.shortName, action.side);
            }
        }

        public static byte[] convert(Side side){
            if (side == null)
                return EMPTY_BYTES;
            if (sideMap.containsKey(side))
                return sideMap.get(side);
            return UNKNOWN.bytes;
        }

        public static Side convert(byte[] bytes){
            if (bytes!= null) {
                for (Action action : Action.values()) {
                    if (Arrays.equals(bytes, action.bytes)) {
                        return action.side;
                    }
                }
            }
            return Side.Undefined;
        }

        public static Side convert(String name){
            if (name!= null) {
                for (Action action : Action.values()) {
                    if (action.shortName.equals(name)) {
                        return action.side;
                    }
                }
            }
            return Side.Undefined;
        }
    }
    public enum RealTimeBarDataType{
        UNKNOWN("UNKNOWN"), EMPTY(""), TRADES("TRADES"), BID("BID"), ASK("ASK"), MID_POINT("MIDPOINT");

        private final byte[] bytes;

        RealTimeBarDataType(String type){
            this.bytes = type.getBytes();
        }

        public byte[] getBytes() {
            return bytes;
        }
    }

    public enum ReturnedTickTypeFilter {

        UNKNOWN(-1), OPTION_VOLUME(100), OPTION_OPEN_INTEREST(101), HISTORICAL_VOLATILITY(104), OPTION_IMPLIED_VOLATILITY(
                106), INDEX_FUTURE_PREMIUM(162), MISCELLANEOUS_STATS(165), MARK_PRICE(221), AUCTION_VALUES(225),
        REAL_TIME_VOLUME(233), SHORTABLE(236), INVENTORY(256), FUNDAMENTAL_RATIOS(258),
        REAL_TIME_HISTORICAL_VOLATILITY(411);

        private final int id;
        private static final Map<Integer, ReturnedTickTypeFilter> MAP;

        static {
            MAP = Maps.newHashMap();
            for (final ReturnedTickTypeFilter filterReturnedTickType : values()) {
                MAP.put(filterReturnedTickType.getId(), filterReturnedTickType);
            }
        }

        private ReturnedTickTypeFilter(final int id) {
            this.id = id;
        }

        public final int getId() {
            return id;
        }

        public static final ReturnedTickTypeFilter fromId(final int id) {
            if (MAP.containsKey(id)) {
                return MAP.get(id);
            }
            return UNKNOWN;
        }
    }


    public interface BarSize{

        int SEC_1  = 1;
        int SEC_5  = 2;
        int SEC_15 = 3;
        int SEC_30 = 4;
        int MIN_1  = 5;
        int MIN_2  = 6;
        int MIN_5  = 7;
        int MIN_15 = 8;
        int MIN_30 = 9;
        int HOUR_1 = 10;
        int DAY_1  = 11;
    }

    public enum TickType {

        UNKNOWN(-1), BID_SIZE(0), BID_PRICE(1), ASK_PRICE(2), ASK_SIZE(3), LAST_PRICE(4), LAST_SIZE(5), DAY_HIGH(6),
        DAY_LOW(7), VOLUME(8), CLOSE(9), BID_OPTION_COMPUTATION(10), ASK_OPTION_COMPUTATION(11),
        LAST_OPTION_COMPUTATION(12), MODEL_OPTION_COMPUTATION(13), DAY_OPEN(14), LOW_13_WEEK(15), HIGH_13_WEEK(16),
        LOW_26_WEEK(17), HIGH_26_WEEK(18), LOW_52_WEEK(19), HIGH_52_WEEK(20), AVERAGE_VOLUME(21), OPEN_INTEREST(22),
        OPTION_HISTORICAL_VOLATILITY(23), OPTION_IMPLIED_VOLATILITY(24), OPTION_BID_EXCHANGE(25), OPTION_ASK_EXCHANGE(26),
        OPTION_CALL_OPEN_INTEREST(27), OPTION_PUT_OPEN_INTEREST(28), OPTION_CALL_VOLUME(29), OPTION_PUT_VOLUME(30),
        INDEX_FUTURE_PREMIUM(31), BID_EXCHANGE(32), ASK_EXCHANGE(33), AUCTION_VOLUME(34), AUCTION_PRICE(35),
        AUCTION_IMBALANCE(36), MARK_PRICE(37), BID_EFP_COMPUTATION(38), ASK_EFP_COMPUTATION(39), LAST_EFP_COMPUTATION(40),
        OPEN_EFP_COMPUTATION(41), HIGH_EFP_COMPUTATION(42), LOW_EFP_COMPUTATION(43), CLOSE_EFP_COMPUTATION(44),
        LAST_TIMESTAMP(45), SHORTABLE(46), FUNDAMENTAL_RATIOS(47), REAL_TIME_VOLUME(48), HALTED(49), BID_YIELD(50),
        ASK_YIELD(51), LAST_YIELD(52), CUSTOM_OPTION_COMPUTATION(53), TRADE_COUNT(54), TRADE_RATE(55), VOLUME_RATE(56);

        private final int value;
        private static final Map<Integer, TickType> MAP;

        static {
            MAP = Maps.newHashMap();
            for (final TickType tickType : values()) {
                MAP.put(tickType.getValue(), tickType);
            }
        }

        private TickType(final int value) {
            this.value = value;
        }

        public final int getValue() {
            return value;
        }

        public static final TickType fromValue(final int value) {
            if (MAP.containsKey(value)) {
                return MAP.get(value);
            }
            return UNKNOWN;
        }
    }

    public interface ErrorCode{
        // Constants
        int ORDER_INVALID_ID         = 106;
        int ORDER_INCOMPLETE_ORDER   = 107;
        int ORDER_PRICE_OUT_OF_RANGE = 109;
        int ORDER_INVALID_PRICE      = 110;

        int HISTORICAL_MARKET_DATA_SERVICE_ERROR = 162;

        int NO_SECURITY_DEFINITION = 200;

        int MARKET_DEPTH_DATA_RESET = 317;

        int SERVER_ERROR_VALIDATING_API_REQUEST = 321;
    }

    public interface FADataType{
        // Constants
        int GROUPS          = 1;
        int PROFILES        = 2;
        int ACCOUNT_ALIASES = 3;
    }

    public interface QuoteSide{
        // Constants
        int ASK = 0;
        int BID = 1;
    }

    public interface Origin{
        // Constants
        int  CUSTOMER            = 0;
        int  FIRM                = 1;
        char OPT_UNKNOWN         = '?';
        char OPT_BROKER_DEALER   = 'b';
        char OPT_CUSTOMER        = 'c';
        char OPT_FIRM            = 'f';
        char OPT_ISEMM           = 'm';
        char OPT_FARMM           = 'n';
        char OPT_SPECIALIST      = 'y';
        int  AUCTION_MATCH       = 1;
        int  AUCTION_IMPROVEMENT = 2;
        int  AUCTION_TRANSPARENT = 3;
    }
}
