package com.jbtits.otus.lecture10.hibernate.dao;

import com.jbtits.otus.lecture10.dataSets.UserDataSet;
import org.hibernate.Session;

public class UserDataSetDAO {
    private Session session;

    public UserDataSetDAO(Session session) {
        this.session = session;
    }

    public void save(UserDataSet dataSet) {
        session.save(dataSet);
    }

    public UserDataSet read(long id) {
        return session.load(UserDataSet.class, id);
    }
}
