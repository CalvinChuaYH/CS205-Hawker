package com.example.androidapp.gamelogic;

public class Chef implements Runnable {

    private Buffer buffer;

    @Override
    public void run() {

        while (true) {
            try {
                // Simulate cooking time
                Thread.sleep(5000);
                buffer.putFood();
            } catch (InterruptedException e) {
            }
        }
    }
}
