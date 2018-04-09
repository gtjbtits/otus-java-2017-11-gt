package com.jbtits.otus.lecture15.messageContext;

import com.jbtits.otus.lecture15.messageSystem.Address;
import com.jbtits.otus.lecture15.messageSystem.MessageSystem;

/**
 * Created by tully.
 */
public class MessageSystemContext {
    private final MessageSystem messageSystem;

    private Address frontAddress;

    private Address dbAddress1;
    private Address dbAddress2;
    private boolean dbSwitcher;

    private Address randomDbAddress() {
        dbSwitcher = !dbSwitcher;
        if (dbSwitcher) {
            return dbAddress1;
        } else {
            return dbAddress2;
        }
    }

    public MessageSystemContext(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    public Address getFrontAddress() {
        return frontAddress;
    }

    public void setFrontAddress(Address frontAddress) {
        this.frontAddress = frontAddress;
    }

    public Address getDbAddress() {
        return randomDbAddress();
    }

    public void setDbAddress1(Address dbAddress1) {
        this.dbAddress1 = dbAddress1;
    }

    public void setDbAddress2(Address dbAddress2) {
        this.dbAddress2 = dbAddress2;
    }
}
