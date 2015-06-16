package com.unisoft.algotrader.networking;

import com.unisoft.algotrader.networking.jeromq.JeroMQConfig;
import com.unisoft.algotrader.networking.jeromq.JeroMQPublisher;
import com.unisoft.algotrader.networking.jeromq.JeroMQSubscriber;
import org.junit.Ignore;

/**
 * Created by alex on 5/16/15.
 */
@Ignore
public class JeroMQPubSubTest extends AbstractPubSubTest{

    public Subscriber createSubScriber(){
        Subscriber subscriber = new JeroMQSubscriber(new JeroMQConfig.JeroMQConfigBuilder().build());
        return subscriber;
    }


    public Publisher createPublisher(){
        Publisher publisher = new JeroMQPublisher(new JeroMQConfig.JeroMQConfigBuilder().build());
        return publisher;
    }

    @Override
    public void test_number() throws Exception {
        super.test_number();
    }


    @Override
    public void test_kyro() throws Exception {
        super.test_kyro();
    }
    @Override
    public void test_msgpack() throws Exception {
        super.test_msgpack();
    }
}
