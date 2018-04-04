package com.jbtits.otus.lecture15.app;

import com.jbtits.otus.lecture15.front.FrontendService;
import com.jbtits.otus.lecture15.messageSystem.Address;
import com.jbtits.otus.lecture15.messageSystem.Addressee;
import com.jbtits.otus.lecture15.messageSystem.Message;

/**
 * Created by tully.
 */
public abstract class MsgToFrontend extends Message {
    public MsgToFrontend(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof FrontendService) {
            exec((FrontendService) addressee);
        } else {
            //todo error!
        }
    }

    public abstract void exec(FrontendService frontendService);
}