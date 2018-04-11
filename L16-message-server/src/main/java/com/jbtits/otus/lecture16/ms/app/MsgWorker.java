package com.jbtits.otus.lecture16.ms.app;

import com.jbtits.otus.lecture16.ms.channel.Blocks;

import java.io.IOException;

/**
 * Created by tully.
 */
public interface MsgWorker {
    void send(Msg msg);

    Msg pool();

    @Blocks
    Msg take() throws InterruptedException;

    void close() throws IOException;
}
