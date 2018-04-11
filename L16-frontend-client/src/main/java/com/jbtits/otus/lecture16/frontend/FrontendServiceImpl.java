package com.jbtits.otus.lecture16.frontend;

import com.jbtits.otus.lecture16.frontend.webSocket.*;
import com.jbtits.otus.lecture16.frontend.webSocket.messages.*;
import com.jbtits.otus.lecture16.ms.app.Address;
import com.jbtits.otus.lecture16.ms.app.ClientType;
import com.jbtits.otus.lecture16.ms.app.Msg;
import com.jbtits.otus.lecture16.ms.app.MsgWorker;
import com.jbtits.otus.lecture16.ms.channel.ClientSocketMsgWorker;
import com.jbtits.otus.lecture16.ms.messages.HandshakeMsg;
import com.jbtits.otus.lecture16.ms.messages.HandshakeResponseMsg;
import com.jbtits.otus.lecture16.ms.messages.SignupMsg;
import com.jbtits.otus.lecture16.ms.messages.SignupResponseMsg;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.jbtits.otus.lecture16.frontend.webSocket.WebSocketMessageMapperImpl.SERVER_MESSAGE_ACTION;
import static com.jbtits.otus.lecture16.frontend.webSocket.WebSocketMessageMapperImpl.SIGNUP_ACTION;
import static com.jbtits.otus.lecture16.frontend.webSocket.WebSocketMessageMapperImpl.SIGNUP_RESPONSE_ACTION;
import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;

public class FrontendServiceImpl extends TextWebSocketHandler implements FrontendService {
    private static final Logger logger = Logger.getLogger(FrontendServiceImpl.class.getName());

    private final WebSocketSessionsRegistry registry;
    private final WebSocketMessageMapper mapper;
    private final SecurityService security;
    private final ClientSocketMsgWorker client;

    private Address address;
    private Map<Class<? extends Msg>, BiConsumer<Msg, MsgWorker>> handlers;

    public FrontendServiceImpl(WebSocketSessionsRegistry registry, WebSocketMessageMapper mapper,
                               SecurityService security, ClientSocketMsgWorker client) {

        this.registry = registry;
        this.mapper = mapper;
        this.security = security;
        this.client = client;

        address = new Address(ClientType.FRONTEND_SERVICE);
        handlers = new HashMap<>();
    }

    @Override
    public void init() {
        client.init();
        handshake(client);
        startClientReciever();

        handlers.put(SignupResponseMsg.class, handleSignupResp());
    }

    private BiConsumer<Msg, MsgWorker> handleSignupResp() {
        return (message, client) -> {
            SignupResponseMsg signupResponseMsg = (SignupResponseMsg) message;
            logger.log(Level.INFO, "SignUp resp " + signupResponseMsg.isSuccess());
        };
    }

    private void handshake(MsgWorker client) {
        Msg handshake = new HandshakeMsg("handshake", address.getType());
        client.send(handshake);
        HandshakeResponseMsg handshakeResponse = null;
        try {
            handshakeResponse = (HandshakeResponseMsg) client.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        address = handshakeResponse.getAddress();
        System.out.println("Client registered: " + address.getUuid());
    }

    private void startClientReciever() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                while (true) {
                    Msg msg = client.take();
                    Class<? extends Msg> clazz = Msg.class;
                    logger.log(Level.INFO, "Message taken " + msg.getUuid());
                    try {
                        clazz = (Class<? extends Msg>) Class.forName(msg.getClassName());
                    } catch (ClassNotFoundException e) {
                        System.out.println("Bad class in Msg className");
                        e.printStackTrace();
                    }
                    if (handlers.containsKey(clazz)) {
                        handlers.get(clazz).accept(msg, client);
                    } else {
                        System.out.println("Unknown message received: " + msg.toString());
                    }
                }
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        });
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
        String broadcast = mapper.serialize(new BroadcastMessage(UUID.BROADCAST, SERVER_MESSAGE_ACTION, message, created.getTime(), userName));
        String response = mapper.serialize(new DateMessage(uuid, SERVER_MESSAGE_ACTION, message, created.getTime()));
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

    private boolean validateRequest(Action action, WebSocketSession session) {
        boolean isRegistered = registry.hasUser(session.getId());
        if (mapper.isUnauthClientAction(action.getAction())) {
            return !isRegistered;
        }
        return isRegistered;
    }

    private void handleRequest(Action action, WebSocketSession session) {
        registry.register(session);
        switch (action.getAction()) {
            case SIGNUP_ACTION:
//            case SIGNIN_ACTION:
                AuthAction auth = (AuthAction) action;
                String encodedPassword = security.encodePassword(auth.getPassword());
                SignupMsg signupMsg = new SignupMsg(action.getUuid(), auth.getLogin(), encodedPassword);
                signupMsg.setFrom(address);
                signupMsg.setTo(new Address(ClientType.DB_SERVICE));
                client.send(signupMsg);
                logger.log(Level.INFO, "Message sent " + signupMsg.getUuid());
//                if (SIGNUP_ACTION.equals(action.getAction())) {
//                    message = new MsgSaveUser(getAddress(), context.getDbAddress(), action.getUuid(), session.getId(),
//                        this, auth.getLogin(), encodedPassword);
//                } else {
//                    message = new MsgGetUser(getAddress(), context.getDbAddress(), action.getUuid(), session.getId(),
//                        this, auth.getLogin(), encodedPassword);
//                }
                break;
//            case CLIENT_MESSAGE_ACTION:
//                MessageAction clientMessage = (MessageAction) action;
//                message = new MsgSaveMessage(getAddress(), context.getDbAddress(), action.getUuid(), session.getId(),
//                    this, clientMessage.getMessage(), registry.getUserId(session.getId()));
//                break;
            default:
                throw new WebSocketError(ErrorCode.UNSUPPORTED_ACTION);
        }
//        sendMessage(message);
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
}
