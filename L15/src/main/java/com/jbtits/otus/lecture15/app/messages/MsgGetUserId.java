package com.jbtits.otus.lecture15.app.messages;

import com.jbtits.otus.lecture15.app.MsgToDB;
import com.jbtits.otus.lecture15.dbService.DBService;
import com.jbtits.otus.lecture15.dataSets.UserDataSet;
import com.jbtits.otus.lecture15.messageSystem.Address;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by tully.
 */
public class MsgGetUserId extends MsgToDB {
    private final String login;
    private final WebSocketSession session;

    public MsgGetUserId(Address from, Address to, String login, WebSocketSession session) {
        super(from, to);
        this.login = login;
        this.session = session;
    }

    @Override
    public void exec(DBService dbService) {
        UserDataSet user = dbService.getUserByName(login);
        long id;
        if (user == null) {
            UserDataSet newUser = new UserDataSet();
            newUser.setName(login);
            dbService.saveUser(newUser);
            id = newUser.getId();
        } else {
            id = user.getId();
        }
        dbService.getMS().sendMessage(new MsgGetUserIdAnswer(getTo(), getFrom(), login, id, session));
    }
}
