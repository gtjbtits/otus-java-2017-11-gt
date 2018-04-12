package com.jbtits.otus.lecture16.db.dbService;

import com.jbtits.otus.lecture16.ms.dataSets.UserDataSet;
import com.jbtits.otus.lecture16.ms.dataSets.DataSet;
import com.jbtits.otus.lecture16.ms.dataSets.MessageDataSet;

public interface DBService {

    public void init();

    void saveUser(UserDataSet user);

    void saveMessage(MessageDataSet message, long userId);

    UserDataSet getUserById(long id);

    UserDataSet getUserByName(String name);

    void shutdown();
}

