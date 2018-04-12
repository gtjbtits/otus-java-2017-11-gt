package com.jbtits.otus.lecture16.ms.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExceptionUtils {
    private static final Logger logger = LogManager.getLogger(ExceptionUtils.class.getName());

    public static RuntimeException fatalError(String message, Throwable cause) {
        logger.error(message, cause);
        return new RuntimeException(message, cause);
    }

    public static RuntimeException fatalError(String message) {
        return fatalError(message, null);
    }
}
