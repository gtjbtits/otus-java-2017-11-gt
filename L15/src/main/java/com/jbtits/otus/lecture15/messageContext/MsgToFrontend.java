package com.jbtits.otus.lecture15.messageContext;

import com.jbtits.otus.lecture15.messageContext.messages.SessionMessage;
import com.jbtits.otus.lecture15.front.FrontendService;
import com.jbtits.otus.lecture15.front.webSocket.ErrorCode;
import com.jbtits.otus.lecture15.front.webSocket.WebSocketError;
import com.jbtits.otus.lecture15.front.webSocket.WebSocketErrorHandler;
import com.jbtits.otus.lecture15.messageSystem.Address;
import com.jbtits.otus.lecture15.messageSystem.Addressee;

/**
 * Created by tully.
 */
public abstract class MsgToFrontend extends SessionMessage {
    public MsgToFrontend(Address from, Address to, String uuid, String sessionId, WebSocketErrorHandler errorHandler) {
        super(from, to, uuid, sessionId, errorHandler);
    }

    @Override
    public void exec(Addressee addressee) {
        try {
            if (!(addressee instanceof FrontendService)) {
                throw new ClassCastException("Addressee is not a FrontService instance");
            }
            exec((FrontendService) addressee);
        } catch (WebSocketError e) {
            getErrorHandler().handleException(e, getSessionId(), getUuid());
        } catch (Exception e) {
            getErrorHandler().handleException(new WebSocketError(ErrorCode.MESSAGE_SYSTEM_ERROR, e), getSessionId(), getUuid());
        }
    }

    public abstract void exec(FrontendService frontendService);
}