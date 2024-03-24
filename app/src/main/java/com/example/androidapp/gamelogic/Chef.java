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
//            try {
//                // Simulate cooking time
//                Thread.sleep(5000);
//                buffer.putFood();
//
//            } catch (InterruptedException e) {
//            }
        }
    }
}
