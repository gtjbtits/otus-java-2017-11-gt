package com.jbtits.otus.lecture15;

import org.junit.Test;

public class ExceptionTest {
    @Test
    public void causeTest() {
        try {
            throw new RuntimeException("test");
        } catch (Exception e) {
            System.out.println(e.getCause());
        }
    }
}
