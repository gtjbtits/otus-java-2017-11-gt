package com.jbtits.otus.lecture2.impls;

import com.jbtits.otus.lecture2.ifs.Facility;
import com.jbtits.otus.lecture2.ifs.GarbageCollector;
import com.jbtits.otus.lecture2.ifs.Memory;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class FacilityImpl implements Facility {
    private int size;
    private Memory mem;
    private GarbageCollector gc;

    public FacilityImpl (Memory mem, GarbageCollector gc) {
        size = 10_000_000;
        this.gc = gc;
        this.mem = mem;
    }

    @Override
    public void sizeOfReference() {
        printResult("Reference", measure(() -> {
            Object container[] = new Object[size];
            return null;
        }));
    }

    @Override
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
        gc.collect();
        mem.A();
        try {
            malloc.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Math.round((double) mem.deltaBA() / size);
    }

    private void printResult(String desc, long result) {
        System.out.println("Facility: Size of " + desc + " is " + result + "B");
    }
}
