package com.jbtits.otus.lecture14.utils.sort;

import com.jbtits.otus.lecture14.utils.ArrayUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class SortThread extends Thread {
    private int id;
    private final List<SortThread> children;
    private volatile Object array;
    private int middle;
    private int right;
    private Class<?> arrayClass;
    private Semaphore sorted;

    private SortThread(int id) {
        this.id = id;
        children = new LinkedList<>();
        sorted = new Semaphore(0);
    }

    public SortThread(long[] array, int id) {
        this(id);
        this.array = array;
        this.arrayClass = long[].class;
    }

    public SortThread(int[] array, int id) {
        this(id);
        this.array = array;
        this.arrayClass = int[].class;
    }

    public SortThread(short[] array, int id) {
        this(id);
        this.array = array;
        this.arrayClass = short[].class;
    }

    public SortThread(byte[] array, int id) {
        this(id);
        this.array = array;
        this.arrayClass = byte[].class;
    }

    public SortThread(float[] array, int id) {
        this(id);
        this.array = array;
        this.arrayClass = float[].class;
    }

    public SortThread(double[] array, int id) {
        this(id);
        this.array = array;
        this.arrayClass = double[].class;
    }

    public SortThread(Object[] array, int id) {
        this(id);
        this.array = array;
        this.arrayClass = Object.class;
    }

    public static SortThread link(SortThread[] workers) {
        if (workers == null || workers.length == 0) {
            throw new RuntimeException("Not enough workers for linkage");
        }
        if (workers.length == 1) {
            return workers[0];
        }
        int linkNum = 0;
        int start = workers.length % 2;
        SortThread linked[] = new SortThread[workers.length / 2 + start];
        if (start == 1) {
            linked[linkNum++] = workers[0];
        }
        for (int i = start; i < workers.length; i += 2) {
            workers[i].addChild(workers[i+1]);
            linked[linkNum++] = workers[i];
        }
        return link(linked);
    }

    private void addChild(SortThread worker) {
        children.add(worker);
    }

    private boolean hasChildren() {
        return !children.isEmpty();
    }

    private SortThread getChild() {
        synchronized (children) {
            return children.remove(0);
        }
    }

    @Override
    public void start() {
        synchronized (children) {
            children.forEach(SortThread::start);
        }
        super.start();
    }

    private int getWorkerId() {
        return id;
    }

    public <T> T getArray() {
        try {
            sorted.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (T) array;
    }

    @Override
    public void run() {
        sort();
        while (hasChildren()) {
            SortThread child = getChild();
            Object childArray = child.getArray();
            array = merge(concat(array, childArray));
            try {
                child.join();
            } catch (InterruptedException e) {
                throw new RuntimeException("Unexpected interrupt", e);
            }
        }
        sorted.release();
    }

    private void sort() {
        if (arrayClass.equals(long[].class)) {
            Arrays.sort((long[]) array);
        } else if (arrayClass.equals(int[].class)) {
            Arrays.sort((int[]) array);
        } else if (arrayClass.equals(short[].class)) {
            Arrays.sort((short[]) array);
        } else if (arrayClass.equals(byte[].class)) {
            Arrays.sort((byte[]) array);
        } else if (arrayClass.equals(float[].class)) {
            Arrays.sort((float[]) array);
        } else if (arrayClass.equals(double[].class)) {
            Arrays.sort((double[]) array);
        } else {
            Arrays.sort((Object[]) array);
        }
    }

    private Object concat(Object left, Object right) {
        if (arrayClass.equals(long[].class)) {
            this.middle = ((long[]) left).length - 1;
            long[] merged = ArrayUtils.concat((long[]) left, (long[]) right);
            this.right = merged.length - 1;
            return merged;
        } else if (arrayClass.equals(int[].class)) {
            this.middle = ((int[]) left).length - 1;
            int[] merged = ArrayUtils.concat((int[]) left, (int[]) right);
            this.right = merged.length - 1;
            return merged;
        } else if (arrayClass.equals(short[].class)) {
            this.middle = ((short[]) left).length - 1;
            short[] merged = ArrayUtils.concat((short[]) left, (short[]) right);
            this.right = merged.length - 1;
            return merged;
        } else if (arrayClass.equals(byte[].class)) {
            this.middle = ((byte[]) left).length - 1;
            byte[] merged = ArrayUtils.concat((byte[]) left, (byte[]) right);
            this.right = merged.length - 1;
            return merged;
        } else if (arrayClass.equals(float[].class)) {
            this.middle = ((float[]) left).length - 1;
            float[] merged = ArrayUtils.concat((float[]) left, (float[]) right);
            this.right = merged.length - 1;
            return merged;
        } else if (arrayClass.equals(double[].class)) {
            this.middle = ((double[]) left).length - 1;
            double[] merged = ArrayUtils.concat((double[]) left, (double[]) right);
            this.right = merged.length - 1;
            return merged;
        } else {
            this.middle = ((Object[]) left).length - 1;
            Object[] merged = ArrayUtils.concat((Object[]) left, (Object[]) right);
            this.right = merged.length - 1;
            return merged;
        }
    }

    private Object merge(Object array) {
        if (arrayClass.equals(long[].class)) {
            ArrayUtils.merge((long []) array, 0, middle, right);
        } else if (arrayClass.equals(byte[].class)) {
            ArrayUtils.merge((byte []) array, 0, middle, right);
        } else if (arrayClass.equals(int[].class)) {
            ArrayUtils.merge((int []) array, 0, middle, right);
        } else if (arrayClass.equals(short[].class)) {
            ArrayUtils.merge((short []) array, 0, middle, right);
        } else if (arrayClass.equals(float[].class)) {
            ArrayUtils.merge((float []) array, 0, middle, right);
        } else if (arrayClass.equals(double[].class)) {
            ArrayUtils.merge((double []) array, 0, middle, right);
        } else {
            ArrayUtils.merge((Object []) array, 0, middle, right);
        }
        return array;
    }
}
