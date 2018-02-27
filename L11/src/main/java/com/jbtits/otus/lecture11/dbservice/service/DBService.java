package com.jbtits.otus.lecture11.dbservice.service;

import com.jbtits.otus.lecture11.dbservice.entity.UserDataSet;

public interface DBService extends AutoCloseable {

    void saveUser(UserDataSet user);

    UserDataSet getUserById(long id);

    void shutdown();
}
