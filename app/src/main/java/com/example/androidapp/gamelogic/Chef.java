package com.example.androidapp.gamelogic;

public class Chef implements Runnable {

    private Buffer buffer;

    public Chef (Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            buffer.putFood();
        }
    }
}
