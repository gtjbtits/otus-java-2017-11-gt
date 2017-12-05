package com.jbtits.otus.lecture2.impls;

import com.jbtits.otus.lecture2.ifs.Memory;

public class MemoryImpl implements Memory {
    private long a;
    @Override
    public void A() {
        a = MemoryImpl.usedMemory();
    }
    @Override
    public long deltaBA() {
        return MemoryImpl.usedMemory() - a;
    }
    private static long usedMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }
}
