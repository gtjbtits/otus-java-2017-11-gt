package com.jbtits.lecture14.utils;

import java.math.BigDecimal;
import java.util.Comparator;

public class MathHelper {
    public static int compare(Number a, Number b) {
        return new BigDecimal(a.toString()).compareTo(new BigDecimal(b.toString()));
    }
    public static Comparator<? super Number> comparator() {
        return (Comparator<Number>) (a, b) -> compare(a,b);
    }
}
