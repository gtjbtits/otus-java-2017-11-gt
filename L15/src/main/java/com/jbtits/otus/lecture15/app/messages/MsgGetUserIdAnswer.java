package com.jbtits.otus.lecture15.app.messages;

import com.jbtits.otus.lecture15.front.FrontendService;
import com.jbtits.otus.lecture15.app.MsgToFrontend;
import com.jbtits.otus.lecture15.messageSystem.Address;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by tully.
 */
public class MsgGetUserIdAnswer extends MsgToFrontend {
    private final String name;
    private final long id;
    private final WebSocketSession session;

    public MsgGetUserIdAnswer(Address from, Address to, String name, long id, WebSocketSession session) {
        super(from, to);
        this.name = name;
        this.id = id;
        this.session = session;
    }

    @Override
    public void exec(FrontendService frontendService) {
//        frontendService.addUser(id, name, session);
    }
}
