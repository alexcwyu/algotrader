package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.model.system.LogLevel;
import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class ServerLogLevelRequestSerializer extends Serializer{

    private static final int VERSION = 1;
    public ServerLogLevelRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion, OutgoingMessageId.SERVER_LOG_LEVEL_REQUEST);
    }

    public byte [] serialize(LogLevel logLevel){

        ByteArrayBuilder builder = getByteArrayBuilder();
        builder.append(messageId.getId());
        builder.append(VERSION);
        builder.append(logLevel.getValue());
        return builder.toBytes();
    }

}
