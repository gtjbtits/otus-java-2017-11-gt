package com.jbtits.otus.lecture15.front;

import com.jbtits.otus.lecture15.app.MessageSystemContext;
import com.jbtits.otus.lecture15.app.messages.MsgSaveUser;
import com.jbtits.otus.lecture15.front.webSocket.*;
import com.jbtits.otus.lecture15.front.webSocket.messages.*;
import com.jbtits.otus.lecture15.messageSystem.Address;
import com.jbtits.otus.lecture15.messageSystem.Message;
import com.jbtits.otus.lecture15.messageSystem.MessageSystem;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

import static com.jbtits.otus.lecture15.front.webSocket.WebSocketMessageMapperImpl.ERROR_ACTION;
import static com.jbtits.otus.lecture15.front.webSocket.WebSocketMessageMapperImpl.SIGNUP_ACTION;
import static com.jbtits.otus.lecture15.front.webSocket.WebSocketMessageMapperImpl.SIGNUP_RESPONSE_ACTION;

/**
 * Created by tully.
 */
public class FrontendServiceImpl extends TextWebSocketHandler implements FrontendService, WebSocketErrorHandler {
    private final Address address;
    private final MessageSystemContext context;
    private final WebSocketSessionsRegistry registry;
    private final WebSocketMessageMapper mapper;
    private final SecurityService security;

    public FrontendServiceImpl(MessageSystemContext context, Address address, WebSocketSessionsRegistry registry,
        WebSocketMessageMapper mapper, SecurityService security) {

        this.context = context;
        this.address = address;
        this.registry = registry;
        this.mapper = mapper;
        this.security = security;
    }

    public void init() {
        context.getMessageSystem().addAddressee(this);
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void addUser(long userId, String uuid, String sessionId) {
        registry.setUserSession(sessionId, userId);
        Action action = new SuccessAction(uuid, SIGNUP_RESPONSE_ACTION, true);
        String json = mapper.serialize(action);
        sendWSMessage(json, registry.getSession(sessionId));
    }

    private void sendWSMessage(String json, WebSocketSession session) {
        try {
            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            throw new RuntimeException("Send websocket message error", e);
        }
    }

    @Override
    public MessageSystem getMS() {
        return context.getMessageSystem();
    }

    private boolean validateRequest(Action action, WebSocketSession session) {
        boolean isRegistered = registry.hasUser(session.getId());
        if (mapper.isUnauthClientAction(action.getAction())) {
            return !isRegistered;
        }
        // TODO: token auth (regenerate session, if connection was broken)
        return isRegistered;
    }

    private void handleRequest(Action action, WebSocketSession session) {
        registry.register(session);
        switch (action.getAction()) {
            case SIGNUP_ACTION:
                AuthAction auth = (AuthAction) action;
                String encodedPassword = security.encodePassword(auth.getPassword());
                Message message = new MsgSaveUser(getAddress(), context.getDbAddress(), action.getUuid(), session.getId(),
                    this, auth.getLogin(), encodedPassword);
                sendMessage(message);
                break;
            default:
                // TODO: log
                throw new WebSocketError(ErrorCode.UNSUPPORTED_ACTION);
        }
    }

    private void sendMessage(Message message) {
        context.getMessageSystem().sendMessage(message);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage text) {
        try {
            Action action = mapper.parse(text.getPayload());
            if (!validateRequest(action, session)) {
                // TODO: log me
                throw new WebSocketError(ErrorCode.WS_MESSAGE_NOT_VALID);
            }
            handleRequest(action, session);
        } catch (WebSocketError e) {
            handleException(e, session);
        } catch (Exception e) {
            handleException(new WebSocketError(ErrorCode.UNKNOWN, e), session);
        }
    }

    private ErrorAction prepareErrorAction(WebSocketError e, String uuid) {
        ErrorAction error;
        if (e.getCause() != null) {
            String trace = org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e.getCause());
            error = new ErrorAction(uuid, e.getMessage(), trace);
        } else {
            error = new ErrorAction(uuid, e.getMessage());
        }
        return error;
    }

    private ErrorAction prepareErrorAction(WebSocketError e) {
        return prepareErrorAction(e, UUIDs.MIRACAST);
    }

    private void handleException(WebSocketError e, WebSocketSession session) {
        sendWSMessage(mapper.serialize(prepareErrorAction(e)), session);
    }

    @Override
    public void handleException(WebSocketError e, String sessionId, String uuid) {
        sendWSMessage(mapper.serialize(prepareErrorAction(e, uuid)), registry.getSession(sessionId));
    }
}
