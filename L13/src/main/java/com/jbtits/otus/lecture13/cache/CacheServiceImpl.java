package com.jbtits.otus.lecture13.cache;

import java.lang.ref.SoftReference;
import java.util.*;
import java.util.function.Function;

/**
 * Created by tully.
 */
public class CacheServiceImpl<K, V> implements CacheService<K, V> {
    private static final int TIME_THRESHOLD_MS = 5;

    private final int maxElements;
    private final long idleTimeMs;

    private final Map<K, SoftReference<MyElement<K, V>>> elements = new LinkedHashMap<>();
    private final Timer timer = new Timer();

    private int hit = 0;
    private int miss = 0;

    public CacheServiceImpl(int maxElements, long idleTimeMs) {
        this.maxElements = maxElements;
        this.idleTimeMs = idleTimeMs > 0 ? idleTimeMs : 0;
    }

    public void put(MyElement<K, V> element) {
        if (elements.size() == maxElements) {
            K firstKey = elements.keySet().iterator().next();
            elements.remove(firstKey);
        }

        K key = element.getKey();
        elements.put(key, new SoftReference<>(element));

        if (idleTimeMs != 0) {
            TimerTask idleTimerTask = getTimerTask(key, idleElement -> idleElement.getLastAccessTime() + idleTimeMs);
            timer.schedule(idleTimerTask, idleTimeMs, idleTimeMs);
        }
    }

    public MyElement<K, V> get(K key) {
        SoftReference<MyElement<K, V>> element = elements.get(key);
        if (element != null) {
            hit++;
            element.get().setAccessed();
        } else {
            miss++;
            return null;
        }
        return element.get();
    }

    public int getHitCount() {
        return hit;
    }

    public int getMissCount() {
        return miss;
    }

    @Override
    public int getHitPercentage() {
        return Math.round((float) getHitCount() / (getHitCount() + getMissCount()) * 100);
    }

    @Override
    public int getMissPercentage() {
        return Math.round((float) getMissCount() / (getHitCount() + getMissCount()) * 100);
    }

    @Override
    public int getMaxSize() {
        return maxElements;
    }

    @Override
    public long getIdleTimeout() {
        return idleTimeMs;
    }

    @Override
    public void shutdown() {
        timer.cancel();
    }

    private TimerTask getTimerTask(final K key, Function<MyElement<K, V>, Long> timeFunction) {
        return new TimerTask() {
            @Override
            public void run() {
                MyElement<K, V> element = elements.get(key).get();
                if (element == null || isT1BeforeT2(timeFunction.apply(element), System.currentTimeMillis())) {
                    elements.remove(key);
                    this.cancel();
                }
            }
        };
    }


    private boolean isT1BeforeT2(long t1, long t2) {
        return t1 < t2 + TIME_THRESHOLD_MS;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxElements, idleTimeMs, elements, timer, hit, miss);
    }
}
