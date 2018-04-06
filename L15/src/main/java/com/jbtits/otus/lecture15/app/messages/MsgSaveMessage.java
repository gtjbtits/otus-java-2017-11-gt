package com.jbtits.otus.lecture15.app.messages;

import com.jbtits.otus.lecture15.app.MsgToDB;
import com.jbtits.otus.lecture15.dataSets.MessageDataSet;
import com.jbtits.otus.lecture15.dataSets.UserDataSet;
import com.jbtits.otus.lecture15.dbService.DBService;
import com.jbtits.otus.lecture15.front.webSocket.ErrorCode;
import com.jbtits.otus.lecture15.front.webSocket.WebSocketError;
import com.jbtits.otus.lecture15.front.webSocket.WebSocketErrorHandler;
import com.jbtits.otus.lecture15.messageSystem.Address;

/**
 * Created by tully.
 */
public class MsgSaveMessage extends MsgToDB {
    private final String message;
    private final long userId;

    public MsgSaveMessage(Address from, Address to, String uuid, String sessionId, WebSocketErrorHandler errorHandler,
          String message, long userId) {

        super(from, to, uuid, sessionId, errorHandler);
        this.message = message;
        this.userId = userId;
    }

    @Override
    public void exec(DBService dbService) {
        MessageDataSet messageDataSet = new MessageDataSet();
        messageDataSet.setText(message);
        dbService.saveMessage(messageDataSet, userId);
        dbService.getMS().sendMessage(new MsgBroadcastMessage(getTo(), getFrom(), getUuid(), getSessionId(),
            getErrorHandler(), messageDataSet.getText(), messageDataSet.getUser().getName(), messageDataSet.getCreated()));
    }
}
