package com.example.androidapp.gamelogic;

import com.example.androidapp.App_Objects.Player;

public class Waiter implements Runnable {

    private Buffer buffer;
    private Player player;

    public Waiter (Buffer buffer, Player player) {
        this.buffer = buffer;
        this.player = player;
    }

    @Override
    public void run() {
        while (true) {

            // If colliding with stall
            if (player.isCollidingWithStall()) {
                buffer.takeFood();
            }
        }
    }
}
