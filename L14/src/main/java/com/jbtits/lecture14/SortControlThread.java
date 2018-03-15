package com.jbtits.lecture14;

import com.jbtits.lecture14.utils.ArrayUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SortControlThread extends SortThread {
    private SortThread workers[];
    private int workersCount;
    private Semaphore freeWorkers;
    private int samplesIdx[];
    private Number pivots[];

    SortControlThread(int workersCount) {
        this.workersCount = workersCount;
        samplesIdx = new int[workersCount];
        pivots = new Number[workersCount - 1];
        freeWorkers = new Semaphore(0);
        workers = new SortThread[workersCount];
        workers[0] = this;
        for (int i = 1; i < workersCount; i++) {
            workers[i] = new SortThread(this, i);
            workers[i].start();
        }
    }

    @Override
    protected void kill() {
        for (int i = 1; i < workersCount; i++) {
            workers[i].kill();
        }
        super.kill();
    }

    @Override
    protected SortControlThread getControl() {
        return this;
    }

    public void sort(Number[] source) {
        setSamplesIdx(source.length);
        // Если threadCount > chunks - будет ошибка (дедлок)
        // Надо завязываться не на просто количество тредов, а на количество тредов в работе
        ArrayUtils.handleByChunks(source, workersCount, (part, i) -> workers[i].setArrayPart((Number[]) part));
        acquire(freeWorkers, workersCount);
        setPivots();
        acquire(freeWorkers, workersCount);
        combineSortedParts(source);
    }

    private Number[] getPivots(Number[] samples) {
        Number[] pivots = new Number[workersCount - 1];
        for (int i = 0; i < pivots.length; i++) {
            int idx = (i + 1) * workersCount + workersCount / 2 - 1;
            pivots[i] = samples[idx];
        }
        return pivots;
    }

    private List<Number[]> collectArrays(Function<SortThread, Number[]> callback) {
        List<Number[]> parts = new LinkedList<>();
        for (SortThread worker : workers) {
            parts.add(callback.apply(worker));
        }
        return parts;
    }

    public void freeWorker() {
        freeWorkers.release();
    }

    private void setSamplesIdx(int length) {
        for (int i = 0; i < samplesIdx.length; i++) {
            samplesIdx[i] = i * length / (workersCount * workersCount);
        }
    }

    public int[] getSamplesIdx() {
        return samplesIdx;
    }

    private void setPivots() {
        Number[] samples = ArrayUtils.mergeMultisort(collectArrays(SortThread::getSamples));
        pivots = getPivots(samples);
//        System.out.println("samples: " + Arrays.asList(samples));
//        System.out.println("pivots: " + Arrays.asList(pivots));
    }

    public Number[] getPivots() {
        return pivots;
    }

    public int getWorkersCount() {
        return workersCount;
    }

    public synchronized void send(int i, Consumer<SortThread> callback) {
        callback.accept(workers[i]);
    }

    public void sendPartition(int sender, int receiver, List<Number> partition) {
        send(receiver, w -> workers[receiver].setPartition(sender, partition));
//        send(receiver, w -> System.out.println("send: from=" + sender + ", to=" + receiver + ", data=" + partition));
    }

    public void combineSortedParts(Number[] dest) {
        int size = 0;
        for (int i = 0; i < workersCount; i++) {
            Number part[] = workers[i].getArrayPart();
            System.arraycopy(part, 0, dest, size, part.length);
            size += part.length;
        }
    }
}
