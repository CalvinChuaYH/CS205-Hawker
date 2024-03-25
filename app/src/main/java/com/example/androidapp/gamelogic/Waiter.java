package com.example.androidapp.gamelogic;

import com.example.androidapp.App_Objects.CollisionHandler;
import com.example.androidapp.App_Objects.Player;

public class Waiter implements Runnable {

    private Buffer buffer;
    private CollisionHandler collisionHandler;

    public Waiter (Buffer buffer, CollisionHandler collisionHandler) {
        this.buffer = buffer;
        this.collisionHandler = collisionHandler;
    }

    @Override
    public void run() {
        while (true) {

            // If colliding with stall
            if (collisionHandler.isCollidingWithStall()) {
                buffer.takeFood();
            }
        }
    }
}
