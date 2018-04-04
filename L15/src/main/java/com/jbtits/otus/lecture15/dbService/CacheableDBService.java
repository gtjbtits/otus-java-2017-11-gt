package com.jbtits.otus.lecture15.dbService;

import com.jbtits.otus.lecture15.cache.CacheService;
import com.jbtits.otus.lecture15.cache.MyElement;
import com.jbtits.otus.lecture15.dataSets.DataSet;

abstract public class CacheableDBService {
    private final CacheService<String, DataSet> cacheService;

    CacheableDBService(CacheService<String, DataSet> cacheEngine) {
        this.cacheService = cacheEngine;
    }

    private String encodeCacheId(Object key, Class<?> clazz) {
        return clazz.getName() + "_" + key;
    }

    protected void cachePut(Object key, DataSet dataSet) {
        if (cacheService == null || key == null || dataSet == null) {
            return;
        }
        cacheService.put(new MyElement<>(encodeCacheId(key, dataSet.getClass()), dataSet));
    }

    protected DataSet cacheGet(Object key, Class<?> clazz) {
        if (cacheService == null || key == null) {
            return null;
        }
        MyElement<String, DataSet> element = cacheService.get(encodeCacheId(key, clazz));
        if (element == null) {
            return null;
        }
        return element.getValue();
    }
}
