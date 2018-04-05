package com.jbtits.otus.lecture15.front.webSocket.messages;

public class AuthAction extends Action {
    private String login;
    private String password;

    public AuthAction() {
        super();
    }

    public AuthAction(String action, String login, String password) {
        super(action);
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
