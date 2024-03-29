package com.example.androidapp.gamelogic;

public class Buffer {
    private boolean isFull = false;

    public synchronized void putFood() {
        while (isFull) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        simulateTime(2000);
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

    public boolean isFoodReady() {
        return isFull;
    }

    public void simulateTime(int time) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
