package com.jbtits.otus.lecture16.frontend.webSocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jbtits.otus.lecture16.frontend.webSocket.messages.Action;
import com.jbtits.otus.lecture16.frontend.webSocket.messages.AuthAction;
import com.jbtits.otus.lecture16.frontend.webSocket.messages.MessageAction;
import com.jbtits.otus.lecture16.frontend.webSocket.messages.SuccessAction;
import com.jbtits.otus.lecture16.ms.utils.ArrayUtils;
import com.jbtits.otus.lecture16.ms.utils.reflection.ReflectionUtils;
import com.jbtits.otus.lecture16.ms.utils.reflection.SimpleField;

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
    public final static String CLIENT_MESSAGE_ACTION = "client_message";
    public final static String SERVER_MESSAGE_ACTION = "server_message";

    private final static String supportedUnauthClientActions[] = {
        SIGNUP_ACTION,
        SIGNIN_ACTION
    };

    private final static String supportedAuthClientActions[] = {
        CLIENT_MESSAGE_ACTION
    };

    private final ObjectMapper mapper;
    private final Map<String, Class<? extends Action>> actions;

    public WebSocketMessageMapperImpl() {
        mapper = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        actions = new HashMap<>();
        fillActions();
    }

    private void fillActions() {
        actions.put(SIGNUP_ACTION, AuthAction.class);
        actions.put(SIGNIN_ACTION, AuthAction.class);
        actions.put(SIGNUP_RESPONSE_ACTION, SuccessAction.class);
        actions.put(SIGNIN_RESPONSE_ACTION, SuccessAction.class);
        actions.put(CLIENT_MESSAGE_ACTION, MessageAction.class);
        actions.put(SERVER_MESSAGE_ACTION, MessageAction.class);
    }

    public <T extends Action> T parse(String json) {
        Action action = parse(json, Action.class);
        String actionName = action.getAction();
        if (actionName == null || !isSupportedClientAction(actionName)) {
            throw new WebSocketError(ErrorCode.JSON_BAD_ACTION);
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
            throw new WebSocketError(ErrorCode.JSON_SERIALIZE_ERROR, e);
        }
        return json;
    }

    private boolean isSupportedClientAction(String action) {
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
            throw new WebSocketError(ErrorCode.JSON_PARSE_ERROR, e);
        }
        if (!requiredCheck(action, Action.class)) {
            throw new WebSocketError(ErrorCode.JSON_REQUIRED_ERROR);
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
