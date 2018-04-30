package com.jbtits.gt.otus.l5.tests;

import com.jbtits.gt.otus.l5.objects.Car;
import com.jbtits.gt.otus.l5.objects.Grass;
import com.jbtits.gt.otus.l5.suite.annotations.After;
import com.jbtits.gt.otus.l5.suite.annotations.Before;
import com.jbtits.gt.otus.l5.suite.annotations.Test;

public class GrassTest {
    private Grass grass;
    @Before
    public void init() {
        grass = new Grass();
    }

    @Test
    public boolean grassIsGreen() {
        return grass.getColor().equals("green");
    }

    @Test
    public boolean winterTest() {
        grass.winterHasCome();
        return grass.getColor().equals("orange");
    }
}
