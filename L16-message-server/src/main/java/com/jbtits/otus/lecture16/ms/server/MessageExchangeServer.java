package com.jbtits.otus.lecture16.ms.server;

import com.jbtits.otus.lecture16.ms.app.Address;
import com.jbtits.otus.lecture16.ms.app.ClientType;
import com.jbtits.otus.lecture16.ms.app.Msg;
import com.jbtits.otus.lecture16.ms.app.MsgWorker;
import com.jbtits.otus.lecture16.ms.channel.Blocks;
import com.jbtits.otus.lecture16.ms.channel.SocketMsgWorker;
import com.jbtits.otus.lecture16.ms.messages.HandshakeMsg;
import com.jbtits.otus.lecture16.ms.messages.HandshakeResponseMsg;
import org.apache.logging.log4j.LogManager;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;
import java.util.function.Predicate;

/**
 * Created by tully.
 */
public class MessageExchangeServer {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(MessageExchangeServer.class.getName());

    private static final int THREADS_NUMBER = 1;

    private final ExecutorService executor;
    private final ConcurrentMap<Address, MsgWorker> clients;
    private final int port;

    public MessageExchangeServer(int port) {
        executor = Executors.newFixedThreadPool(THREADS_NUMBER);
        clients = new ConcurrentHashMap<>();
        this.port = port;
    }

    @Blocks
    public void start() throws Exception {
        Future future = executor.submit(this::exchange);
        executor.submit(this::acceptSocketConnections);

//        try (ServerSocket serverSocket = new ServerSocket(port)) {
//            logger.info("Server started on port: " + serverSocket.getLocalPort());
//            while (!executor.isShutdown()) {
//                Socket socket = serverSocket.accept(); //blocks
//                SocketMsgWorker client = new SocketMsgWorker(socket);
//                client.init();
//                clientHandshake(client);
//            }
//        } catch (Exception e) {
//            logger.error("Socket acceptor", e);
//        }

        try {
            future.get();
        } catch (ExecutionException ex) {
            logger.error("Future execution", ex);
        }
    }

    private void acceptSocketConnections() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Server started on port: " + serverSocket.getLocalPort());
            while(true) {
                Socket socket = serverSocket.accept(); //blocks
                SocketMsgWorker client = new SocketMsgWorker(socket);
                client.init();
                clientHandshake(client);
            }
        } catch (Exception e) {
            logger.error("Socket acceptor", e);
        }
    }

    // move to msgworker
    private void clientHandshake(MsgWorker client) {
        HandshakeMsg handshake = null;
        try {
            handshake = (HandshakeMsg) client.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Address address = new Address(handshake.getClientType());
        address.generateUuid();
        clients.put(address, client);
        logger.info("Client [" + address.getType() + "] registered at " + address.getUuid());
        client.send(new HandshakeResponseMsg(handshake.getUuid(), address));
    }

    private Address getRandomAddresstByType(ClientType type) {
        Random generator = new Random();
        Object values[] = clients.entrySet().stream()
                .map(Map.Entry::getKey)
                .filter(typeFilter(type))
                .toArray();
        if (values.length < 1) {
            return null;
        }
        return (Address) values[generator.nextInt(values.length)];
    }

    private Predicate<Address> typeFilter(ClientType type) {
        return address -> {
            return address.getType().equals(type);
        };
    }

    private void sendSpecificClient(Msg msg) {
        Address address = msg.getTo();

        // address may be not defined
        clients.get(address).send(msg);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void exchange() {
        while (true) {
            for (Map.Entry<Address, MsgWorker> client : clients.entrySet()) {
                Msg msg = client.getValue().pool();
                while (msg != null) {
                    logger.info("Accept Message [" + msg.getUuid() + "] from [" + msg.getFrom().getType() + "] " + msg.getFrom().getUuid());
                    logger.debug("Message from " + msg.getFrom());
                    logger.debug("Message to " + msg.getTo());

                    // TODO: handle NPE!
                    Address to = msg.getTo();
                    if (to.getUuid() == null) {
                        to = getRandomAddresstByType(msg.getTo().getType());
                    }

                    msg.setTo(to);

                    // npe
                    sendSpecificClient(msg);

                    logger.info("Send Message [" + msg.getUuid() + "] to [" + to.getType() + "] " + to.getUuid());
                    logger.debug("Message from " + msg.getFrom());
                    logger.debug("Message to " + msg.getTo());
                    msg = client.getValue().pool();
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                logger.error("Server main cycle interrupted", e);
            }
        }
    }
}
