package com.unisoft.algotrader.provider.ib.api.exception;


import com.unisoft.algotrader.provider.ib.api.model.system.ClientMessageCode;

/**
 * Created by alex on 8/26/15.
 */
public class IBApiClientException extends RuntimeException{

    private final String detailedMessage;
    private final ClientMessageCode clientMessageCode;

    public IBApiClientException(final ClientMessageCode clientMessageCode, final String detailedMessage) {
        this(clientMessageCode, detailedMessage, null);
    }

    public IBApiClientException(final ClientMessageCode clientMessageCode, final String detailedMessage,
                                   final Throwable cause) {
        super(clientMessageCode.getMessage() + " " + detailedMessage, cause);
        this.detailedMessage = detailedMessage;
        this.clientMessageCode = clientMessageCode;
    }

    public final String getDetailedMessage() {
        return detailedMessage;
    }

    public final ClientMessageCode getClientMessageCode() {
        return clientMessageCode;
    }

}
