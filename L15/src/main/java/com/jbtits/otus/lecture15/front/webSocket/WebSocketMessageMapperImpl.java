package com.jbtits.otus.lecture15.front.webSocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jbtits.otus.lecture15.front.webSocket.messages.Action;
import com.jbtits.otus.lecture15.front.webSocket.messages.ActionWithAuth;
import com.jbtits.otus.lecture15.front.webSocket.messages.ActionWithSuccess;
import com.jbtits.otus.lecture15.utils.ArrayUtils;
import com.jbtits.otus.lecture15.utils.reflection.ReflectionUtils;
import com.jbtits.otus.lecture15.utils.reflection.SimpleField;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
        actions.put(SIGNUP_ACTION, ActionWithAuth.class);
        actions.put(SIGNIN_ACTION, ActionWithAuth.class);
        actions.put(SIGNUP_RESPONSE_ACTION, ActionWithSuccess.class);
        actions.put(SIGNIN_RESPONSE_ACTION, ActionWithSuccess.class);
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

    private <T extends Action> T parse(String json, Class<? extends Action> clazz) {
        T action;
        try {
            action = (T) mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Can\'t parse websocket message json", e);
        }
        if (!requiredCheck(action, Action.class)) {
            throw new RuntimeException("Json validation failed: some of required fields has null value");
        }
        return action;
    }

    private <T extends Action> boolean requiredCheck(T action, Class<? extends Action> clazz) {
        List<SimpleField> fields = ReflectionUtils.getFields(action, clazz);
        return fields.stream().allMatch(WebSocketMessageMapperImpl::isProperField);
    }

    private static boolean isProperField(SimpleField field) {
        boolean isRequired = Arrays.stream(field.getAnnotations()).anyMatch(a -> {
            return a.annotationType().equals(JsonProperty.class) && ((JsonProperty) a).required();
        });
        return !isRequired || field.getValue() != null;
    }
}
