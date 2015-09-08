package com.unisoft.algotrader.provider.ib.api.model.system;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alex on 8/1/15.
 */
public enum ClientMessageCode {

    UNKNOWN_MESSAGE(0, "Unknown message."),
    CONNECTION_ERROR(1, "Connection error."),
    INTERNAL_ERROR(2, "Internal error."),
    INPUT_OUTPUT_STREAM_EXCEPTION(10, "Input/Output stream exception."),
    ERROR_CREATING_OUPUT_STREAM(11, "Error creating output stream."),
    ERROR_CREATING_INPUT_STREAM(12, "Error creating input stream."),
    CANT_START_SESSION(13, "Can't start session."),
    SERVER_CURRENT_VERSION(14, "Server current version."),
    CONNECTION_SERVER_TIME(15, "Connection server time."),
    CANT_PUBLISH_REQUEST(16, "Can't publish request."),
    SUBSCRIPTION_ALREADY_SUBSCRIBED(20, "Subscription already subscribed, skip"),
    ALREADY_CONNECTED(501, "Already connected."),
    CONNECTION_FAILURE(502,  "Couldn't connect to TWS. Confirm that \"Enable ActiveX and Socket Clients\" is enabled on the TWS \"Edit->Global Configuration->API->Settings\" menu."),
    UPDATE_TWS(503, "TWS is out of date and must be updated."),
    ACCOUNT_UPDATE_SUBSCRIPTION_REQUEST_FAILED(513, "Account Update Request Sending Error.");

    private final int code;
    private final String message;
    private static final Map<Integer, ClientMessageCode> MAP;

    static {
        MAP = Maps.newHashMap();
        for (final ClientMessageCode code : values()) {
            MAP.put(code.getCode(), code);
        }
    }

    private ClientMessageCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    public final int getCode() {
        return code;
    }

    public final String getMessage() {
        return message;
    }

    public static final ClientMessageCode fromCode(final int code) {
        if (MAP.containsKey(code)) {
            return MAP.get(code);
        }
        return UNKNOWN_MESSAGE;
    }
}
