package com.jbtits.otus.lecture16.ms.dataSets;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class UserDataSet extends DataSet {
    @Column(name = "name", unique = true)
    private String name;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "password")
    private String password;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}