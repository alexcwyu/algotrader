package com.unisoft.algotrader.provider.ib.api;

import com.unisoft.algotrader.provider.ib.IBConfig;
import org.apache.commons.io.IOUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by alex on 8/1/15.
 */
public class IBSession {

    private final IBConfig ibConfig;
    private int clientCurrentVersion;
    private int mininumRequiredVersion;
    private int serverCurrentVersion;

    private Socket socket;
    private OutputStream outputStream;

    public IBSession(IBConfig ibConfig){
        this.ibConfig = ibConfig;
    }

    public void connect(){

        try {
            this.socket = new Socket(ibConfig.host, ibConfig.port);
            this.outputStream = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException e){

        }
    }

    public void send(final byte[] bytes) throws IOException {
        IOUtils.write(bytes, outputStream);
    }

    public void disconnect(){
        IOUtils.closeQuietly(outputStream);
        IOUtils.closeQuietly(socket);
    }

    public boolean isConnected(){
        return socket != null && socket.isConnected();
    }

}
