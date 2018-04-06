package com.jbtits.otus.lecture15.front;

import com.jbtits.otus.lecture15.app.MessageSystemContext;
import com.jbtits.otus.lecture15.app.messages.MsgGetUser;
import com.jbtits.otus.lecture15.app.messages.MsgSaveMessage;
import com.jbtits.otus.lecture15.app.messages.MsgSaveUser;
import com.jbtits.otus.lecture15.front.webSocket.*;
import com.jbtits.otus.lecture15.front.webSocket.messages.*;
import com.jbtits.otus.lecture15.messageSystem.Address;
import com.jbtits.otus.lecture15.messageSystem.Message;
import com.jbtits.otus.lecture15.messageSystem.MessageSystem;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Date;

import static com.jbtits.otus.lecture15.front.webSocket.WebSocketMessageMapperImpl.*;
import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;

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
    public void registerUser(long userId, String uuid, String sessionId) {
        registry.setUserSession(sessionId, userId);
        Action action = new SuccessAction(uuid, SIGNUP_RESPONSE_ACTION, true);
        String json = mapper.serialize(action);
        sendWSMessage(json, registry.getSession(sessionId));
    }

    @Override
    public void broadcastMessageToClients(String uuid, String sessionId, String message, String userName, Date created) {
        String broadcast = mapper.serialize(new BroadcastMessage(UUID.BROADCAST, SERVER_MESSAGE, message, created.getTime(), userName));
        String response = mapper.serialize(new DateMessage(uuid, SERVER_MESSAGE, message, created.getTime()));
        for (WebSocketSession session : registry.getUserSessions()) {
            if (session.getId().equals(sessionId)) {
                sendWSMessage(response, session);
            } else {
                sendWSMessage(broadcast, session);
            }
        }
    }

    private void sendWSMessage(String json, WebSocketSession session) {
        if (!session.isOpen()) {
            throw new WebSocketError(ErrorCode.WS_SESSION_CLOSED);
        }
        try {
            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            throw new WebSocketError(ErrorCode.WS_SEND_MESSAGE_ERROR, e);
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
        Message message;
        switch (action.getAction()) {
            case SIGNUP_ACTION:
            case SIGNIN_ACTION:
                AuthAction auth = (AuthAction) action;
                String encodedPassword = security.encodePassword(auth.getPassword());
                if (SIGNUP_ACTION.equals(action.getAction())) {
                    message = new MsgSaveUser(getAddress(), context.getDbAddress(), action.getUuid(), session.getId(),
                        this, auth.getLogin(), encodedPassword);
                } else {
                    message = new MsgGetUser(getAddress(), context.getDbAddress(), action.getUuid(), session.getId(),
                        this, auth.getLogin(), encodedPassword);
                }
                break;
            case CLIENT_MESSAGE:
                MessageAction clientMessage = (MessageAction) action;
                message = new MsgSaveMessage(getAddress(), context.getDbAddress(), action.getUuid(), session.getId(),
                    this, clientMessage.getMessage(), registry.getUserId(session.getId()));
                break;
            default:
                // TODO: log
                throw new WebSocketError(ErrorCode.UNSUPPORTED_ACTION);
        }
        sendMessage(message);
    }

    private void sendMessage(Message message) {
        context.getMessageSystem().sendMessage(message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        registry.unregister(session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage text) {
        Action action;
        String uuid = UUID.UNKNOWN;
        try {
            action = mapper.parse(text.getPayload());
            uuid = action.getUuid();
            if (!validateRequest(action, session)) {
                // TODO: log me
                throw new WebSocketError(ErrorCode.ACTION_VALIDATION_FAILED);
            }
            handleRequest(action, session);
        } catch (WebSocketError e) {
            handleException(e, session, uuid);
        } catch (Exception e) {
            handleException(new WebSocketError(ErrorCode.UNKNOWN, e), session, uuid);
        }
    }

    private ErrorAction prepareErrorAction(WebSocketError e, String uuid) {
        ErrorAction error;
        if (e.getCause() != null) {
            String trace = getStackTrace(e.getCause());
            error = new ErrorAction(uuid, e.getMessage(), trace);
        } else {
            error = new ErrorAction(uuid, e.getMessage());
        }
        return error;
    }

    private void handleException(WebSocketError e, WebSocketSession session, String uuid) {
        sendWSMessage(mapper.serialize(prepareErrorAction(e, uuid)), session);
    }

    @Override
    public void handleException(WebSocketError e, String sessionId, String uuid) {
        sendWSMessage(mapper.serialize(prepareErrorAction(e, uuid)), registry.getSession(sessionId));
    }
}
