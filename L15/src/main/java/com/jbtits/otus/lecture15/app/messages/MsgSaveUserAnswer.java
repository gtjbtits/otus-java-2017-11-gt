package com.jbtits.otus.lecture15.app.messages;

import com.jbtits.otus.lecture15.front.FrontendService;
import com.jbtits.otus.lecture15.app.MsgToFrontend;
import com.jbtits.otus.lecture15.dataSets.UserDataSet;
import com.jbtits.otus.lecture15.front.webSocket.WebSocketErrorHandler;
import com.jbtits.otus.lecture15.messageSystem.Address;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by tully.
 */
public class MsgSaveUserAnswer extends MsgToFrontend {
    private final long userId;

    public MsgSaveUserAnswer(Address from, Address to, String uuid, String sessionId, WebSocketErrorHandler errorHandler, long userId) {
        super(from, to, uuid, sessionId, errorHandler);
        this.userId = userId;
    }

    @Override
    public void exec(FrontendService frontendService) {
        frontendService.addUser(userId, getUuid(), getSessionId());
    }
}
