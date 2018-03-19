package com.jbtits.otus.lecture2;

public class Main {
    public static void main(String[] args) {
        Facility facility = new Facility();
        facility.sizeOfReference();
        facility.growContainerSize();
        facility.sizeOf("Object", () -> new Object());
        facility.sizeOf("Empty String", () -> new String(new char[0]));
        facility.sizeOf("String with pool", () -> new String());
        facility.sizeOf("String without pool", () -> new String(new char[2]));
        facility.sizeOf("Zero length array", () -> new System[0]);
    }
}
