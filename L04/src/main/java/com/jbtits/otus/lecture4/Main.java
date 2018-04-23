package com.jbtits.otus.lecture4;

import java.util.LinkedList;
import java.util.List;

public class Main {
//    private static final int POOL_SIZE = 81616; // Concurrent
//    private static final int POOL_SIZE = 94902; // G1
    private static final int POOL_SIZE = 90684; // Serial, Parallel

    public static void main(String[] args) throws InterruptedException {
        List objects = new LinkedList();
        while (true) {
            for (int i = 0; i < POOL_SIZE; i++) {
                objects.add(new Object());
            }
            for (int i = 0; i < POOL_SIZE / 2; i++) {
                objects.remove(objects.size() - 1);
            }
            Thread.sleep(1000);
        }
    }
}
