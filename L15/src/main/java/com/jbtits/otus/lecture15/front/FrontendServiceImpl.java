package com.jbtits.otus.lecture15.front;

import com.jbtits.otus.lecture15.app.MessageSystemContext;
import com.jbtits.otus.lecture15.app.messages.MsgSaveUser;
import com.jbtits.otus.lecture15.dataSets.UserDataSet;
import com.jbtits.otus.lecture15.front.webSocket.WebSocketException;
import com.jbtits.otus.lecture15.front.webSocket.WebSocketMessageMapper;
import com.jbtits.otus.lecture15.front.webSocket.WebSocketSessionsRegistry;
import com.jbtits.otus.lecture15.front.webSocket.messages.Action;
import com.jbtits.otus.lecture15.front.webSocket.messages.TokenAction;
import com.jbtits.otus.lecture15.front.webSocket.messages.WSMessage;
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

    public FrontendServiceImpl(MessageSystemContext context, Address address, WebSocketSessionsRegistry registry, WebSocketMessageMapper mapper) {
        this.context = context;
        this.address = address;
        this.registry = registry;
        this.mapper = mapper;
    }

    public void init() {
        context.getMessageSystem().addAddressee(this);
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void addUser(UserDataSet user, WebSocketSession session) {
        registry.register(session.getId(), user.getId());
        Action action = new Action(SIGNUP_RESPONSE_ACTION);
        String json = mapper.serialize(action);
        sendMessage(json, session);
    }

    private void sendMessage(String json, WebSocketSession session) {
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
        boolean isRegistered = registry.isRegistered(session.getId());
        if (mapper.isUnauthClientAction(action.getAction())) {
            return !isRegistered;
        }
//        TokenAction tokenAction = (TokenAction) action;
//        String token = tokenAction.getToken();
//        if (token != null && isRegistered) {
//            return false;
//        }
        return isRegistered;
    }

    private void handleRequest(Action action, WebSocketSession session) {
        switch (action.getAction()) {
            case SIGNUP_ACTION:
                Message message = new MsgSaveUser(
                    getAddress(), context.getDbAddress(),
                    "login", "password", session);
                context.getMessageSystem().sendMessage(message);
                break;
            default:
                // TODO: log
                System.out.println("[!] No handler for action " + action.getAction());
                break;
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage text) throws Exception {
        ConcurrentWebSocketSessionDecorator concurrentSession
            = new ConcurrentWebSocketSessionDecorator(session, 1000, 1024);
        try {
            Action action = mapper.parse(text.getPayload());
            if (!mapper.isSupportedClientAction(action.getAction())) {
//                // TODO: Logger
                throw new WebSocketException("[!] Unsupported client action " + action.getAction());
            }
            if (!validateRequest(action, concurrentSession)) {
//                // TODO: Logger
                throw new WebSocketException("[!] Validation failed");
            }
            handleRequest(action, session);
        } catch (WebSocketException e) {
            handleException(e, session);
        }
    }

    private void handleException(Throwable cause, WebSocketSession session) {
        WSMessage message = new WSMessage(ERROR_ACTION, cause.getMessage());
        sendMessage(mapper.serialize(message), session);
    }
}
