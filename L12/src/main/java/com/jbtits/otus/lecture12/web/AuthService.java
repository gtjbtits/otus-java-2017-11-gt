package com.jbtits.otus.lecture12.web;

import com.jbtits.otus.lecture12.dbService.DBService;
import com.jbtits.otus.lecture12.dbService.dataSets.UserDataSet;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class AuthService {
    private static final String SALT = "312ke0~!#2432nF@$R$#&";

    private final DBService dbService;

    AuthService(DBService dbService) {
        this.dbService = dbService;
    }

    public boolean authorize(AuthFilter.Credentials credentials) {
        if (credentials == null) {
            return false;
        }
        UserDataSet user = dbService.getUserByName(credentials.getLogin());
        if (user == null) {
            dbService.saveUser(createUser(credentials));
            return true;
        }
        return user.getPassword().equals(encodePassword(credentials.getPassword()));
    }

    public String encodePassword(String password) {
        byte[] bytes;
        MessageDigest md;
        String saltedPassword = password + SALT;
        try {
            bytes = saltedPassword.getBytes("UTF-8");
            md = MessageDigest.getInstance("MD5");
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return toHex(md.digest(bytes));
    }

    public UserDataSet createUser(AuthFilter.Credentials credentials) {
        UserDataSet user = new UserDataSet();
        user.setName(credentials.getLogin());
        user.setPassword(encodePassword(credentials.getPassword()));
        return user;
    }

    private static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }
}
