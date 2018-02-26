package com.jbtits.otus.lecture9.service;

import com.jbtits.otus.lecture9.entity.DataSet;
import com.jbtits.otus.lecture9.entity.UserDataSet;

import javax.xml.crypto.Data;
import java.sql.SQLException;

public interface DBService extends AutoCloseable {

    void saveUser(UserDataSet user);

    UserDataSet getUserById(long id);

    void shutdown();
}
