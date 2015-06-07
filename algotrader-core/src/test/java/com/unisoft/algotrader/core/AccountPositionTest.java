package com.unisoft.algotrader.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by alex on 5/31/15.
 */
public class AccountPositionTest {

    @Test
    public void should_return_correct_value(){
        AccountPosition position = new AccountPosition(Currency.USD);

        assertEquals(0.0, position.getValue(), 0.0);

        position.add(new AccountTransaction(Currency.USD, 1000));
        assertEquals(1000, position.getValue(), 0.0);

        position.add(new AccountTransaction(Currency.USD, 86.1));
        assertEquals(1086.1, position.getValue(), 0.0);

        position.add(new AccountTransaction(Currency.USD, -750));
        assertEquals(1086.1-750, position.getValue(), 0.0);

    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception(){
        AccountPosition position = new AccountPosition(Currency.USD);
        position.add(new AccountTransaction(Currency.HKD, 1000));

    }
}
