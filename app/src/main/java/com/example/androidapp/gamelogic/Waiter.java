package com.example.androidapp.gamelogic;

import com.example.androidapp.App_Objects.Player;
import com.example.androidapp.App_Objects.Stall;

public class Waiter implements Runnable {

    private Player player;
    private Stall stall;

    public Waiter (Player player, Stall stall) {
        this.player = player;
        this.stall = stall;
    }

    @Override
    public void run() {
        while (true) {

            // If colliding with stall
//            if (stall.handleCollide(player, null)) {
//            }
        }
    }
}
