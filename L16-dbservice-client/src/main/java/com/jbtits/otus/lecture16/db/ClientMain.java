package com.jbtits.otus.lecture16.db;

import com.jbtits.otus.lecture16.db.cache.CacheServiceImpl;
import com.jbtits.otus.lecture16.db.dbService.DBService;
import com.jbtits.otus.lecture16.db.dbService.DBServiceHibernateImpl;
import com.jbtits.otus.lecture16.ms.app.Address;
import com.jbtits.otus.lecture16.ms.app.ClientType;
import com.jbtits.otus.lecture16.ms.app.Msg;
import com.jbtits.otus.lecture16.ms.app.MsgWorker;
import com.jbtits.otus.lecture16.ms.channel.ClientSocketMsgWorker;
import com.jbtits.otus.lecture16.ms.channel.SocketMsgWorker;
import com.jbtits.otus.lecture16.ms.dataSets.DataSet;
import com.jbtits.otus.lecture16.ms.dataSets.MessageDataSet;
import com.jbtits.otus.lecture16.ms.dataSets.UserDataSet;
import com.jbtits.otus.lecture16.ms.messages.*;
import com.jbtits.otus.lecture16.ms.messages.error.ErrorCode;
import com.jbtits.otus.lecture16.ms.messages.error.ErrorMsg;

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

    private final DBService dbService;

    public ClientMain() {
        address = new Address(ClientType.DB_SERVICE);
        handlers = new HashMap<>();
        dbService = new DBServiceHibernateImpl(new CacheServiceImpl<String, DataSet>(1000, 1000));

        handlers.put(SignupMsg.class, handleSignup());
        handlers.put(SigninMsg.class, handleSignin());
        handlers.put(SendMessageMsg.class, handleSendMessage());
    }

    private BiConsumer<Msg, MsgWorker> handleSignup() {
        return (message, client) -> {
            SignupMsg signupMsg = (SignupMsg) message;
            if (dbService.getUserByName(signupMsg.getLogin()) != null) {
                ErrorMsg errorMsg = new ErrorMsg(message.getUuid(), new Exception(ErrorCode.DB_USER_ALREADY_EXISTS.toString()));
                errorMsg.setFrom(address);
                errorMsg.setTo(message.getFrom());
                client.send(errorMsg);
                return;
            }
            UserDataSet user = new UserDataSet();
            user.setName(signupMsg.getLogin());
            user.setPassword(signupMsg.getPassword());
            dbService.saveUser(user);
            SignupResponseMsg signupResponseMsg = new SignupResponseMsg(message.getUuid(), user.getId());
            signupResponseMsg.setTo(message.getFrom());
            signupResponseMsg.setFrom(address);
            client.send(signupResponseMsg);
        };
    }

    private BiConsumer<Msg, MsgWorker> handleSendMessage() {
        return (message, client) -> {
            SendMessageMsg sendMessageMsg = (SendMessageMsg) message;
            MessageDataSet messageDataSet = new MessageDataSet();
            messageDataSet.setText(sendMessageMsg.getText());
            dbService.saveMessage(messageDataSet, sendMessageMsg.getUserId());

            BroadcastMessageMsg broadcastMessageMsg = new BroadcastMessageMsg(message.getUuid(), messageDataSet.getText(),
                messageDataSet.getUser().getName(), messageDataSet.getCreated());
            broadcastMessageMsg.setTo(message.getFrom());
            broadcastMessageMsg.setFrom(address);
            client.send(broadcastMessageMsg);
        };
    }

    private BiConsumer<Msg, MsgWorker> handleSignin() {
        return (message, client) -> {
            SigninMsg signinMsg = (SigninMsg) message;
            UserDataSet user = dbService.getUserByName(signinMsg.getLogin());
            if (user == null) {
                ErrorMsg errorMsg = new ErrorMsg(message.getUuid(), new Exception(ErrorCode.DB_USER_NOT_FOUND.toString()));
                errorMsg.setFrom(address);
                errorMsg.setTo(message.getFrom());
                client.send(errorMsg);
                return;
            }
            if (!user.getPassword().equals(signinMsg.getPassword())) {
                ErrorMsg errorMsg = new ErrorMsg(message.getUuid(), new Exception(ErrorCode.DB_USER_PASSWORD_MISMATCH.toString()));
                errorMsg.setFrom(address);
                errorMsg.setTo(message.getFrom());
                client.send(errorMsg);
                return;
            }
            SigninResponseMsg signinResponseMsg = new SigninResponseMsg(message.getUuid(), user.getId());
            signinResponseMsg.setTo(message.getFrom());
            signinResponseMsg.setFrom(address);
            client.send(signinResponseMsg);
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

        Thread.sleep(1_000_000);
        client.close();
        executorService.shutdown();
    }

}
