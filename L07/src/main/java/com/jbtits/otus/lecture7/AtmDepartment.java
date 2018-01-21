package com.jbtits.otus.lecture7;

public class AtmDepartment {
    private Atm[] atms;

    public AtmDepartment(Atm ... atms) {
        this.atms = atms;
    }

    public int getAtmsCount() {
        return atms.length;
    }
}
