package com.jbtits.otus.lecture16.db;

import com.jbtits.otus.lecture16.db.cache.CacheServiceImpl;
import com.jbtits.otus.lecture16.db.dbService.DBService;
import com.jbtits.otus.lecture16.db.dbService.DBServiceHibernateImpl;
import com.jbtits.otus.lecture16.ms.app.ClientType;
import com.jbtits.otus.lecture16.ms.app.Msg;
import com.jbtits.otus.lecture16.ms.app.MsgWorker;
import com.jbtits.otus.lecture16.ms.channel.ClientMsgAddressee;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

/**
 * Created by tully.
 */
public class ClientMain {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(ClientMain.class.getName());
    private final DBService dbService;

    public ClientMain() {
        dbService = new DBServiceHibernateImpl(new CacheServiceImpl<String, DataSet>(1000, 1000));
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

    private BiConsumer<Msg, ClientMsgAddressee> handleSignup() {
        return (message, client) -> {
            SignupMsg signupMsg = (SignupMsg) message;
            if (dbService.getUserByName(signupMsg.getLogin()) != null) {
                ErrorMsg errorMsg = new ErrorMsg(message.getUuid(), new Exception(ErrorCode.DB_USER_ALREADY_EXISTS.toString()));
                errorMsg.setFrom(client.getAddress());
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
            signupResponseMsg.setFrom(client.getAddress());
            client.send(signupResponseMsg);
        };
    }

    private BiConsumer<Msg, ClientMsgAddressee> handleSendMessage() {
        return (message, client) -> {
            SendMessageMsg sendMessageMsg = (SendMessageMsg) message;
            MessageDataSet messageDataSet = new MessageDataSet();
            messageDataSet.setText(sendMessageMsg.getText());
            dbService.saveMessage(messageDataSet, sendMessageMsg.getUserId());

            BroadcastMessageMsg broadcastMessageMsg = new BroadcastMessageMsg(message.getUuid(), messageDataSet.getText(),
                messageDataSet.getUser().getName(), messageDataSet.getCreated());
            broadcastMessageMsg.setTo(message.getFrom());
            broadcastMessageMsg.setFrom(client.getAddress());
            client.send(broadcastMessageMsg);
        };
    }

    private BiConsumer<Msg, ClientMsgAddressee> handleSignin() {
        return (message, client) -> {
            SigninMsg signinMsg = (SigninMsg) message;
            UserDataSet user = dbService.getUserByName(signinMsg.getLogin());
            if (user == null) {
                ErrorMsg errorMsg = new ErrorMsg(message.getUuid(), new Exception(ErrorCode.DB_USER_NOT_FOUND.toString()));
                errorMsg.setFrom(client.getAddress());
                errorMsg.setTo(message.getFrom());
                client.send(errorMsg);
                return;
            }
            if (!user.getPassword().equals(signinMsg.getPassword())) {
                ErrorMsg errorMsg = new ErrorMsg(message.getUuid(), new Exception(ErrorCode.DB_USER_PASSWORD_MISMATCH.toString()));
                errorMsg.setFrom(client.getAddress());
                errorMsg.setTo(message.getFrom());
                client.send(errorMsg);
                return;
            }
            SigninResponseMsg signinResponseMsg = new SigninResponseMsg(message.getUuid(), user.getId());
            signinResponseMsg.setTo(message.getFrom());
            signinResponseMsg.setFrom(client.getAddress());
            client.send(signinResponseMsg);
        };
    }

    public static void main(String[] args) {
        new ClientMain().start();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void start() {
        MSClientConfiguration msConfig = new MSClientConfiguration();
        final ClientMsgAddressee client;
        try {
            client = new ClientMsgAddressee(msConfig.getServerHost(), msConfig.getServerPort(), ClientType.DB_SERVICE);
        } catch (IOException e) {
            throw fatalError("Unable connect to server " + msConfig.getServerHost() + ":" + msConfig.getServerPort());
        }
        client.init();
        client.addHandler(SignupMsg.class, handleSignup());
        client.addHandler(SigninMsg.class, handleSignin());
        client.addHandler(SendMessageMsg.class, handleSendMessage());
        // TODO: wait for client
        try {
            Thread.sleep(1_000_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
