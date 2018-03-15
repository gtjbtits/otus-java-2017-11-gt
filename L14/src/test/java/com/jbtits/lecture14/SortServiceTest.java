package com.jbtits.lecture14;

import com.jbtits.lecture14.utils.ArrayUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.manipulation.Sortable;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class SortServiceTest {

    private SortService sortService;

    @Before
    public void init() {
        sortService = new SortServiceImpl();
    }

    @After
    public void tearDown() {
        sortService.dispose();
    }

    @Test
    public void canSortArray() {
        int length = 999_999;
        Long testSource[] = new Long[length];
        for (int i = 0; i < length; i++) {
            testSource[i] = Math.round(Math.random() * 1000);
        }
        Long source[] = Arrays.copyOf(testSource, testSource.length);
        Arrays.sort(testSource);
        sortService.sort(source);
//        System.out.println(Arrays.asList(testSource));
//        System.out.println(Arrays.asList(source));
        assertTrue(Arrays.equals(testSource, source));
    }
}
