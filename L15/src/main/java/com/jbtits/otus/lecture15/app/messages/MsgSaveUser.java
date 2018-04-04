package com.jbtits.otus.lecture15.app.messages;

import com.jbtits.otus.lecture15.app.MsgToDB;
import com.jbtits.otus.lecture15.dbService.DBService;
import com.jbtits.otus.lecture15.dataSets.UserDataSet;
import com.jbtits.otus.lecture15.messageSystem.Address;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by tully.
 */
public class MsgSaveUser extends MsgToDB {
    private final String login;
    private final String password;
    private final WebSocketSession session;

    public MsgSaveUser(Address from, Address to, String login, String password, WebSocketSession session) {
        super(from, to);
        this.login = login;
        this.password = password;
        this.session = session;
    }

    @Override
    public void exec(DBService dbService) {
        UserDataSet newUser = new UserDataSet();
        newUser.setName(login);
        newUser.setPassword(password); // TODO: encode password
        dbService.saveUser(newUser);
        UserDataSet user = dbService.getUserByName(login);
        dbService.getMS().sendMessage(new MsgSaveUserAnswer(getTo(), getFrom(), user, session));
    }
}
