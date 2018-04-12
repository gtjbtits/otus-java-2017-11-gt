package com.jbtits.otus.lecture16.db.dbService;

import com.jbtits.otus.lecture16.db.ClientMain;
import com.jbtits.otus.lecture16.db.cache.CacheService;
import com.jbtits.otus.lecture16.db.dbService.dao.MessageDataSetDAO;
import com.jbtits.otus.lecture16.db.dbService.dao.UserDataSetDAO;
import com.jbtits.otus.lecture16.db.dbService.executor.SessionExecutor;
import com.jbtits.otus.lecture16.ms.dataSets.UserDataSet;
import com.jbtits.otus.lecture16.ms.dataSets.DataSet;
import com.jbtits.otus.lecture16.ms.dataSets.MessageDataSet;
import com.jbtits.otus.lecture16.ms.utils.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.function.Function;

public class DBServiceHibernateImpl extends CacheableDBService implements DBService {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(DBServiceHibernateImpl.class.getName());

    private final SessionFactory sessionFactory;
    private SessionExecutor executor;

    public DBServiceHibernateImpl(CacheService cacheService) {
        super(cacheService);
        try {
            sessionFactory = createSessionFactory(getConfiguration());
        } catch (HibernateException e) {
            super.shutdown();
            throw ExceptionUtils.fatalError("Hibernate misconfigurated", e);
        }
        executor = new SessionExecutor(sessionFactory);
    }

    private Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UserDataSet.class);
        configuration.addAnnotatedClass(MessageDataSet.class);
        return configuration;
    }

    private static SessionFactory createSessionFactory(Configuration configuration) throws HibernateException {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    @Override
    public void init() {

    }

    @Override
    public void saveUser(UserDataSet user) {
        save(user, session -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            return dao.save(user);
        });
    }

    @Override
    public void saveMessage(MessageDataSet message, long userId) {
        save(message, session -> {
            MessageDataSetDAO messageDAO = new MessageDataSetDAO(session);
            return messageDAO.save(message, userId);
        });
    }

    private <T extends DataSet> void save(T dataSet, Function<Session, Long> callDAO) {
        long id = executor.runInSession(callDAO);
        dataSet.setId(id);
        cachePut(id, dataSet);
    }

    @Override
    public UserDataSet getUserById(long id) {
        UserDataSet cached = (UserDataSet) cacheGet(id, UserDataSet.class);
        if (cached != null) {
            return cached;
        }
        return executor.runInSession(session -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            UserDataSet user = dao.read(id);
            cachePut(id, user);
            return user;
        });
    }

    @Override
    public UserDataSet getUserByName(String name) {
        UserDataSet cached = (UserDataSet) cacheGet(name, UserDataSet.class);
        if (cached != null) {
            return cached;
        }
        return executor.runInSession(session -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            UserDataSet user = dao.readByName(name);
            cachePut(name, user);
            return user;
        });
    }

    @Override
    public void shutdown() {
        super.shutdown();
        try {
            sessionFactory.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
