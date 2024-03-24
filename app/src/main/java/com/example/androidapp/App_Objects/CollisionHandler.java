package com.example.androidapp.App_Objects;

import android.graphics.Canvas;

import com.example.androidapp.Joystick;

import java.util.*;

public class CollisionHandler {
    private final Player player;
    private final List<Roadblock> blocks = new ArrayList<>();
    public CollisionHandler(Player player, Stall stall, Table[] tables){
        this.player = player;
        blocks.add(stall);
        blocks.addAll(Arrays.asList(tables));
    }

    public void draw(Canvas canvas, Joystick joystick){
        player.draw(canvas, joystick);
    }

    public void update(Joystick joystick, int screenWidth, int screenHeight) {
        double newX = player.positionX + joystick.getActuatorX() * player.MAX_SPEED;
        double newY = player.positionY + joystick.getActuatorY() * player.MAX_SPEED;

        //Handles Player colliding with table to "Bounce them back"
        for (Roadblock block : blocks) {
            double[] coords;
            if ((coords = block.handleCollide(player, joystick)) != null) {
                newX = coords[0];
                newY = coords[1];
                break;
            }
        }

        // Keep the player within the screen bounds
        if (newX - player.radius >= 0 && newX + player.radius <= screenWidth) {
            player.positionX = newX;
        }
        if (newY - player.radius >= 0 && newY + player.radius <= screenHeight) {
            player.positionY = newY;
        }
    }

    public boolean allCustomerServed(){
        return player.getServed() == 9;
    }
}
