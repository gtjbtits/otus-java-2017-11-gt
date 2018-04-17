package com.jbtits.otus.lecture16.ms.app;

import com.jbtits.otus.lecture16.ms.ServerMain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

abstract public class Shutdownable {
    private static final Logger logger = LogManager.getLogger(Shutdownable.class.getName());
    private final AtomicBoolean shutdowned;

    private Shutdownable root;

    public Shutdownable() {
        this(null);
    }

    public Shutdownable(Shutdownable root) {
        shutdowned = new AtomicBoolean(false);
        this.root = root;
    }

    abstract public void onShutdown() throws Exception;

    public void shutdown(String message, Throwable cause) {
        if (shutdowned.compareAndSet(true, true)) {
            logger.debug("Try to call shutdown again with message: " + message, cause);
            return;
        }
        try {
            onShutdown();
        } catch (Exception e) {
            logger.error("Shutdown failed: " + message, e);
        }
        if (root != null) {
            root.shutdown(message, cause);
        } else {
            logger.error(message, cause);
            throw new RuntimeException(message, cause);
        }
    }

    public void setRoot(Shutdownable root) {
        this.root = root;
    }
}
