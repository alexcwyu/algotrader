package com.unisoft.algotrader.networking;

import com.unisoft.algotrader.networking.aeron.AeronConfig;
import com.unisoft.algotrader.networking.aeron.AeronPublisher;
import com.unisoft.algotrader.networking.aeron.AeronSubscriber;
import org.junit.Ignore;

/**
 * Created by alex on 5/16/15.
 */
@Ignore
public class AeronPubSubTest extends AbstractPubSubTest{

    public Subscriber createSubScriber(){
        Subscriber subscriber = new AeronSubscriber(new AeronConfig.AeronConfigBuilder().build());
        return subscriber;
    }


    public Publisher createPublisher(){
        Publisher publisher = new AeronPublisher(new AeronConfig.AeronConfigBuilder().build());
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
