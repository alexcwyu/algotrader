package com.unisoft.algotrader.utils.networking.jeromq;

import org.zeromq.ZMQ;

/**
 * Created by alex on 4/19/15.
 */
public class JeroMQConfig {

    public final int ioThreads;
    public final int socketType;
    public final int linger;
    public final int hwm;
    public final String pubSubAddress;
    public final String reqRepAddress;
    public final byte[] topic;

    private JeroMQConfig(JeroMQConfigBuilder builder){
        this.ioThreads = builder.ioThreads;
        this.socketType = builder.socketType;
        this.linger = builder.linger;
        this.hwm = builder.hwm;
        this.pubSubAddress = builder.pubSubAddress;
        this.reqRepAddress = builder.reqRepAddress;
        this.topic = builder.topic;
    }


    public static class JeroMQConfigBuilder{

        private int ioThreads = 1;
        private int socketType;
        private int linger = 5000;
        private int hwm = 0;
        private String pubSubAddress = "tcp://localhost:5561";
        private String reqRepAddress = "tcp://localhost:5562";
        private byte[] topic = ZMQ.SUBSCRIPTION_ALL;
        public JeroMQConfigBuilder ioThreads(int ioThreads){
            this.ioThreads = ioThreads;
            return this;
        }

        public JeroMQConfigBuilder socketType(int socketType){
            this.socketType = socketType;
            return this;
        }

        public JeroMQConfigBuilder linger(int linger){
            this.linger = linger;
            return this;
        }

        public JeroMQConfigBuilder hwm(int hwm){
            this.hwm = hwm;
            return this;
        }

        public JeroMQConfigBuilder pubSubAddress(String pubSubAddress){
            this.pubSubAddress = pubSubAddress;
            return this;
        }

        public JeroMQConfigBuilder reqRepAddress(String reqRepAddress){
            this.reqRepAddress = reqRepAddress;
            return this;
        }

        public JeroMQConfigBuilder topic(byte[] topic){
            this.topic = topic;
            return this;
        }

        public JeroMQConfig build(){
            return new JeroMQConfig(this);
        }

    }


}
