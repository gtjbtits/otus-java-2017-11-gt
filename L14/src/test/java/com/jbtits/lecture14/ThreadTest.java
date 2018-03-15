package com.jbtits.lecture14;

import org.junit.Test;

public class ThreadTest {
    @Test
    public void arrayGetSetTest() {
        new Thread(() -> {
            while (true) {}
        }).start();
    }

    private class SetThread extends Thread {

    }
}
