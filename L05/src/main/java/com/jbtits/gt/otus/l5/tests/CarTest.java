package com.jbtits.gt.otus.l5.tests;

import com.jbtits.gt.otus.l5.objects.Car;
import com.jbtits.gt.otus.l5.suite.annotations.After;
import com.jbtits.gt.otus.l5.suite.annotations.Before;
import com.jbtits.gt.otus.l5.suite.annotations.Test;

public class CarTest {
    private Car car;

    @Before
    public void init() {
        car = new Car();
    }

    @After
    public void tearDown() {
        car.destroy();
    }

    @Test
    public boolean carHas4Wheels() {
        return 4 == car.getWheelsCount();
    }

    @Test
    public void someNoExceptionTest() {
        throw new RuntimeException("Unexpected me!");
    }
}
