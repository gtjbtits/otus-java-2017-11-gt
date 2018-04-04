package com.jbtits.otus.lecture15.app;

import com.jbtits.otus.lecture15.dbService.DBService;
import com.jbtits.otus.lecture15.messageSystem.Address;
import com.jbtits.otus.lecture15.messageSystem.Addressee;
import com.jbtits.otus.lecture15.messageSystem.Message;

/**
 * Created by tully.
 */
public abstract class MsgToDB extends Message {
    public MsgToDB(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof DBService) {
            exec((DBService) addressee);
        }
    }

    public abstract void exec(DBService dbService);
}
