package com.unisoft.algotrader.persistence.cassandra.objectmapper;

import com.datastax.driver.mapping.EnumType;
import com.datastax.driver.mapping.annotations.Enumerated;
import com.datastax.driver.mapping.annotations.UDT;

/**
 * Created by alex on 6/28/15.
 */
@UDT(keyspace = "complex", name = "phone")
public class Phone {
    enum Type { Mobile, Work, Home, Other };

    @Enumerated(EnumType.STRING)
    private Type type;

    private String phone;

    public Phone(){

    }

    public Phone(Type type, String phone) {
        this.type = type;
        this.phone = phone;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
