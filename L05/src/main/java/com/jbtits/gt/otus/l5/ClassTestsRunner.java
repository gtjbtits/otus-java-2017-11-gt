package com.jbtits.gt.otus.l5;

import com.jbtits.gt.otus.l5.suite.TestSuite;
import com.jbtits.gt.otus.l5.tests.CarTest;

public class ClassTestsRunner {
    public static void main(String[] args) {
        TestSuite.runTests(CarTest.class);
    }
}
