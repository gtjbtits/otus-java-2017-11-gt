package com.jbtits.otus.lecture15.app.messages;

import com.jbtits.otus.lecture15.app.MsgToDB;
import com.jbtits.otus.lecture15.app.MsgToFrontend;
import com.jbtits.otus.lecture15.dataSets.MessageDataSet;
import com.jbtits.otus.lecture15.dbService.DBService;
import com.jbtits.otus.lecture15.front.FrontendService;
import com.jbtits.otus.lecture15.front.webSocket.WebSocketErrorHandler;
import com.jbtits.otus.lecture15.messageSystem.Address;

import java.util.Date;

/**
 * Created by tully.
 */
public class MsgBroadcastMessage extends MsgToFrontend {
    private final String message;
    private final String userName;
    private final Date created;

    public MsgBroadcastMessage(Address from, Address to, String uuid, String sessionId, WebSocketErrorHandler errorHandler,
                               String message, String userName, Date created) {

        super(from, to, uuid, sessionId, errorHandler);
        this.message = message;
        this.userName = userName;
        this.created = created;
    }

    @Override
    public void exec(FrontendService frontendService) {
        frontendService.broadcastMessageToClients(getUuid(), getSessionId(), message, userName, created);
    }
}
