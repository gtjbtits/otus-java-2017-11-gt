package com.jbtits.otus.lecture16.ms;

import com.jbtits.otus.lecture16.ms.app.ProcessRunner;
import com.jbtits.otus.lecture16.ms.config.MSServerConfiguration;
import com.jbtits.otus.lecture16.ms.runner.ProcessRunnerImpl;
import com.jbtits.otus.lecture16.ms.server.MessageExchangeServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by tully.
 */
public class ServerMain {
    private static final Logger logger = LogManager.getLogger(ServerMain.class.getName());

    private static final String DB_CLIENT_START_COMMAND = "java -jar C:\\projects\\otus-java-2017-11-gt\\L16-dbservice-client\\target\\dbservice-client.jar";
    private static final String FRONT_CLIENT_START_COMMAND = "cmd.exe /c copy /Y C:\\projects\\otus-java-2017-11-gt\\L16-frontend-client\\target\\frontend.war C:\\jetty\\webapps\\root.war";
    private static final int CLIENT_START_DELAY_SEC = 5;
    private static final int THREAD_POOL_SIZE = 2;

    public static void main(String[] args) throws Exception {
        new ServerMain().start();
    }

    private void start() throws Exception {
        logger.info("Starting Message Sever...");
        MSServerConfiguration configuration = new MSServerConfiguration();

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(THREAD_POOL_SIZE);
//        startClient(executorService, DB_CLIENT_START_COMMAND);
//        startClient(executorService, DB_CLIENT_START_COMMAND);
//        startClient(executorService, FRONT_CLIENT_START_COMMAND);

        MessageExchangeServer server = new MessageExchangeServer(configuration.getPort());
        server.start();
        executorService.shutdown();
    }

    private void startClient(ScheduledExecutorService executorService, String command) {
        executorService.schedule(() -> {
            try {
                ProcessRunner pr = new ProcessRunnerImpl();
                pr.start(command);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(pr.getOutput());
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }, CLIENT_START_DELAY_SEC, TimeUnit.SECONDS);
    }

}
