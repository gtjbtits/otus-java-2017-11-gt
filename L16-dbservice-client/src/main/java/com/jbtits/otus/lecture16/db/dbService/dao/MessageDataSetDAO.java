package com.jbtits.otus.lecture16.db.dbService.dao;

import com.jbtits.otus.lecture16.ms.dataSets.UserDataSet;
import com.jbtits.otus.lecture16.ms.dataSets.DataSet;
import com.jbtits.otus.lecture16.ms.dataSets.MessageDataSet;
import org.hibernate.Session;

public class MessageDataSetDAO {
    private Session session;

    public MessageDataSetDAO(Session session) {
        this.session = session;
    }

    public long save(MessageDataSet dataSet, long userId) {
        UserDataSetDAO userDAO = new UserDataSetDAO(session);
        UserDataSet user = userDAO.read(userId);
        dataSet.setUser(user);
        return (Long) session.save(dataSet);
    }

    public MessageDataSet read(long id) {
        return session.load(MessageDataSet.class, id);
    }
}
