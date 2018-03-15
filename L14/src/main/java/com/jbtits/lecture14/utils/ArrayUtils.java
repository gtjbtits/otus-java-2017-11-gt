package com.jbtits.lecture14.utils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

public class ArrayUtils {
    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static <T> boolean doubleEach(List<T> list1, List<T> list2, BiPredicate<T, T> each) {
        if (list1 == null && list2 == null) {
            return true;
        }
        if (list1 == null || list2 == null || list1.size() != list2.size()) {
            return false;
        }
        Iterator<T> it1 = list1.iterator();
        Iterator<T> it2 = list2.iterator();
        while (it1.hasNext()) {
            if (!each.test(it1.next(), it2.next())) {
                return false;
            }
        }
        return true;
    }

    public static void handleByChunks(Object[] arr, int chunkCnt, BiConsumer<Object, Integer> consumer) {
        if (arr == null || arr.length == 0) {
            return;
        }
        if (arr.length < chunkCnt) {
            consumer.accept(arr, 0);
            return;
        }
        int chunkSize = arr.length / chunkCnt;
        for (int i = 0; i < chunkCnt; i++) {
            boolean isLast = i == chunkCnt - 1;
            int from = i * chunkSize;
            int to = (!isLast) ? from + chunkSize : arr.length;
            Object chunk[] = Arrays.copyOfRange(arr, from, to);
            consumer.accept(chunk, i);
        }
    }

    public static Object createArray(Class<?> clazz, int... dimensions) {
        return Array.newInstance(clazz, dimensions);
    }

    public static Number[] mergeMultisortList(List<List<Number>> sortedParts) {
        Comparator<ArrayPartElement> comparator = new ArrayPartComparartor();
        PriorityQueue<ArrayPartElement> queue = new PriorityQueue<>(10, comparator);
        int length = 0;
        int partIdx = 0;
        for (List<Number> sortedPart : sortedParts) {
            if (sortedPart.size() > 0) {
                ArrayPartElement element = new ArrayPartElement(
                        sortedPart.get(0),
                        partIdx,
                        0
                );
                queue.add(element);
                length += sortedPart.size();
                partIdx++;
            }
        }
        Number[] result = new Number[length];
        int i = 0;
        while (!queue.isEmpty()) {
            ArrayPartElement element = queue.remove();
            result[i++] = element.getValue();
            List<Number> part = sortedParts.get(element.getArrayPartIdx());
            if (element.getElementIdx() + 1 < part.size()) {
                queue.add(new ArrayPartElement(
                        part.get(element.getElementIdx() + 1),
                        element.getArrayPartIdx(),
                        element.getElementIdx() + 1
                ));
            }
        }
        return result;
    }

    public static Number[] mergeMultisort(List<Number[]> sortedParts) {
        Comparator<ArrayPartElement> comparator = new ArrayPartComparartor();
        PriorityQueue<ArrayPartElement> queue = new PriorityQueue<>(10, comparator);
        int length = 0;
        int partIdx = 0;
        for (Number[] sortedPart : sortedParts) {
            if (sortedPart.length > 0) {
                ArrayPartElement element = new ArrayPartElement(
                        sortedPart[0],
                        partIdx,
                        0
                );
                queue.add(element);
                length += sortedPart.length;
                partIdx++;
            }
        }
        Number[] result = new Number[length];
        int i = 0;
        while (!queue.isEmpty()) {
            ArrayPartElement element = queue.remove();
            result[i++] = element.getValue();
            Number[] part = sortedParts.get(element.getArrayPartIdx());
            if (element.getElementIdx() + 1 < part.length) {
                queue.add(new ArrayPartElement(
                        part[element.getElementIdx() + 1],
                        element.getArrayPartIdx(),
                        element.getElementIdx() + 1
                ));
            }
        }
        return result;
    }
}
