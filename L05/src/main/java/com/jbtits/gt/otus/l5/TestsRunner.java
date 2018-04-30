package com.jbtits.gt.otus.l5;

import com.jbtits.gt.otus.l5.suite.TestSuite;
import com.jbtits.gt.otus.l5.tests.CarTest;

import java.util.Arrays;

public class TestsRunner {
    public static void main(String[] args) {
        if (args.length != 0 && args[0].equals("package")) {
            // package tests
            TestSuite.runTests("com.jbtits.gt.otus.l5.tests");
        } else {
            // single class tests
            TestSuite.runTests(CarTest.class);
        }
    }
}
