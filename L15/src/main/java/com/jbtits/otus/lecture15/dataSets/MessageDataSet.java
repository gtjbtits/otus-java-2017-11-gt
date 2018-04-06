package com.jbtits.otus.lecture15.dataSets;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "message")
public class MessageDataSet extends DataSet {
    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDataSet user;

    @Column(name = "created")
    private Date created;

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getCreated() {
        return created;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserDataSet getUser() {
        return user;
    }

    public void setUser(UserDataSet user) {
        this.user = user;
    }

    public MessageDataSet() {
        created = new Date();
    }
}
