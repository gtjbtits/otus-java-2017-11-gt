package com.jbtits.gt.otus.l5.suite;

import com.jbtits.gt.otus.l5.suite.annotations.After;
import com.jbtits.gt.otus.l5.suite.annotations.Before;
import com.jbtits.gt.otus.l5.suite.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.lang.reflect.Method;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.logging.Level;

import static com.jbtits.gt.otus.l5.util.ReflectionHelper.*;

public class TestSuite {
    private static final Logger logger = Logger.getLogger(TestSuite.class.getName());

    public static void runTests(Class<?> testClass) {
        List<String> before = new LinkedList<>();
        List<String> after = new LinkedList<>();
        List<String> tests = new LinkedList<>();
        Method[] methods = testClass.getMethods();
        for (Method method : methods) {
            String name = method.getName();
            if (hasAnnotation(method, After.class)) {
                after.add(name);
            } else if (hasAnnotation(method, Before.class)) {
                before.add(name);
            } else if (hasAnnotation(method, Test.class)) {
                tests.add(name);
            }
        }
        if (tests.isEmpty()) {
            throw new RuntimeException("No tests found in class " + testClass.getName());
        }
        logger.log(Level.INFO, "Starting tests for " + testClass.getName());
        Object testInstance = instantiate(testClass, EMPTY_ARRAY);
        if (testInstance == null) {
            throw new NullPointerException("Can\'t instantiate test object");
        }
        Map<String, Boolean> results = new HashMap<>();
        tests.forEach(test -> {
            Map.Entry<String, Boolean> result = runTest(before, after, test, testInstance);
            results.put(result.getKey(), result.getValue());
        });
        System.out.println("Tests results for class " + testClass.getName() + ": " + results);
    }

    public static void runTests(String packageName) {
        Set<Class<?>> classes = getAllPackageClassesWithMethodAnnotation(packageName, Test.class);
        classes.forEach(TestSuite::runTests);
    }

    private static Map.Entry<String, Boolean> runTest(List<String> before, List<String> after, String test, Object testInstance) {
        before.forEach(method -> {
            try {
                callMethod(testInstance, method, EMPTY_ARRAY);
            } catch (InvocationTargetException e) {
                logger.log(Level.SEVERE, "Before method invocation fails", e);
            }
        });
        Map.Entry<String, Boolean> result = ((Supplier<Map.Entry<String, Boolean>>) () -> {
            Object methodReturn;
            try {
                methodReturn = callMethod(testInstance, test, EMPTY_ARRAY);
            } catch (InvocationTargetException e) {
                logger.log(Level.SEVERE, "Test " + test + " unexpected exception", e);
                return new AbstractMap.SimpleEntry<String, Boolean>(test, false);
            }
            if (methodReturn != null && methodReturn instanceof Boolean) {
                return new AbstractMap.SimpleEntry<String, Boolean>(test, (boolean) methodReturn);
            } else {
                logger.log(Level.SEVERE, "Wrong test return type for test " + test + ": boolean expected");
                return new AbstractMap.SimpleEntry<String, Boolean>(test, false);
            }
        }).get();
        after.forEach(method -> {
            try {
                callMethod(testInstance, method, EMPTY_ARRAY);
            } catch (InvocationTargetException e) {
                logger.log(Level.SEVERE, "After method invocation fails", e);
            }
        });
        return result;
    }
}
