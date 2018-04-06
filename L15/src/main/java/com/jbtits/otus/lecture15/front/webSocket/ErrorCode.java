package com.jbtits.otus.lecture15.front.webSocket;

public enum ErrorCode {
    JSON_BAD_ACTION(304),
    JSON_REQUIRED_ERROR(303),
    JSON_SERIALIZE_ERROR(302),
    JSON_PARSE_ERROR(301),
//    JSON_ERROR(300),
    MESSAGE_SYSTEM_ERROR(200),
    DB_USER_PASSWORD_MISMATCH(103),
    DB_USER_NOT_FOUND(102),
    DB_USER_ALREADY_EXISTS(101),
//    DB_ERROR(100),
    UNSUPPORTED_ACTION(4),
    WS_MESSAGE_NOT_VALID(3),
    UNKNOWN(1);

    private final int code;

    ErrorCode(final int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return String.valueOf(code);
    }
}
