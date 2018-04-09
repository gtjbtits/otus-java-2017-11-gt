package com.jbtits.otus.lecture15.messageContext.messages;

import com.jbtits.otus.lecture15.messageContext.MsgToDB;
import com.jbtits.otus.lecture15.dbService.DBService;
import com.jbtits.otus.lecture15.dataSets.UserDataSet;
import com.jbtits.otus.lecture15.front.webSocket.ErrorCode;
import com.jbtits.otus.lecture15.front.webSocket.WebSocketError;
import com.jbtits.otus.lecture15.front.webSocket.WebSocketErrorHandler;
import com.jbtits.otus.lecture15.messageSystem.Address;

/**
 * Created by tully.
 */
public class MsgSaveUser extends MsgToDB {
    private final String login;
    private final String password;

    public MsgSaveUser(Address from, Address to, String uuid, String sessionId, WebSocketErrorHandler errorHandler,
        String login, String password) {

        super(from, to, uuid, sessionId, errorHandler);
        this.login = login;
        this.password = password;
    }

    @Override
    public void exec(DBService dbService) {
        if (dbService.getUserByName(login) != null) {
            throw new WebSocketError(ErrorCode.DB_USER_ALREADY_EXISTS);
        }
        UserDataSet newUser = new UserDataSet();
        newUser.setName(login);
        newUser.setPassword(password);
        dbService.saveUser(newUser);
        UserDataSet user = dbService.getUserByName(login);
        dbService.getMS().sendMessage(new MsgSaveUserAnswer(getTo(), getFrom(), getUuid(), getSessionId(), getErrorHandler(), user.getId()));
    }
}
