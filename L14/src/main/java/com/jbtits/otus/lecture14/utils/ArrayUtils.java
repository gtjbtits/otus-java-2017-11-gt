package com.jbtits.otus.lecture14.utils;

import com.jbtits.otus.lecture14.utils.sort.SortThread;

import java.util.*;
import java.util.function.BiConsumer;

public class ArrayUtils {
    private static final int POOL_SIZE = 4;
    private static final SortThread workers[] = new SortThread[POOL_SIZE];

    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static long[] concat(long[] first, long[] second) {
        long[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static int[] concat(int[] first, int[] second) {
        int[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static short[] concat(short[] first, short[] second) {
        short[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static byte[] concat(byte[] first, byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static float[] concat(float[] first, float[] second) {
        float[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static double[] concat(double[] first, double[] second) {
        double[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static void handleByChunks(long[] arr, int chunks, BiConsumer<long[], Integer> consumer) {
        if (arr == null || arr.length == 0) {
            return;
        }
        if (arr.length < chunks) {
            throw new IndexOutOfBoundsException("Chunks count must be less than ot equals arr.length");
        }
        int size = arr.length / chunks;
        for (int i = 0; i < chunks; i++) {
            boolean isLast = i == chunks - 1;
            int from = i * size;
            int to = (!isLast) ? from + size : arr.length;
            long chunk[] = Arrays.copyOfRange(arr, from, to);
            consumer.accept(chunk, i);
        }
    }

    public static void handleByChunks(int[] arr, int chunks, BiConsumer<int[], Integer> consumer) {
        if (arr == null || arr.length == 0) {
            return;
        }
        if (arr.length < chunks) {
            throw new IndexOutOfBoundsException("Chunks count must be less than ot equals arr.length");
        }
        int size = arr.length / chunks;
        for (int i = 0; i < chunks; i++) {
            boolean isLast = i == chunks - 1;
            int from = i * size;
            int to = (!isLast) ? from + size : arr.length;
            int chunk[] = Arrays.copyOfRange(arr, from, to);
            consumer.accept(chunk, i);
        }
    }

    public static void handleByChunks(short[] arr, int chunks, BiConsumer<short[], Integer> consumer) {
        if (arr == null || arr.length == 0) {
            return;
        }
        if (arr.length < chunks) {
            throw new IndexOutOfBoundsException("Chunks count must be less than ot equals arr.length");
        }
        int size = arr.length / chunks;
        for (int i = 0; i < chunks; i++) {
            boolean isLast = i == chunks - 1;
            int from = i * size;
            int to = (!isLast) ? from + size : arr.length;
            short chunk[] = Arrays.copyOfRange(arr, from, to);
            consumer.accept(chunk, i);
        }
    }

    public static void handleByChunks(byte[] arr, int chunks, BiConsumer<byte[], Integer> consumer) {
        if (arr == null || arr.length == 0) {
            return;
        }
        if (arr.length < chunks) {
            throw new IndexOutOfBoundsException("Chunks count must be less than ot equals arr.length");
        }
        int size = arr.length / chunks;
        for (int i = 0; i < chunks; i++) {
            boolean isLast = i == chunks - 1;
            int from = i * size;
            int to = (!isLast) ? from + size : arr.length;
            byte chunk[] = Arrays.copyOfRange(arr, from, to);
            consumer.accept(chunk, i);
        }
    }

    public static void handleByChunks(float[] arr, int chunks, BiConsumer<float[], Integer> consumer) {
        if (arr == null || arr.length == 0) {
            return;
        }
        if (arr.length < chunks) {
            throw new IndexOutOfBoundsException("Chunks count must be less than ot equals arr.length");
        }
        int size = arr.length / chunks;
        for (int i = 0; i < chunks; i++) {
            boolean isLast = i == chunks - 1;
            int from = i * size;
            int to = (!isLast) ? from + size : arr.length;
            float chunk[] = Arrays.copyOfRange(arr, from, to);
            consumer.accept(chunk, i);
        }
    }

    public static void handleByChunks(double[] arr, int chunks, BiConsumer<double[], Integer> consumer) {
        if (arr == null || arr.length == 0) {
            return;
        }
        if (arr.length < chunks) {
            throw new IndexOutOfBoundsException("Chunks count must be less than ot equals arr.length");
        }
        int size = arr.length / chunks;
        for (int i = 0; i < chunks; i++) {
            boolean isLast = i == chunks - 1;
            int from = i * size;
            int to = (!isLast) ? from + size : arr.length;
            double chunk[] = Arrays.copyOfRange(arr, from, to);
            consumer.accept(chunk, i);
        }
    }

    public static void handleByChunks(Object[] arr, int chunks, BiConsumer<Object[], Integer> consumer) {
        if (arr == null || arr.length == 0) {
            return;
        }
        if (arr.length < chunks) {
            throw new IndexOutOfBoundsException("Chunks count must be less than ot equals arr.length");
        }
        int size = arr.length / chunks;
        for (int i = 0; i < chunks; i++) {
            boolean isLast = i == chunks - 1;
            int from = i * size;
            int to = (!isLast) ? from + size : arr.length;
            Object chunk[] = Arrays.copyOfRange(arr, from, to);
            consumer.accept(chunk, i);
        }
    }

    public static void merge(long arr[], int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;
        long L[] = new long [n1];
        long R[] = new long [n2];
        for (int i=0; i<n1; ++i)
            L[i] = arr[l + i];
        for (int j=0; j<n2; ++j)
            R[j] = arr[m + 1+ j];
        int i = 0, j = 0;
        int k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    public static void merge(int arr[], int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;
        int L[] = new int [n1];
        int R[] = new int [n2];
        for (int i=0; i<n1; ++i)
            L[i] = arr[l + i];
        for (int j=0; j<n2; ++j)
            R[j] = arr[m + 1+ j];
        int i = 0, j = 0;
        int k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    public static void merge(short arr[], int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;
        short L[] = new short [n1];
        short R[] = new short [n2];
        for (int i=0; i<n1; ++i)
            L[i] = arr[l + i];
        for (int j=0; j<n2; ++j)
            R[j] = arr[m + 1+ j];
        int i = 0, j = 0;
        int k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    public static void merge(byte arr[], int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;
        byte L[] = new byte [n1];
        byte R[] = new byte [n2];
        for (int i=0; i<n1; ++i)
            L[i] = arr[l + i];
        for (int j=0; j<n2; ++j)
            R[j] = arr[m + 1+ j];
        int i = 0, j = 0;
        int k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    public static void merge(float arr[], int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;
        float L[] = new float [n1];
        float R[] = new float [n2];
        for (int i=0; i<n1; ++i)
            L[i] = arr[l + i];
        for (int j=0; j<n2; ++j)
            R[j] = arr[m + 1+ j];
        int i = 0, j = 0;
        int k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    public static void merge(double arr[], int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;
        double L[] = new double [n1];
        double R[] = new double [n2];
        for (int i=0; i<n1; ++i)
            L[i] = arr[l + i];
        for (int j=0; j<n2; ++j)
            R[j] = arr[m + 1+ j];
        int i = 0, j = 0;
        int k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    public static void merge(Object arr[], int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;
        Object L[] = new Object [n1];
        Object R[] = new Object [n2];
        for (int i=0; i<n1; ++i)
            L[i] = arr[l + i];
        for (int j=0; j<n2; ++j)
            R[j] = arr[m + 1+ j];
        int i = 0, j = 0;
        int k = l;
        while (i < n1 && j < n2) {
            if (((Comparable) L[i]).compareTo(R[j]) <= 0) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    public static void sort(long[] source) {
        if (source.length < POOL_SIZE) {
            Arrays.sort(source);
            return;
        }
        ArrayUtils.handleByChunks(source, POOL_SIZE, (array, i) -> workers[i] = new SortThread(array, i));
        SortThread root = SortThread.link(workers);
        root.start();
        System.arraycopy(root.getArray(), 0, source, 0, source.length);
        try {
            root.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("Unexpected interrupt", e);
        }
    }

    public static void sort(int[] source) {
        if (source.length < POOL_SIZE) {
            Arrays.sort(source);
            return;
        }
        ArrayUtils.handleByChunks(source, POOL_SIZE, (array, i) -> workers[i] = new SortThread(array, i));
        SortThread root = SortThread.link(workers);
        root.start();
        System.arraycopy(root.getArray(), 0, source, 0, source.length);
        try {
            root.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("Unexpected interrupt", e);
        }
    }

    public static void sort(short[] source) {
        if (source.length < POOL_SIZE) {
            Arrays.sort(source);
            return;
        }
        ArrayUtils.handleByChunks(source, POOL_SIZE, (array, i) -> workers[i] = new SortThread(array, i));
        SortThread root = SortThread.link(workers);
        root.start();
        System.arraycopy(root.getArray(), 0, source, 0, source.length);
        try {
            root.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("Unexpected interrupt", e);
        }
    }

    public static void sort(byte[] source) {
        if (source.length < POOL_SIZE) {
            Arrays.sort(source);
            return;
        }
        ArrayUtils.handleByChunks(source, POOL_SIZE, (array, i) -> workers[i] = new SortThread(array, i));
        SortThread root = SortThread.link(workers);
        root.start();
        System.arraycopy(root.getArray(), 0, source, 0, source.length);
        try {
            root.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("Unexpected interrupt", e);
        }
    }

    public static void sort(float[] source) {
        if (source.length < POOL_SIZE) {
            Arrays.sort(source);
            return;
        }
        ArrayUtils.handleByChunks(source, POOL_SIZE, (array, i) -> workers[i] = new SortThread(array, i));
        SortThread root = SortThread.link(workers);
        root.start();
        System.arraycopy(root.getArray(), 0, source, 0, source.length);
        try {
            root.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("Unexpected interrupt", e);
        }
    }

    public static void sort(double[] source) {
        if (source.length < POOL_SIZE) {
            Arrays.sort(source);
            return;
        }
        ArrayUtils.handleByChunks(source, POOL_SIZE, (array, i) -> workers[i] = new SortThread(array, i));
        SortThread root = SortThread.link(workers);
        root.start();
        System.arraycopy(root.getArray(), 0, source, 0, source.length);
        try {
            root.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("Unexpected interrupt", e);
        }
    }

    public static void sort(Object[] source) {
        if (source.length < POOL_SIZE) {
            Arrays.sort(source);
            return;
        }
        ArrayUtils.handleByChunks(source, POOL_SIZE, (array, i) -> workers[i] = new SortThread(array, i));
        SortThread root = SortThread.link(workers);
        root.start();
        System.arraycopy(root.getArray(), 0, source, 0, source.length);
        try {
            root.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("Unexpected interrupt", e);
        }
    }
}
