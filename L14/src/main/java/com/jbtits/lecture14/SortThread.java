package com.jbtits.lecture14;

import com.jbtits.lecture14.utils.ArrayUtils;
import com.jbtits.lecture14.utils.MathHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

public class SortThread extends Thread {
    private int id;
    private boolean actual;
    private boolean sorted;
    private boolean isBroadcastReady;
    private boolean partitioned;
    private Semaphore running;
    private Semaphore broadcastSem;
    private Semaphore broadcastSetSem;
    private SortControlThread control;
    private Number[] arrayPart;
    private Number[] samples;
    private List<List<Number>> partitions;

    SortThread() {
        actual = true;
        sorted = false;
        partitioned = false;
        isBroadcastReady = false;
        running = new Semaphore(0);
        broadcastSem = new Semaphore(0);
        broadcastSetSem = new Semaphore(0);
        partitions = new LinkedList<>();
    }

    SortThread(SortControlThread control, int id) {
        this();
        this.id = id;
        this.control = control;
    }

    @Override
    public void run() {
        while(isActual()) {
            if (arrayPart != null && !sorted) {
                sortArrayPart();
                setSamples();
                getControl().freeWorker();
            } else if (getControl().getPivots()[0] != null && !partitioned) {
                sortPartitions();
                broadcast();
//                System.out.println(getName() + " parts" + partitions);
                mergePartitions();
//                System.out.println(getName() + ": ______ " + Arrays.asList(arrayPart));
                getControl().freeWorker();
            }
            safeSleep(10);
        }
        running.release();
    }

    protected void kill() {
        actual = false;
        acquire(running);
    }

    protected boolean isActual() {
        return actual;
    }

    protected void acquire(Semaphore semaphore) {
        acquire(semaphore, 1);
    }

    protected void acquire(Semaphore semaphore, int permits) {
        try {
            semaphore.acquire(permits);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected void safeSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected void setArrayPart(Number[] arrayPart) {
        this.arrayPart = arrayPart;
        sorted = false;
    }

    public Number[] getArrayPart() {
        return arrayPart;
    }

    protected SortControlThread getControl() {
        return control;
    }

    private void sortArrayPart() {
        Arrays.sort(arrayPart);
        sorted = true;
    }

    private void sortPartitions() {
        Number pivots[] = getControl().getPivots();
        int workers = getControl().getWorkersCount();
        // TODO: list:utils?
        for (int i = 0; i < workers; i++) {
            partitions.add(new LinkedList<>());
        }
        for (Number anArrayPart : arrayPart) {
            if (MathHelper.compare(anArrayPart, pivots[0]) < 0) {
                partitions.get(0).add(anArrayPart);
            } else if (MathHelper.compare(anArrayPart, pivots[pivots.length - 1]) >= 0) {
                partitions.get(workers - 1).add(anArrayPart);
            } else {
                for (int worker = 1; worker < workers - 1; worker++) {
                    if (MathHelper.compare(anArrayPart, pivots[worker - 1]) >= 0 && MathHelper.compare(anArrayPart, pivots[worker]) < 0) {
                        partitions.get(worker).add(anArrayPart);
                    }
                }
            }
        }
        partitions.forEach(partition -> partition.sort(MathHelper.comparator()));

        int pLength = partitions.stream().mapToInt(p -> p.size()).reduce(Integer::sum).orElseThrow(NullPointerException::new);
        System.out.println(getName() + " partitions length (sort stage)" + pLength);

        partitioned = true;
    }

    private void broadcast() {
        List<List<Number>> pool = prepareBroadcastPool();
        broadcastSem.release();
        for (int i = 0; i < getControl().getWorkersCount(); i++) {
            if (i != id) {
                getControl().sendPartition(getWorkerId(), i, pool.get(i));
            }
        }
        acquire(broadcastSetSem,getControl().getWorkersCount() - 1);

        int pLength = partitions.stream().mapToInt(p -> p.size()).reduce(Integer::sum).orElseThrow(NullPointerException::new);
        System.out.println(getName() + " partitions length (broadcast stage)" + pLength);
    }

    public synchronized void setPartition(int id, List<Number> partition) {
        if (!isBroadcastReady) {
            acquire(broadcastSem);
            isBroadcastReady = true;
        }
        partitions.set(id, partition);
        broadcastSetSem.release();
    }

    private void mergePartitions() {
        arrayPart = ArrayUtils.mergeMultisortList(partitions);
        System.out.println(getName() + " merged length" + arrayPart.length);
    }

    private List<List<Number>> prepareBroadcastPool() {
        List<List<Number>> pool = new LinkedList<>();
        for (int i = 0; i < getControl().getWorkersCount(); i++) {
            pool.add(null);
        }
        for (int i = 0; i < getControl().getWorkersCount(); i++) {
            if (i != id) {
                pool.set(i, new LinkedList<>(partitions.get(i)));
            }
        }
        return pool;
    }

    private void setSamples() {
        if (arrayPart == null) {
            throw new NullPointerException("Can\'t generate samples: arrayPart[] is not ready yet");
        }
        int samplesIdx[] = getControl().getSamplesIdx();
        if (samplesIdx == null) {
            throw new NullPointerException("Can\'t get samplesIdx from control thread");
        }
        samples = new Number[samplesIdx.length];
        for (int i = 0; i < samples.length; i++) {
            samples[i] = arrayPart[samplesIdx[i]];
        }
        Arrays.sort(samples);
    }

    public Number[] getSamples() {
        return samples;
    }

    protected int getWorkerId() {
        return id;
    }
}
