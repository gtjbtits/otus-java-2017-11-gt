package com.jbtits.otus.lecture13.services;

import com.jbtits.otus.lecture13.dbService.DBService;
import com.jbtits.otus.lecture13.dbService.DBServiceHibernateImpl;

public class DBServiceSingleton {
    private static final DBService dbService = new DBServiceHibernateImpl(CacheServiceSingleton.get());

    public static DBService get() {
        return dbService;
    }
}
