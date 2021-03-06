package com.jbtits.otus.lecture15.front;

import com.jbtits.otus.lecture15.dataSets.UserDataSet;
import com.jbtits.otus.lecture15.messageSystem.Addressee;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;

import java.util.Date;

/**
 * Created by tully.
 */
public interface FrontendService extends Addressee, WebSocketHandler {
    void init();

    void registerUser(long userId, String uuid, String sessionId);

    void broadcastMessageToClients(String uuid, String sessionId, String message, String userName, Date created);
}

