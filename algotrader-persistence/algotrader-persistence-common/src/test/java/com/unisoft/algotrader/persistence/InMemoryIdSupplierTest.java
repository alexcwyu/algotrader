package com.unisoft.algotrader.persistence;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
/**
 * Created by alex on 7/14/15.
 */
public class InMemoryIdSupplierTest {
    private InMemoryIdSupplier supplier;

    @Before
    public void setup(){
        supplier = new InMemoryIdSupplier();
    }

    @Test
    public void should_increment_id() throws Exception {
        assertEquals(1L, supplier.next().longValue());
        assertEquals(2L, supplier.next().longValue());
        assertEquals(3L, supplier.next().longValue());
    }
}
