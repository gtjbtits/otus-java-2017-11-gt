package com.jbtits.otus.lecture15.app;

import com.jbtits.otus.lecture15.app.messages.SessionMessage;
import com.jbtits.otus.lecture15.dbService.DBService;
import com.jbtits.otus.lecture15.front.FrontendService;
import com.jbtits.otus.lecture15.front.webSocket.ErrorCode;
import com.jbtits.otus.lecture15.front.webSocket.WebSocketError;
import com.jbtits.otus.lecture15.front.webSocket.WebSocketErrorHandler;
import com.jbtits.otus.lecture15.messageSystem.Address;
import com.jbtits.otus.lecture15.messageSystem.Addressee;

/**
 * Created by tully.
 */
public abstract class MsgToDB extends SessionMessage {
    public MsgToDB(Address from, Address to, String uuid, String sessionId, WebSocketErrorHandler errorHandler) {
        super(from, to, uuid, sessionId, errorHandler);
    }

    @Override
    public void exec(Addressee addressee) {
        try {
            if (!(addressee instanceof DBService)) {
                throw new ClassCastException("Addressee is not a DBService instance");
            }
            exec((DBService) addressee);
        } catch (WebSocketError e) {
            getErrorHandler().handleException(e, getSessionId(), getUuid());
        } catch (Exception e) {
            getErrorHandler().handleException(new WebSocketError(ErrorCode.MESSAGE_SYSTEM_ERROR, e), getSessionId(), getUuid());
        }
    }

    public abstract void exec(DBService dbService);
}
