package com.unisoft.algotrader.provider.ib.api.serializer;

/**
 * Created by alex on 8/7/15.
 */
public class RegisterClientRequestSerializer extends Serializer{

    public RegisterClientRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion);
    }

    public byte [] serialize(int clientId){
        ByteArrayBuilder builder = getByteArrayBuilder();
        builder.append(clientId);
        return builder.toBytes();
    }

}
