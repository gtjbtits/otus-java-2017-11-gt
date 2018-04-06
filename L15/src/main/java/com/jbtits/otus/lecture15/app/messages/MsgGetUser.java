package com.jbtits.otus.lecture15.app.messages;

import com.jbtits.otus.lecture15.app.MsgToDB;
import com.jbtits.otus.lecture15.dataSets.UserDataSet;
import com.jbtits.otus.lecture15.dbService.DBService;
import com.jbtits.otus.lecture15.front.webSocket.ErrorCode;
import com.jbtits.otus.lecture15.front.webSocket.WebSocketError;
import com.jbtits.otus.lecture15.front.webSocket.WebSocketErrorHandler;
import com.jbtits.otus.lecture15.messageSystem.Address;

/**
 * Created by tully.
 */
public class MsgGetUser extends MsgToDB {
    private final String login;
    private final String password;

    public MsgGetUser(Address from, Address to, String uuid, String sessionId, WebSocketErrorHandler errorHandler,
                      String login, String password) {

        super(from, to, uuid, sessionId, errorHandler);
        this.login = login;
        this.password = password;
    }

    @Override
    public void exec(DBService dbService) {
        UserDataSet user = dbService.getUserByName(login);
        if (user == null) {
            throw new WebSocketError(ErrorCode.DB_USER_NOT_FOUND);
        }
        if (!user.getPassword().equals(password)) {
            throw new WebSocketError(ErrorCode.DB_USER_PASSWORD_MISMATCH);
        }
        dbService.getMS().sendMessage(new MsgUserIdAnswer(getTo(), getFrom(), getUuid(), getSessionId(), getErrorHandler(), user.getId()));
    }
}
