package com.jbtits.otus.lecture9.service;

import com.jbtits.otus.lecture9.entity.DataSet;
import com.jbtits.otus.lecture9.entity.UserDataSet;

import java.sql.SQLException;
import java.util.List;

public interface DBService extends AutoCloseable {
    String getMetaData();

    void prepareTables() throws SQLException;

    void addUsers(String... names) throws SQLException;

    String getUserName(int id) throws SQLException;

    List<String> getAllNames() throws SQLException;

    List<UserDataSet> getAllUsers() throws SQLException;

    void deleteTables() throws SQLException;

    void saveUser(UserDataSet user) throws SQLException;

    UserDataSet getUserById(long id);

    void shutdown();
}
