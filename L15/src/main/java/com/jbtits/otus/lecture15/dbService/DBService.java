package com.jbtits.otus.lecture15.dbService;

import com.jbtits.otus.lecture15.dataSets.UserDataSet;
import com.jbtits.otus.lecture15.messageSystem.Addressee;

public interface DBService extends AutoCloseable, Addressee {

    public void init();

    void saveUser(UserDataSet user);

    UserDataSet getUserById(long id);

    UserDataSet getUserByName(String name);

    void shutdown();
}

