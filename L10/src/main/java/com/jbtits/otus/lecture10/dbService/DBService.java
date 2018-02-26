package com.jbtits.otus.lecture10.dbService;

import com.jbtits.otus.lecture10.dataSets.UserDataSet;

public interface DBService extends AutoCloseable {

    void saveUser(UserDataSet user);

    UserDataSet getUserById(long id);

    void shutdown();
}

