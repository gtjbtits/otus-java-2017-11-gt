package com.jbtits.otus.lecture16.ms.app;

/**
 * Created by tully.
 */
public abstract class Msg {
    public static final String CLASS_NAME_VARIABLE = "className";

    private final String className;

    private final String uuid;

    private Address from;

    private Address to;

    protected Msg(Class<?> klass, String uuid) {
        this.className = klass.getName();
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public String getClassName() {
        return className;
    }

    public Address getFrom() {
        return from;
    }

    public Address getTo() {
        return to;
    }

    public void setFrom(Address from) {
        this.from = from;
    }

    public void setTo(Address to) {
        this.to = to;
    }
}
