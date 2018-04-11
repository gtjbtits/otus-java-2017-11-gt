package com.jbtits.otus.lecture16.frontend;

import org.springframework.web.socket.WebSocketHandler;

import java.util.Date;

/**
 * Created by tully.
 */
public interface FrontendService extends WebSocketHandler {
    void init();

    void registerUser(long userId, String uuid);

    void broadcastMessageToClients(String uuid, String sessionId, String message, String userName, Date created);
}

