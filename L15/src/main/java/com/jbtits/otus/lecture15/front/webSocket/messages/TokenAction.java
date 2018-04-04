package com.jbtits.otus.lecture15.front.webSocket.messages;

public class TokenAction extends Action {
    private String token;

    public TokenAction() {
        super();
    }

    public TokenAction(String token, String action) {
        super(action);
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
