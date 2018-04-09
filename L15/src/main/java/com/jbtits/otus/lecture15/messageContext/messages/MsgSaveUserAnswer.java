package com.jbtits.otus.lecture15.messageContext.messages;

import com.jbtits.otus.lecture15.front.FrontendService;
import com.jbtits.otus.lecture15.messageContext.MsgToFrontend;
import com.jbtits.otus.lecture15.front.webSocket.WebSocketErrorHandler;
import com.jbtits.otus.lecture15.messageSystem.Address;

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
        frontendService.registerUser(userId, getUuid(), getSessionId());
    }
}
