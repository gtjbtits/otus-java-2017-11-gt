package com.jbtits.otus.lecture9.service;

import com.jbtits.otus.lecture9.entity.DataSet;
import com.jbtits.otus.lecture9.entity.UserDataSet;

import javax.xml.crypto.Data;
import java.sql.SQLException;

public interface DBService extends AutoCloseable {

    <T extends DataSet> void save(T dataSet);

    <T extends DataSet> T getById(long id, Class<T> clazz);

    void shutdown();
}
