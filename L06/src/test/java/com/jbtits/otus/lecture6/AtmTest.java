package com.jbtits.otus.lecture6;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AtmTest {
    private Atm atm;

    @Before
    public void setUp() {
        atm = new Atm();
        putInitialSum();
    }

    private int initialSum;

    private void putInitialSum() {
        atm.put(Denomination.TEN, 20);
        atm.put(Denomination.HUNDRED, 4);
        atm.put(Denomination.THOUSAND, 1);
        initialSum = 20 * 10 + 4 * 100 + 1000;
    }

    @Test
    public void sumShouldBeCorrectAfterPut() {
        assertEquals(initialSum, atm.sum());
    }

    @Test
    public void sumShouldBeCorrectAfterGet() {
        atm.get(1400);
        for (int i = 0; i < 20; i++) {
            atm.get(10);
        }
        assertEquals(0, atm.sum());
    }

    @Test(expected = NotEnoughNotes.class)
    public void cantWithdrawMoreThanItHas() {
        atm.get(2000);
    }

    @Test
    public void immuneForNegativeSumValue() {
        atm.get(-500);
        assertEquals(initialSum, atm.sum());
    }
}
