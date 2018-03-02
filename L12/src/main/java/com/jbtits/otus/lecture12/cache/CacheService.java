package com.jbtits.otus.lecture12.cache;

/**
 * Created by tully.
 */
public interface CacheService<K, V> {

    void put(MyElement<K, V> element);

    MyElement<K, V> get(K key);

    int getHitCount();

    int getMissCount();

    int getHitPercentage();

    int getMissPercentage();

    int getMaxSize();

    long getIdleTimeout();

    void shutdown();
}
