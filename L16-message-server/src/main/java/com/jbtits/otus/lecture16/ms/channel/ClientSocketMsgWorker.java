package com.jbtits.otus.lecture16.ms.channel;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by tully.
 */
public class ClientSocketMsgWorker extends SocketMsgWorker {

    private final Socket socket;

    protected ClientSocketMsgWorker(String host, int port, int additionalWorkersCount) throws IOException {
        this(new Socket(host, port), additionalWorkersCount);
    }

    public ClientSocketMsgWorker(String host, int port) throws IOException {
        this(new Socket(host, port), 0);
    }

    private ClientSocketMsgWorker(Socket socket, int additionalWorkersCount) throws IOException {
        super(socket, additionalWorkersCount);
        this.socket = socket;
    }

    public void close() throws IOException {
        super.close();
        socket.close();
    }
}
