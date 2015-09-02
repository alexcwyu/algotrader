package com.unisoft.algotrader.provider.ib;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.unisoft.algotrader.config.AppConfigModule;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by alex on 9/1/15.
 */
public class IBProviderIntegrationTest {

    public static IBProvider provider;

    @BeforeClass
    public static void init(){
        Injector injector = Guice.createInjector(new AppConfigModule());
        provider = injector.getInstance(IBProvider.class);
    }

    @Before
    public void setup(){
        provider.connect();
    }

    @After
    public void teardown(){
        provider.disconnect();
    }


    @Test
    public void testConnect(){
        provider.connect();
        assertTrue(provider.connected());

        assertTrue(provider.getIbSocket().getServerCurrentVersion() > 0);

        provider.disconnect();
        assertFalse(provider.connected());
    }

    @Test
    public void testSubscribeMarketData(){
        provider.connect();


    }

    
}
