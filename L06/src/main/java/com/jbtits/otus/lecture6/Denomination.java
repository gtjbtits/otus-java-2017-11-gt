package com.jbtits.otus.lecture6;

public enum Denomination {
    TEN(10), HUNDRED(100), THOUSAND(1000);

    private final int value;

    Denomination(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
