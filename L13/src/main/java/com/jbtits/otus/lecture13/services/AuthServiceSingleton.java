package com.jbtits.otus.lecture13.services;

import com.jbtits.otus.lecture13.web.AuthService;

public class AuthServiceSingleton {
    private static final AuthService authService = new AuthService(DBServiceSingleton.get());

    public static AuthService get() {
        return authService;
    }
}
