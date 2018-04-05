package com.jbtits.otus.lecture15.front;

import com.jbtits.otus.lecture15.utils.SecutiryUtils;

public class SecurityServiceImpl implements SecurityService {
    private final String salt;

    public SecurityServiceImpl(final String salt) {
        this.salt = salt;
    }

    @Override
    public String encodePassword(String password) {
        return SecutiryUtils.encodePassword(password, salt);
    }
}
