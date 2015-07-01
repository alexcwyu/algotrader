package com.unisoft.algotrader.persistence.cassandra.objectmapper;

import com.datastax.driver.mapping.annotations.UDT;

import java.util.List;

/**
 * Created by alex on 6/28/15.
 */
@UDT(keyspace = "complex", name = "address")
public class Address {
    private String street;
    private String city;
    private int zipCode;
    //private List<Phone> phones;

    public Address() {
    }

    public Address(String street, String city, int zipCode) {
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }
}