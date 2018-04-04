package com.jbtits.otus.lecture15.app.messages;

import com.jbtits.otus.lecture15.front.FrontendService;
import com.jbtits.otus.lecture15.app.MsgToFrontend;
import com.jbtits.otus.lecture15.dataSets.UserDataSet;
import com.jbtits.otus.lecture15.messageSystem.Address;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by tully.
 */
public class MsgSaveUserAnswer extends MsgToFrontend {
    private final UserDataSet user;
    private final WebSocketSession session;

    public MsgSaveUserAnswer(Address from, Address to, UserDataSet user, WebSocketSession session) {
        super(from, to);
        this.user = user;
        this.session = session;
    }

    @Override
    public void exec(FrontendService frontendService) {
        frontendService.addUser(user, session);
    }
}
