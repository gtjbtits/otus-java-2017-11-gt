package com.jbtits.otus.lecture15.front.webSocket.messages;

public class WSMessage extends Action {
    private String message;

    public WSMessage() {
        super();
    }

    public WSMessage(String action, String message) {
        super(action);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
