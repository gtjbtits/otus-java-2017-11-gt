package com.jbtits.otus.lecture15.config;

import com.jbtits.otus.lecture15.front.FrontendService;
import com.jbtits.otus.lecture15.messageContext.MessageSystemContext;
import com.jbtits.otus.lecture15.cache.CacheService;
import com.jbtits.otus.lecture15.cache.CacheServiceImpl;
import com.jbtits.otus.lecture15.dbService.DBService;
import com.jbtits.otus.lecture15.dbService.DBServiceHibernateImpl;
import com.jbtits.otus.lecture15.dataSets.DataSet;
import com.jbtits.otus.lecture15.front.FrontendServiceImpl;
import com.jbtits.otus.lecture15.front.SecurityService;
import com.jbtits.otus.lecture15.front.SecurityServiceImpl;
import com.jbtits.otus.lecture15.front.webSocket.WebSocketMessageMapper;
import com.jbtits.otus.lecture15.front.webSocket.WebSocketMessageMapperImpl;
import com.jbtits.otus.lecture15.front.webSocket.WebSocketSessionsRegistry;
import com.jbtits.otus.lecture15.front.webSocket.WebSocketSessionsRegistryImpl;
import com.jbtits.otus.lecture15.messageSystem.Address;
import com.jbtits.otus.lecture15.messageSystem.MessageSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.PostConstruct;

@Configuration
@EnableWebSocket
@ComponentScan({"com.jbtits.otus.lecture15"})
public class AppConfiguration implements WebSocketConfigurer {
    @PostConstruct
    public void startMessageSystem() {
        frontendService().init();
        dbService1().init();
        dbService2().init();
        messageSystem().start();
    }

    @Bean
    public MessageSystem messageSystem() {
        return new MessageSystem();
    }

    @Bean
    public Address frontAddress() {
        return new Address("front");
    }

    @Bean
    public Address db1Address() {
        return new Address("db1");
    }

    @Bean
    public Address db2Address() {
        return new Address("db2");
    }

    @Bean
    public MessageSystemContext messageSystemContext() {
        MessageSystemContext context = new MessageSystemContext(messageSystem());
        context.setFrontAddress(frontAddress());
        context.setDbAddress1(db1Address());
        context.setDbAddress2(db2Address());
        return context;
    }

    @Bean
    public CacheService<String, DataSet> cacheService() {
        return new CacheServiceImpl<>(10,1000);
    }

    @Bean
    public DBService dbService1() {
        return new DBServiceHibernateImpl(cacheService(), messageSystemContext(), db1Address());
    }

    @Bean
    public DBService dbService2() {
        return new DBServiceHibernateImpl(cacheService(), messageSystemContext(), db2Address());
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(frontendService(), "/ws");
    }

    @Bean
    public WebSocketMessageMapper webSocketMessageMapper() {
        return new WebSocketMessageMapperImpl();
    }

    @Bean
    public WebSocketSessionsRegistry webSocketSessionsRegistry() {
        return new WebSocketSessionsRegistryImpl();
    }

    @Bean
    public SecurityService securityService() {
        return new SecurityServiceImpl("OTUS_Java");
    }

    @Bean
    public FrontendService frontendService() {
        return new FrontendServiceImpl(messageSystemContext(), frontAddress(), webSocketSessionsRegistry(),
            webSocketMessageMapper(), securityService());
    }
}
