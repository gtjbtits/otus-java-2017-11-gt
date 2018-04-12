package com.jbtits.otus.lecture16.ms.server;

import com.jbtits.otus.lecture16.ms.app.Address;
import com.jbtits.otus.lecture16.ms.app.ClientType;
import com.jbtits.otus.lecture16.ms.app.Msg;
import com.jbtits.otus.lecture16.ms.app.MsgWorker;
import com.jbtits.otus.lecture16.ms.channel.Blocks;
import com.jbtits.otus.lecture16.ms.channel.SocketMsgWorker;
import com.jbtits.otus.lecture16.ms.messages.HandshakeMsg;
import com.jbtits.otus.lecture16.ms.messages.HandshakeResponseMsg;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Created by tully.
 */
public class MirrorSocketMsgServer {
    private static final Logger logger = Logger.getLogger(MirrorSocketMsgServer.class.getName());

    private static final int THREADS_NUMBER = 1;
    private static final int MIRROR_DELAY_MS = 100;

    private final ExecutorService executor;
    private final ConcurrentMap<Address, MsgWorker> clients;
    private final int port;

    public MirrorSocketMsgServer(int port) {
        executor = Executors.newFixedThreadPool(THREADS_NUMBER);
        clients = new ConcurrentHashMap<>();
        this.port = port;
    }

    @Blocks
    public void start() throws Exception {
        executor.submit(this::mirror);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Server started on port: " + serverSocket.getLocalPort());
            while (!executor.isShutdown()) {
                Socket socket = serverSocket.accept(); //blocks
                SocketMsgWorker client = new SocketMsgWorker(socket);
                client.init();
                clientHandshake(client);
            }
        }
    }

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
        System.out.println("Client " + address.getType() + " registered at " + address.getUuid());
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
        clients.get(address).send(msg);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void mirror() {
        while (true) {
            for (Map.Entry<Address, MsgWorker> client : clients.entrySet()) {
                Msg msg = client.getValue().pool();
                while (msg != null) {
                    logger.log(Level.INFO, "Accept Message " + msg.getUuid() + " from " + msg.getFrom() + " to " + msg.getTo());
                    // TODO: handle NPE!
                    Address to = getRandomAddresstByType(msg.getTo().getType());
                    msg.setTo(to);
                    sendSpecificClient(msg);
                    logger.log(Level.INFO, "Send Message " + msg.getUuid() + " to " + to);
                    msg = client.getValue().pool();
                }
            }
            try {
                Thread.sleep(MIRROR_DELAY_MS);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, e.toString());
            }
        }
    }
}
