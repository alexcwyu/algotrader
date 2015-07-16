package com.unisoft.algotrader.config;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Created by alex on 7/16/15.
 */
public class AppConfigTest {
    public static void main(String [] args){

        Injector injector = Guice.createInjector(new AppConfigModule());
        AppConfig appConfig = injector.getInstance(AppConfig.class);

        System.out.print(appConfig);
    }
}
