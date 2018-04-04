package com.jbtits.otus.lecture15.front.webSocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jbtits.otus.lecture15.front.webSocket.messages.Action;
import com.jbtits.otus.lecture15.front.webSocket.messages.TokenAction;
import com.jbtits.otus.lecture15.utils.ArrayUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WebSocketMessageMapperImpl implements WebSocketMessageMapper {
    public final static String ERROR_ACTION = "error";
    public final static String SIGNUP_ACTION = "signup";
    public final static String SIGNUP_RESPONSE_ACTION = "signup_response";
    public final static String SIGNIN_ACTION = "signin";
    public final static String SIGNIN_RESPONSE_ACTION = "signin_response";

    private final static String supportedUnauthClientActions[] = {
        SIGNUP_ACTION,
        SIGNIN_ACTION
    };

    private final static String supportedAuthClientActions[] = {
    };

    private final ObjectMapper mapper;
    private final Map<String, Class<? extends Action>> actions;

    public WebSocketMessageMapperImpl() {
        mapper = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        actions = new HashMap<>();
        fillActions();
    }

    private void fillActions() {
        actions.put(SIGNIN_RESPONSE_ACTION, TokenAction.class);
        actions.put(SIGNUP_RESPONSE_ACTION, TokenAction.class);
    }

    public <T extends Action> T parse(String json) {
        Action action = parse(json, Action.class);
        String actionName = action.getAction();
        if (actionName == null) {
            throw new NullPointerException("No action name present");
        }
        if (!actions.containsKey(actionName)) {
            return (T) action;
        }
        return parse(json, actions.get(actionName));
    }

    @Override
    public <T extends Action> String serialize(T message) {
        String json;
        try {
            json = mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Websocket message object to json serialization error", e);
        }
        return json;
    }

    @Override
    public boolean isSupportedClientAction(String action) {
        return ArrayUtils.inArray(ArrayUtils.concat(supportedUnauthClientActions, supportedAuthClientActions), action);
    }

    @Override
    public boolean isUnauthClientAction(String action) {
        return ArrayUtils.inArray(supportedUnauthClientActions, action);
    }

    private <T extends Action> T parse(String json, Class<?> clazz) {
        T action;
        try {
            action = (T) mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Can\'t parse websocket message json", e);
        }
        return action;
    }
}
