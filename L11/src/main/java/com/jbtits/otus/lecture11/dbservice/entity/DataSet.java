package com.jbtits.otus.lecture11.dbservice.entity;

abstract public class DataSet {
    protected long id;

    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }
}
