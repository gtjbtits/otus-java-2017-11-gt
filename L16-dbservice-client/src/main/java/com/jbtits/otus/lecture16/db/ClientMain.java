package com.jbtits.otus.lecture16.db;

import com.jbtits.otus.lecture16.ms.app.Address;
import com.jbtits.otus.lecture16.ms.app.ClientType;
import com.jbtits.otus.lecture16.ms.app.Msg;
import com.jbtits.otus.lecture16.ms.app.MsgWorker;
import com.jbtits.otus.lecture16.ms.channel.ClientSocketMsgWorker;
import com.jbtits.otus.lecture16.ms.channel.SocketMsgWorker;
import com.jbtits.otus.lecture16.ms.messages.HandshakeMsg;
import com.jbtits.otus.lecture16.ms.messages.HandshakeResponseMsg;
import com.jbtits.otus.lecture16.ms.messages.SignupMsg;
import com.jbtits.otus.lecture16.ms.messages.SignupResponseMsg;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by tully.
 */
public class ClientMain {
    private static final Logger logger = Logger.getLogger(ClientMain.class.getName());

    private static final String HOST = "localhost";
    private static final int PORT = 5050;
    private static final int PAUSE_MS = 5000;
    private static final int MAX_MESSAGES_COUNT = 10;
    private Address address;
    private Map<Class<? extends Msg>, BiConsumer<Msg, MsgWorker>> handlers;

    public ClientMain() {
        address = new Address(ClientType.DB_SERVICE);
        handlers = new HashMap<>();

        handlers.put(SignupMsg.class, handleSignup());
    }

    private BiConsumer<Msg, MsgWorker> handleSignup() {
        return (message, client) -> {
            SignupResponseMsg signupResponseMsg = new SignupResponseMsg(message.getUuid(), true);
            signupResponseMsg.setTo(new Address(ClientType.FRONTEND_SERVICE));
            client.send(signupResponseMsg);
        };
    }

    public static void main(String[] args) throws Exception {
        new ClientMain().start();
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
        logger.log(Level.INFO, "Client registered: " + address.getUuid());
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void start() throws Exception {
        SocketMsgWorker client = new ClientSocketMsgWorker(HOST, PORT);
        client.init();

        handshake(client);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                while (true) {
                    Msg msg = client.take();
                    logger.log(Level.INFO, "Message taken " + msg.getUuid());
                    Class<? extends Msg> clazz = Msg.class;
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

        Thread.sleep(60_000);
        client.close();
        executorService.shutdown();
    }

}
