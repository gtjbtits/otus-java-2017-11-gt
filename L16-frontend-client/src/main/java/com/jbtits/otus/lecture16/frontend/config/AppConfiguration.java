package com.jbtits.otus.lecture16.frontend.config;
import com.jbtits.otus.lecture16.frontend.FrontendService;
import com.jbtits.otus.lecture16.frontend.FrontendServiceImpl;
import com.jbtits.otus.lecture16.frontend.SecurityService;
import com.jbtits.otus.lecture16.frontend.SecurityServiceImpl;
import com.jbtits.otus.lecture16.frontend.webSocket.WebSocketMessageMapper;
import com.jbtits.otus.lecture16.frontend.webSocket.WebSocketMessageMapperImpl;
import com.jbtits.otus.lecture16.frontend.webSocket.WebSocketSessionsRegistry;
import com.jbtits.otus.lecture16.frontend.webSocket.WebSocketSessionsRegistryImpl;
import com.jbtits.otus.lecture16.ms.channel.ClientSocketMsgWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
@EnableWebSocket
@ComponentScan({"com.jbtits.otus.lecture16.frontend"})
public class AppConfiguration implements WebSocketConfigurer {
    @PostConstruct
    public void startMessageSystem() {
        frontendService().init();
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
        return new FrontendServiceImpl(webSocketSessionsRegistry(), webSocketMessageMapper(), securityService(), clientSocketMsgWorker());
    }

    @Bean
    public ClientSocketMsgWorker clientSocketMsgWorker() {
        try {
            return new ClientSocketMsgWorker("localhost", 5050);
        } catch (IOException e) {
            throw new RuntimeException("Unable to connect to server");
        }
    }
}
