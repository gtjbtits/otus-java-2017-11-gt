package com.jbtits.otus.lecture16.ms.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecutiryUtils {
    public static String encodePassword(String password, String salt) {
        byte[] bytes;
        MessageDigest md;
        String saltedPassword = password + salt;
        try {
            bytes = saltedPassword.getBytes("UTF-8");
            md = MessageDigest.getInstance("MD5");
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return toHex(md.digest(bytes));
    }

    private static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }
}
