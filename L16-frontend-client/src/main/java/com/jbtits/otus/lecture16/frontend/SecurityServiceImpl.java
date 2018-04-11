package com.jbtits.otus.lecture16.frontend;

import com.jbtits.otus.lecture16.ms.utils.SecutiryUtils;

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
