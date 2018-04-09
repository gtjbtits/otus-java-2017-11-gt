package com.jbtits.otus.lecture15.dataSets;

import com.jbtits.otus.lecture15.utils.ArrayUtils;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

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