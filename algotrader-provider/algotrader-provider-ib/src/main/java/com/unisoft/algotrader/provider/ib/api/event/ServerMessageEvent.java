package com.unisoft.algotrader.provider.ib.api.event;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by alex on 8/26/15.
 */
public class ServerMessageEvent extends IBEvent<ServerMessageEvent>  {

    public final int code;
    public final String message;
    public List<Object> parameters = Lists.newArrayList();

    public ServerMessageEvent(final int code, final String message, final Object... parameters) {
        this(-1, code, message);
        if ((parameters != null) && (parameters.length > 0)) {
            this.parameters = Lists.newArrayList(parameters);
        }
    }

    public ServerMessageEvent(final long requestId, final int code, final String message) {
        super(requestId);
        this.code = code;
        this.message = message;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onServerMessageEvent(this);
    }

    @Override
    public String toString() {
        return "ServerMessageEvent{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", parameters=" + parameters +
                "} " + super.toString();
    }
}