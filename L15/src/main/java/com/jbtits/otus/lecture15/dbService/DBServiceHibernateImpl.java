package com.jbtits.otus.lecture15.dbService;

import com.jbtits.otus.lecture15.messageContext.MessageSystemContext;
import com.jbtits.otus.lecture15.cache.CacheService;
import com.jbtits.otus.lecture15.dataSets.*;
import com.jbtits.otus.lecture15.dbService.dao.MessageDataSetDAO;
import com.jbtits.otus.lecture15.dbService.dao.UserDataSetDAO;
import com.jbtits.otus.lecture15.dbService.executor.SessionExecutor;
import com.jbtits.otus.lecture15.messageSystem.Address;
import com.jbtits.otus.lecture15.messageSystem.MessageSystem;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.function.Function;

public class DBServiceHibernateImpl extends CacheableDBService implements DBService {
    private final SessionFactory sessionFactory;
    private SessionExecutor executor;
    private Address address;
    private MessageSystemContext context;

    public DBServiceHibernateImpl(CacheService cacheService, MessageSystemContext msContext, Address address) {
        super(cacheService);
        sessionFactory = createSessionFactory(getConfiguration());
        executor = new SessionExecutor(sessionFactory);
        this.address = address;
        this.context = msContext;
    }

    public void init() {
        context.getMessageSystem().addAddressee(this);
    }

    private Configuration getConfiguration() {
        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(UserDataSet.class);
        configuration.addAnnotatedClass(MessageDataSet.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:mem:hw10");
        configuration.setProperty("hibernate.connection.username", "sa");
        configuration.setProperty("hibernate.connection.password", "");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        configuration.setProperty("hibernate.connection.useSSL", "false");
        configuration.setProperty("hibernate.enable_lazy_load_no_trans", "true");

        return configuration;
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
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

    }

    @Override
    public void close() {
        try {
            sessionFactory.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMS() {
        return context.getMessageSystem();
    }
}
