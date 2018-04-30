package com.jbtits.gt.otus.l5.objects;

public class Grass {
    private String color;
    public Grass() {
        color = "green";
    }
    public void winterHasCome() {
        color = "orange";
    }
    public String getColor() {
        return color;
    }
}
