package com.jbtits.otus.lecture13.services;

import com.jbtits.otus.lecture13.cache.CacheService;
import com.jbtits.otus.lecture13.cache.CacheServiceImpl;
import com.jbtits.otus.lecture13.dbService.dataSets.DataSet;

public class CacheServiceSingleton {
    private final static CacheService<String, DataSet> cacheService = new CacheServiceImpl<>(10, 1000);

    public static CacheService<String, DataSet> get() {
        return cacheService;
    }
}
