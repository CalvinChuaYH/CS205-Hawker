package com.example.androidapp.gamelogic;

public class Score {
    private String username;
    private int value;

    public Score(String username, int value) {
        this.username = username;
        this.value = value;
    }

    public String getUsername() {
        return username;
    }

    public int getValue() {
        return value;
    }
}
