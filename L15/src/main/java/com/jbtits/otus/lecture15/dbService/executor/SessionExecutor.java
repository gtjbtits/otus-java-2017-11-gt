package com.jbtits.otus.lecture15.dbService.executor;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.function.Consumer;
import java.util.function.Function;

public class SessionExecutor {
    private SessionFactory sessionFactory;

    public SessionExecutor(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public <R> R runInSession(Function<Session, R> function) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            R result = function.apply(session);
            transaction.commit();
            return result;
        }
    }

    public void runInSession(Consumer<Session> consumer) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            consumer.accept(session);
            transaction.commit();
        }
    }
}
