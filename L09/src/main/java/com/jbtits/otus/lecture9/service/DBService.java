package com.jbtits.otus.lecture9.service;

import com.jbtits.otus.lecture9.entity.UserDataSet;

import java.sql.SQLException;

public interface DBService extends AutoCloseable {

    void prepareTables();

    void saveUser(UserDataSet user);

    UserDataSet getUserById(long id);

    void shutdown();
}
