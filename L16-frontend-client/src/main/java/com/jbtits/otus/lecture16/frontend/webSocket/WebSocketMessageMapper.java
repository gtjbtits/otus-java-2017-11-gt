package com.jbtits.otus.lecture16.frontend.webSocket;

import com.jbtits.otus.lecture16.frontend.webSocket.messages.Action;

public interface WebSocketMessageMapper {
    <T extends Action> T parse(String json);

    <T extends Action> String serialize(T message);

    boolean isUnauthClientAction(String action);
}
