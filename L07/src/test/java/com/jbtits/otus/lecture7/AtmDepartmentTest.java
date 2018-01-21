package com.jbtits.otus.lecture7;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AtmDepartmentTest {
    @Test
    public void canContainsMultimpleAtms() {
        Atm atm1 = new Atm();
        Atm atm2 = new Atm();
        AtmDepartment department = new AtmDepartment(atm1, atm2);
        assertEquals(2, department.getAtmsCount());
    }

}