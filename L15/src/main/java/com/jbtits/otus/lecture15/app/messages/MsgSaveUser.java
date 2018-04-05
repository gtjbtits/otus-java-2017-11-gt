package com.jbtits.otus.lecture15.app.messages;

import com.jbtits.otus.lecture15.app.MsgToDB;
import com.jbtits.otus.lecture15.dbService.DBService;
import com.jbtits.otus.lecture15.dataSets.UserDataSet;
import com.jbtits.otus.lecture15.messageSystem.Address;

/**
 * Created by tully.
 */
public class MsgSaveUser extends MsgToDB {
    private final String login;
    private final String password;

    public MsgSaveUser(Address from, Address to, String sessionId, String login, String password) {
        super(from, to, sessionId);
        this.login = login;
        this.password = password;
    }

    @Override
    public void exec(DBService dbService) {
        UserDataSet newUser = new UserDataSet();
        newUser.setName(login);
        newUser.setPassword(password);
        dbService.saveUser(newUser);
        UserDataSet user = dbService.getUserByName(login);
        dbService.getMS().sendMessage(new MsgSaveUserAnswer(getTo(), getFrom(), getSessionId(), user));
    }
}
