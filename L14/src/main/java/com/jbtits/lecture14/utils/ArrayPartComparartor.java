package com.jbtits.lecture14.utils;

import java.math.BigDecimal;
import java.util.Comparator;

public class ArrayPartComparartor implements Comparator<ArrayPartElement> {
    public int compare(ArrayPartElement a, ArrayPartElement b){
        return new BigDecimal(a.getValue().toString()).compareTo(new BigDecimal(b.getValue().toString()));
    }
}
