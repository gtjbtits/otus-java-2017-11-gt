package com.jbtits.otus.lecture2;

import com.jbtits.otus.lecture2.ifs.Facility;
import com.jbtits.otus.lecture2.ifs.GarbageCollector;
import com.jbtits.otus.lecture2.impls.FacilityImpl;
import com.jbtits.otus.lecture2.impls.GarbageCollectorImpl;
import com.jbtits.otus.lecture2.ifs.Memory;
import com.jbtits.otus.lecture2.impls.MemoryImpl;

public class Main {
    public static void main(String[] args) {
        Memory mem = new MemoryImpl();
        GarbageCollector gc = new GarbageCollectorImpl(mem);
        Facility facility = new FacilityImpl(mem, gc);

        facility.sizeOfReference();
        facility.sizeOf("Object", () -> new Object());
        facility.sizeOf("Empty String", () -> new String(new char[0]));
        facility.sizeOf("String with pool", () -> new String());
        facility.sizeOf("String without pool", () -> new String(new char[2]));
        facility.sizeOf("Zero length array", () -> new System[0]);
    }
}
