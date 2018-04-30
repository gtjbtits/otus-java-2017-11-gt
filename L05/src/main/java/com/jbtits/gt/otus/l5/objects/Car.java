package com.jbtits.gt.otus.l5.objects;

import com.jbtits.gt.otus.l5.suite.annotations.Test;

public class Car {
    private final int wheelsCount;
    public Car() {
        wheelsCount = 4;
    }
    public int getWheelsCount() {
        return wheelsCount;
    }
    public void destroy() {
        // TODO: destroy car instance
    }
}
