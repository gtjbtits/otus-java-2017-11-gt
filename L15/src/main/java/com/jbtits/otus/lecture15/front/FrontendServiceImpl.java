package com.jbtits.otus.lecture15.front;

import com.jbtits.otus.lecture15.app.MessageSystemContext;
import com.jbtits.otus.lecture15.app.messages.MsgSaveUser;
import com.jbtits.otus.lecture15.dataSets.UserDataSet;
import com.jbtits.otus.lecture15.front.webSocket.WebSocketMessageMapper;
import com.jbtits.otus.lecture15.front.webSocket.WebSocketSessionsRegistry;
import com.jbtits.otus.lecture15.front.webSocket.messages.Action;
import com.jbtits.otus.lecture15.front.webSocket.messages.ActionWithAuth;
import com.jbtits.otus.lecture15.front.webSocket.messages.ActionWithMessage;
import com.jbtits.otus.lecture15.front.webSocket.messages.ActionWithSuccess;
import com.jbtits.otus.lecture15.messageSystem.Address;
import com.jbtits.otus.lecture15.messageSystem.Message;
import com.jbtits.otus.lecture15.messageSystem.MessageSystem;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

import static com.jbtits.otus.lecture15.front.webSocket.WebSocketMessageMapperImpl.ERROR_ACTION;
import static com.jbtits.otus.lecture15.front.webSocket.WebSocketMessageMapperImpl.SIGNUP_ACTION;
import static com.jbtits.otus.lecture15.front.webSocket.WebSocketMessageMapperImpl.SIGNUP_RESPONSE_ACTION;

/**
 * Created by tully.
 */
public class FrontendServiceImpl extends TextWebSocketHandler implements FrontendService {
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
    public void addUser(UserDataSet user, String sessionId) {
        registry.register(session.getId(), user.getId());
        Action action = new ActionWithSuccess(SIGNUP_RESPONSE_ACTION, true);
        String json = mapper.serialize(action);
        sendWSMessage(json, session);
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
                ActionWithAuth auth = (ActionWithAuth) action;
                String encodedPassword = security.encodePassword(auth.getPassword());
                Message message = new MsgSaveUser(getAddress(), context.getDbAddress(), session.getId(),
                    auth.getLogin(), encodedPassword);
                sendMessage(message);
                break;
            default:
                // TODO: log
                throw new RuntimeException("Nothing to do (no specific handler provided)");
        }
    }

    private void sendMessage(Message message) {
        context.getMessageSystem().sendMessage(message);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage text) throws Exception {
        ConcurrentWebSocketSessionDecorator concurrentSession
            = new ConcurrentWebSocketSessionDecorator(session, 1000, 1024);
        try {
            Action action = mapper.parse(text.getPayload());
            if (!mapper.isSupportedClientAction(action.getAction())) {
//                // TODO: Logger
                throw new RuntimeException("Unsupported action");
            }
            if (!validateRequest(action, concurrentSession)) {
//                // TODO: Logger
                throw new RuntimeException("Validation failed");
            }
            handleRequest(action, concurrentSession);
        } catch (Exception e) {
            handleException(e, concurrentSession);
        }
    }

    private void handleException(Exception e, WebSocketSession session) {
        ActionWithMessage message = new ActionWithMessage(ERROR_ACTION, e.getMessage());
        sendWSMessage(mapper.serialize(message), session);
    }
}
