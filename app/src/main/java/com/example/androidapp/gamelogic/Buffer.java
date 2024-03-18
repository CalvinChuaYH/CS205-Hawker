package com.example.androidapp.gamelogic;

public class Buffer {

    private boolean isFull = false;

    public synchronized void putFood() {
        while (isFull) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        System.out.println("Chef prepared food");
        isFull = true;
        notifyAll();
    }

    public synchronized void takeFood() {
        while (!isFull) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        System.out.println("Waiter takes food");
        isFull = false;
        notifyAll();
    }
}
