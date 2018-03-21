package com.jbtits.otus.lecture3;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GtArrayList<T> implements List<T> {
    private static final int INITIAL_CAPACITY = 2;
    private Object container[];
    private int size;
    private int capacity;

    public GtArrayList() {
        capacity = INITIAL_CAPACITY;
        size = 0;
        container = new Object[capacity];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size < 1;
    }

    @Override
    public boolean contains(Object o) {
        return getStream().anyMatch(e -> e.equals(o));
    }

    private Stream<T> getStream() {
        return (Stream<T>) Arrays.stream(container).limit(size);
    }

    @Override
    public Iterator<T> iterator() {
        return new GtIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        System.arraycopy(container, 0, array, 0, size);
        return array;
    }

    @Override
    public <R> R[] toArray(R[] a) {
        if (a.length < size) {
            return (R[]) Arrays.copyOf(container, size, a.getClass());
        }
        System.arraycopy(container, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean add(T t) {
        if (container.length == size) {
            reAllocate();
        }
        container[size] = t;
        size++;
        return true;
    }

    private void growCapacity() {
        capacity = capacity * 2;
    }

    private void reAllocate() {
        growCapacity();
        container = Arrays.copyOf(container, capacity);
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        return (T) container[index];
    }

    @Override
    public T set(int index, T element) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        if (element == null) {
            throw new NullPointerException();
        }
        T prevElement = get(index);
        container[index] = element;
        return prevElement;
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new GtListIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new GtListIterator();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "[" + getStream().map(Object::toString).collect(Collectors.joining(",")) + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GtArrayList<?> that = (GtArrayList<?>) o;
        return size == that.size &&
            Arrays.equals(toArray(), that.toArray());
    }

    private class GtIterator implements Iterator<T> {
        protected int cursor;

        public GtIterator() {
            cursor = 0;
        }

        @Override
        public boolean hasNext() {
            return Math.abs(cursor) < GtArrayList.this.size;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (cursor < 0) {
                cursor *= -1;
            } else {
                cursor++;
            }
            return (T) GtArrayList.this.container[cursor - 1];
        }
    }

    private class GtListIterator extends GtIterator implements ListIterator<T> {
        private boolean isElementReady;

        public GtListIterator() {
            super();
            isElementReady = false;
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0 || Math.abs(cursor) > 1;
        }

        @Override
        public T previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            if (cursor > 0) {
                cursor *= -1;
            } else {
                cursor++;
            }
            isElementReady = true;
            return (T) GtArrayList.this.container[Math.abs(cursor) - 1];
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (cursor < 0) {
                cursor *= -1;
            } else {
                cursor++;
            }
            isElementReady = true;
            return (T) GtArrayList.this.container[cursor - 1];
        }

        @Override
        public int nextIndex() {
            return cursor >= 0 ? cursor : Math.abs(cursor) - 1;
        }

        @Override
        public int previousIndex() {
            return cursor > 0 ? cursor - 1 : (Math.abs(cursor) > 1 ? Math.abs(cursor) - 2 : -1);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(T t) {
            if (!isElementReady) {
                throw new IllegalStateException();
            }
            GtArrayList.this.container[Math.abs(cursor) - 1] = t;
        }

        @Override
        public void add(T t) {
            throw new UnsupportedOperationException();
        }
    }
}
