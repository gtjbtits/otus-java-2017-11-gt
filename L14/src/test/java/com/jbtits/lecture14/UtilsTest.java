package com.jbtits.lecture14;

import com.jbtits.lecture14.utils.ArrayUtils;
import com.jbtits.lecture14.utils.MathHelper;
import com.jbtits.lecture14.utils.Matrix;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jbtits.lecture14.utils.ArrayUtils.handleByChunks;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UtilsTest {
    @Test
    public void createArray() {
        Long arr[] = (Long[]) ArrayUtils.createArray(Long.class, 10);
        System.out.println(arr);
    }

    @Test
    public void handleByChunksTest() {
        Integer arr[] = {5,4,3,2,1,0,-1};
        handleByChunks(arr, 3, (e, i) -> System.out.println("chunk " + i + ": " + Arrays.asList((Object[]) e)));
    }

    @Test
    public void matrixCreateTest() {
        Matrix<Integer> matrix = new Matrix<>(2, 3, Integer.class);
        Integer[] row1 = {1,2,3};
        Integer[] row2 = {4,5,6};
        matrix.setRow(0, row1);
        matrix.setRow(1, row2);
        System.out.println(matrix);
    }

    @Test
    public void compareTest() {
        Integer a = 7;
        Integer b = 10;
        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println(MathHelper.compare(a,b));
    }

    @Test
    public void SemaphoreTest() throws InterruptedException {
        Semaphore s = new Semaphore(0);
        s.release();
        s.release();
//        s.release();
        s.acquire();
        System.out.println("After acquire!");
        s.acquire();
        System.out.println("After second acquire!");
    }

    @Test
    public void multiMergeTest() {
        Integer a[] = {1,2,3};
        Integer b[] = {4,5,6};
        System.out.println(Arrays.asList(ArrayUtils.mergeMultisort(Arrays.asList(a,b))));
    }

    @Test
    public void mapTest() {
        String strs[] = new String[2];
        List<Integer> ints = Arrays.stream(strs).map(s -> 10).collect(Collectors.toList());
//        Arrays.stream(strs).forEach(System.out::println);
        ints.stream().forEach(System.out::println);
    }

    @Test
    public void arrayOfLinksCopyTest() {
        Long a[] = new Long[10];
        for (int i = 0; i < a.length; i++) {
            a[i] = 10L;
        }
        Long b[] = Arrays.copyOfRange(a, 0, a.length);
        b[1] = 11L;
        System.out.println(a[1]);
        System.out.println(b[1]);
    }

    @Test
    public void arrayOfLinksAssignTest() {
        Long a[] = new Long[10];
        for (int i = 0; i < a.length; i++) {
            a[i] = 10L;
        }
        Long b[] = new Long[3];
        b[0] = a[0];
        b[1] = a[1];
        b[2] = a[2];
        b[1] = 5L;
        a[2] = 7L;
        assertEquals(10L, a[1].longValue());
        assertEquals(10L, b[2].longValue());
    }
}
