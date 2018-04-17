package com.jbtits.otus.lecture16.ms.channel;

import com.jbtits.otus.lecture16.ms.ServerMain;
import com.jbtits.otus.lecture16.ms.app.*;
import com.jbtits.otus.lecture16.ms.messages.HandshakeMsg;
import com.jbtits.otus.lecture16.ms.messages.HandshakeResponseMsg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;

public class ClientMsgAddressee extends ClientSocketMsgWorker {
    private static final Logger logger = LogManager.getLogger(ServerMain.class.getName());

    private final ClientType type;
    private ClientAddress address;
    private ConcurrentMap<Class<? extends Msg>, BiConsumer<Msg, ClientMsgAddressee>> handlers;
    private BiConsumer<String, Throwable> onError;

    public ClientMsgAddressee(String host, int port, ClientType type) throws IOException {
        super(host, port, 1);
        this.type = type;
        handlers = new ConcurrentHashMap<>();
    }

    public ClientAddress getAddress() {
        return address;
    }

    public void registerOnError(BiConsumer<String, Throwable> onError) {
        this.onError = onError;
    }

    private void handleError(String message, Throwable cause) {
        if (onError != null) {
            try {
                onError.accept(message, cause);
            } catch (Exception e) {
                logger.error("On error action failed", e);
            }
        } else {
            throw new RuntimeException(message, cause);
        }
    }

    public void addHandler(Class<? extends Msg> msgClass, BiConsumer<Msg, ClientMsgAddressee> consumer) {
        handlers.put(msgClass, consumer);
    }

    private void handshake() {
        HandshakeMsg handshakeMsg = new HandshakeMsg("handshake", type);
        send(handshakeMsg);
        HandshakeResponseMsg handshakeResponse;
        try {
            handshakeResponse = (HandshakeResponseMsg) take();
        } catch (InterruptedException e) {
            handleError("Handshake interrupted", e);
            return;
        }
        address = handshakeResponse.getAddress();
        logger.info("Client registered as [" + address.getType() + "] with uuid=" + address.getUuid());
    }

    private void receiveMsg() {
        while (!executor.isShutdown()) {
            Msg msg;
            try {
                msg = take();
            } catch (InterruptedException e) {
                handleError("Message take() interrupted", e);
                return;
            }
            logger.info("Incoming message [" + msg.getUuid() + "]");
            logger.debug(msg);
            Class<? extends Msg> clazz;
            try {
                clazz = (Class<? extends Msg>) Class.forName(msg.getClassName());
            } catch (ClassNotFoundException e) {
                logger.warn("Skip message with unknown class: " + msg.getClassName(), e);
                logger.debug(msg);
                continue;
            }
            if (handlers.containsKey(clazz)) {
                handlers.get(clazz).accept(msg, this);
            } else {
                logger.warn("No handler for message with class: " + msg.getClassName());
                logger.debug(msg);
            }
        }
    }

    @Override
    public void init() {
        super.init();
        handshake();
        executor.execute(this::receiveMsg);
    }
}
