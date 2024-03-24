package com.example.androidapp.gamelogic;

public class Waiter implements Runnable {

    private Buffer buffer;

    @Override
    public void run() {
        while (true) {
            try {
                buffer.takeOrder();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}
