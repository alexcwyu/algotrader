package com.unisoft.algotrader.utils.config;

/**
 * Created by alex on 6/16/15.
 */

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public abstract class BaseConfigModule extends AbstractModule {

    @Override
    protected void configure() {
        bindConfig();
    }

    private void bindProperties() {
        try {
            Properties properties = new Properties();
            properties.load(this.getClass().getClassLoader().getResourceAsStream("application.properties"));
            Names.bindProperties(binder(), properties);
        } catch (IOException ex) {
        }
    }

    private Config loadConfig(){
        Config conf = ConfigFactory.load();
        return conf;
    }

    private void bindConfig(){
        Config config = loadConfig();
        for (Map.Entry<String, ConfigValue> entry : config.entrySet()){
            ConfigValue value = entry.getValue();

            switch (value.valueType()){
                case STRING:
                case NUMBER:
                case BOOLEAN:
                    bindPrimitive(entry.getKey(), value);
                    break;
                case LIST:
                    bindList(entry.getKey(), value);
                    break;
                case NULL:
                    throw new IllegalArgumentException("Did not expect NULL entry in ConfigValue.entrySet");
                case OBJECT:
                    throw new IllegalArgumentException("Did not expect OBJECT entry in ConfigValue.entrySet");

            }
        }
    }


    private void bindPrimitive(String key, ConfigValue value){
        String valueStr = value.unwrapped().toString();
        binder().bindConstant().annotatedWith(Names.named(key)).to(valueStr);
    }

    private void bindList(String key, ConfigValue value){
        List list = (List)value.unwrapped();
        bind(List.class).annotatedWith(Names.named(key)).toInstance(list);

    }


}