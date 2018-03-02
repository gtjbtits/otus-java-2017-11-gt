package com.jbtits.otus.lecture12.dbService;

import com.jbtits.otus.lecture12.dbService.dataSets.UserDataSet;

public interface DBService extends AutoCloseable {

    void saveUser(UserDataSet user);

    UserDataSet getUserById(long id);

    UserDataSet getUserByName(String name);

    void shutdown();
}

