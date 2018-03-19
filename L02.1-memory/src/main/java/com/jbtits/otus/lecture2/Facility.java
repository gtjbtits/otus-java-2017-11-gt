package com.jbtits.otus.lecture2;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class Facility {
    private int size;

    public Facility () {
        size = 10_000_000;
        note("The error in memory assessment was about 1B");
    }

    public void sizeOfReference() {
        printResult("Reference", measure(() -> {
            Object container[] = new Object[size];
            return null;
        }));
    }

    public void growContainerSize() {
        Object container[] = new Object[size];
        printResult("Container with 0 Object length [It\'s empty array size (16B)]", measure(() -> {
            for (int i = 0; i < size; i++) {
                container[i] = new Object[0];
            }
            return null;
        }));
        printResult("Container with 1 Object length [16B + reference size (4B), but memory should be allocated a multiple of 8B => 24B]", measure(() -> {
            for (int i = 0; i < size; i++) {
                container[i] = new Object[1];
            }
            return null;
        }));
        printResult("Container with 7 Object length [16B + 4B * 7 = 44B => 48B]", measure(() -> {
            for (int i = 0; i < size; i++) {
                container[i] = new Object[7];
            }
            return null;
        }));
    }

    public void sizeOf(String desc, Supplier<Object> factory) {
        Object container[] = new Object[size];
        printResult(desc, measure(() -> {
            for (int i = 0; i < size; i++) {
                container[i] = factory.get();
            }
            return null;
        }));
    }

    private long measure(Callable malloc) {
        gc();
        long before = usedMemory();
        try {
            malloc.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Math.round((double) (usedMemory() - before) / size);
    }

    private void note(String note) {
        System.out.println("Facility: " + note);
    }

    private void printResult(String desc, long result) {
        System.out.println("Facility: Size of " + desc + " is " + result + "B");
    }

    private static void gc() {
        System.gc();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static long usedMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }
}
