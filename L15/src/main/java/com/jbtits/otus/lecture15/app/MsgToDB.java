package com.jbtits.otus.lecture15.app;

import com.jbtits.otus.lecture15.app.messages.SessionMessage;
import com.jbtits.otus.lecture15.dbService.DBService;
import com.jbtits.otus.lecture15.messageSystem.Address;
import com.jbtits.otus.lecture15.messageSystem.Addressee;

/**
 * Created by tully.
 */
public abstract class MsgToDB extends SessionMessage {
    public MsgToDB(Address from, Address to, String sessionId) {
        super(from, to, sessionId);
    }

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof DBService) {
            exec((DBService) addressee);
        }
    }

    public abstract void exec(DBService dbService);
}
