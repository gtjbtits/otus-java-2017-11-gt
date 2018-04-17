package com.jbtits.otus.lecture16.ms.channel;

import com.jbtits.otus.lecture16.ms.app.Msg;
import com.jbtits.otus.lecture16.ms.app.MsgWorker;
import com.google.gson.Gson;
import com.jbtits.otus.lecture16.ms.app.Shutdownable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by tully.
 */
public class SocketMsgWorker extends Shutdownable implements MsgWorker {
    private static final Logger logger = Logger.getLogger(SocketMsgWorker.class.getName());
    private static final int WORKERS_COUNT = 2;

    private final BlockingQueue<Msg> output = new LinkedBlockingQueue<>();
    private final BlockingQueue<Msg> input = new LinkedBlockingQueue<>();

    protected final ExecutorService executor;
    private final Socket socket;

    protected SocketMsgWorker(Socket socket, int additionalWorkersCount) {
        this.socket = socket;
        this.executor = Executors.newFixedThreadPool(WORKERS_COUNT + additionalWorkersCount);
    }

    public SocketMsgWorker(Socket socket) {
        this(socket, WORKERS_COUNT);
    }

    @Override
    public void send(Msg msg) {
        output.add(msg);
    }

    @Override
    public Msg pool() {
        return input.poll();
    }

    @Override
    public Msg take() throws InterruptedException {
        return input.take();
    }

    @Override
    public void close() throws IOException {
        executor.shutdown();
    }

    public void init() {
        executor.execute(this::sendMessage);
        executor.execute(this::receiveMessage);
    }

    @Blocks
    private void sendMessage() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            while (socket.isConnected()) {
                Msg msg = output.take(); //blocks
                String json = new Gson().toJson(msg);
                out.println(json);
                out.println();//line with json + an empty line
            }
        } catch (InterruptedException | IOException e) {
            shutdown("Send messages thread has been failed", e);
        }
    }

    @Blocks
    private void receiveMessage() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) { //blocks
                //System.out.println("Message received: " + inputLine);
                stringBuilder.append(inputLine);
                if (inputLine.isEmpty()) { //empty line is the end of the message
                    String json = stringBuilder.toString();
                    Msg msg = getMsgFromJSON(json);
                    input.add(msg);
                    stringBuilder = new StringBuilder();
                }
            }
        } catch (ClassNotFoundException | IOException | ParseException e) {
            shutdown("Receive messages thread has been failed", e);
        }
    }

    private static Msg getMsgFromJSON(String json) throws ParseException, ClassNotFoundException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
        String className = (String) jsonObject.get(Msg.CLASS_NAME_VARIABLE);
        Class<?> msgClass = Class.forName(className);
        return (Msg) new Gson().fromJson(json, msgClass);
    }

    @Override
    public void onShutdown() {
        close();
    }
}
