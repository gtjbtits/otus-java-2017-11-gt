package com.jbtits.otus.lecture2.ifs;

import java.util.function.Supplier;

public interface Facility {
    void sizeOfReference();
    void sizeOf(String desc, Supplier<Object> o);
}
