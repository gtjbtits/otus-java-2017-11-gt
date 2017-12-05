package com.jbtits.otus.lecture2.impls;

import com.jbtits.otus.lecture2.ifs.Memory;
import com.jbtits.otus.lecture2.ifs.GarbageCollector;

public class GarbageCollectorImpl implements GarbageCollector {
    private int collectAttempts;
    private int gcDelay_ms;
    private Memory mem;
    public GarbageCollectorImpl(Memory memory) {
        collectAttempts = 3;
        gcDelay_ms = 10;
        this.mem = memory;
    }

    @Override
    public void collect() {
        callGarbageCollector(0);
    }

    private void callGarbageCollector(int attempt) {
        if (attempt < collectAttempts) {
            mem.A();
            System.gc();
            try {
                Thread.sleep(gcDelay_ms);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (mem.deltaBA() >= 0) {
                callGarbageCollector(++attempt);
            }
        } else {
            System.out.println("GarbageCollector: can't collect garbage after " + attempt + " attempts. Sorry... (");
        }
    }
}
