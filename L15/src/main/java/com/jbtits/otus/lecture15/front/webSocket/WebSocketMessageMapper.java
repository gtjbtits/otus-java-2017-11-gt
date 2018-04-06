package com.jbtits.otus.lecture15.front.webSocket;

import com.jbtits.otus.lecture15.front.webSocket.messages.Action;

public interface WebSocketMessageMapper {
    <T extends Action> T parse(String json);

    <T extends Action> String serialize(T message);

    boolean isUnauthClientAction(String action);
}
