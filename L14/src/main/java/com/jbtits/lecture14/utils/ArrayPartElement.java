package com.jbtits.lecture14.utils;

import java.util.Comparator;

public class ArrayPartElement {
    public Number getValue() {
        return value;
    }

    public int getArrayPartIdx() {
        return arrayPartIdx;
    }

    public int getElementIdx() {
        return elementIdx;
    }

    public static Comparator<ArrayPartElement> getComparator() {
        return null;
    }

    private Number value;
    private int arrayPartIdx;
    private int elementIdx;

    ArrayPartElement(Number value, int arrayPartIdx, int elementIdx) {
        this.value = value;
        this.arrayPartIdx = arrayPartIdx;
        this.elementIdx = elementIdx;
    }
}
