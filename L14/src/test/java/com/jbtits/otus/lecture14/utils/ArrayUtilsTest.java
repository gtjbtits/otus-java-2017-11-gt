package com.jbtits.otus.lecture14.utils;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class ArrayUtilsTest {
    private static final int ARRAY_LENGTH = 1_000_000;

    @Test
    public void canSortArrayOfLongs() {
        long source[] = new long[ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            source[i] = randLong();
        }
        long sourceCopy[] = Arrays.copyOf(source, source.length);
        Arrays.parallelSort(source);
        ArrayUtils.sort(sourceCopy);
        assertTrue(Arrays.equals(source, sourceCopy));
    }

    @Test
    public void canSortArrayOfInts() {
        int source[] = new int[ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            source[i] = randInt();
        }
        int sourceCopy[] = Arrays.copyOf(source, source.length);
        Arrays.parallelSort(source);
        ArrayUtils.sort(sourceCopy);
        assertTrue(Arrays.equals(source, sourceCopy));
    }

    @Test
    public void canSortArrayOfShorts() {
        short source[] = new short[ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            source[i] = randShort();
        }
        short sourceCopy[] = Arrays.copyOf(source, source.length);
        Arrays.parallelSort(source);
        ArrayUtils.sort(sourceCopy);
        assertTrue(Arrays.equals(source, sourceCopy));
    }

    @Test
    public void canSortArrayOfBytes() {
        byte source[] = new byte[ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            source[i] = randByte();
        }
        byte sourceCopy[] = Arrays.copyOf(source, source.length);
        Arrays.parallelSort(source);
        ArrayUtils.sort(sourceCopy);
        assertTrue(Arrays.equals(source, sourceCopy));
    }

    @Test
    public void canSortArrayOfFloats() {
        float source[] = new float[ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            source[i] = randFloat();
        }
        float sourceCopy[] = Arrays.copyOf(source, source.length);
        Arrays.parallelSort(source);
        ArrayUtils.sort(sourceCopy);
        assertTrue(Arrays.equals(source, sourceCopy));
    }

    @Test
    public void canSortArrayOfDoubles() {
        double source[] = new double[ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            source[i] = randDouble();
        }
        double sourceCopy[] = Arrays.copyOf(source, source.length);
        Arrays.parallelSort(source);
        ArrayUtils.sort(sourceCopy);
        assertTrue(Arrays.equals(source, sourceCopy));
    }

    @Test
    public void canSortArrayOfObjects() {
        Integer source[] = new Integer[ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            source[i] = randInt();
        }
        Integer sourceCopy[] = Arrays.copyOf(source, source.length);
        Arrays.parallelSort(source);
        ArrayUtils.sort(sourceCopy);
        assertTrue(Arrays.equals(source, sourceCopy));
    }

    private static long randLong() {
        return Math.round(Math.random() * 1000);
    }

    private static int randInt() {
        return (int) randLong();
    }

    private static short randShort() {
        return (short) randLong();
    }

    private static byte randByte() {
        return (byte) randLong();
    }

    private static float randFloat() {
        return (float) randLong();
    }

    private static double randDouble() {
        return (double) randLong();
    }
}
