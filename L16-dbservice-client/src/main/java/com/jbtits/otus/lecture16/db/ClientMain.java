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
import com.jbtits.otus.lecture16.ms.config.MSClientConfiguration;
import com.jbtits.otus.lecture16.ms.dataSets.DataSet;
import com.jbtits.otus.lecture16.ms.dataSets.MessageDataSet;
import com.jbtits.otus.lecture16.ms.dataSets.UserDataSet;
import com.jbtits.otus.lecture16.ms.messages.*;
import com.jbtits.otus.lecture16.ms.messages.error.ErrorCode;
import com.jbtits.otus.lecture16.ms.messages.error.ErrorMsg;
import com.jbtits.otus.lecture16.ms.utils.ExceptionUtils;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
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
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(ClientMain.class.getName());
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

    public void shutdown() {
        dbService.shutdown();
    }

    private RuntimeException fatalError(String message, Throwable cause) {
        shutdown();
        return ExceptionUtils.fatalError(message, cause);
    }

    private RuntimeException fatalError(String message) {
        return fatalError(message, null);
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

    public static void main(String[] args) {
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
        logger.info("Client registered: " + address.getUuid());
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void start() {
        MSClientConfiguration msConfig = new MSClientConfiguration();
        final SocketMsgWorker client;
        try {
            client = new ClientSocketMsgWorker(msConfig.getServerHost(), msConfig.getServerPort());
        } catch (IOException e) {
            throw fatalError("Unable connect to server " + msConfig.getServerHost() + ":" + msConfig.getServerPort());
        }
        client.init();

        handshake(client);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                while (true) {
                    Msg msg = client.take();
                    logger.info("Incoming message [" + msg.getUuid() + "]");
                    logger.debug(msg);
                    Class<? extends Msg> clazz = Msg.class;
                    try {
                        clazz = (Class<? extends Msg>) Class.forName(msg.getClassName());
                    } catch (ClassNotFoundException e) {
                        logger.warn("Skip message with unknown class: " + msg.getClassName(), e);
                        logger.debug(msg);
                        continue;
                    }
                    if (handlers.containsKey(clazz)) {
                        handlers.get(clazz).accept(msg, client);
                    } else {
                        logger.warn("No handler for message with class: " + msg.getClassName());
                        logger.debug(msg);
                    }
                }
            } catch (InterruptedException e) {
                logger.error("ExecutorService interrupted", e);
            }
        });

//        try {
//            client.close();
//        } catch (IOException e) {
//            logger.error("Error at closing client connection", e);
//        }
//        executorService.shutdown();
    }

}
