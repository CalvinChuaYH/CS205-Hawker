package com.example.androidapp.gamelogic;

public class Chef implements Runnable {

    private Buffer buffer;

    public Chef (Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {

//        System.out.println("HELOOOOOOOOOOOOOOOOOOOOOO");

        while (true) {
            try {
                // Simulate cooking time
                Thread.sleep(2000);
                buffer.putOrder(new Order("🍔"));

            } catch (InterruptedException e) {
            }
        }
    }
}
