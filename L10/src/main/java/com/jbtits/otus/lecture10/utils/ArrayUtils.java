package com.jbtits.otus.lecture10.utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class ArrayUtils {
    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
    public static <T> boolean doubleEach(List<T> list1, List<T> list2, BiPredicate<T,T> each) {
        if (list1 == null && list2 == null) {
            return true;
        }
        if (list1 == null || list2 == null || list1.size() != list2.size()) {
            return false;
        }
        Iterator<T> it1 = list1.iterator();
        Iterator<T> it2 = list2.iterator();
        while(it1.hasNext()) {
            if (!each.test(it1.next(), it2.next())) {
                return false;
            }
        }
        return true;
    }
}
