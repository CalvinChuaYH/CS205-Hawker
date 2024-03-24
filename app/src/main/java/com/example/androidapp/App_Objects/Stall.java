package com.example.androidapp.App_Objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.androidapp.Joystick;
import com.example.androidapp.gamelogic.Buffer;

public class Stall implements Roadblock {
    public int centerX;
    public int topY;
    public int width;
    public int height;
    private Paint paint;
    private Buffer buffer;

    //Initialize stall and where it is
    public Stall(int centerScreenX, int topScreenY, Buffer buffer) {
        this.centerX = centerScreenX;
        this.topY = topScreenY;
        this.width = 200; // Set the width of the stall
        this.height = 100; // Set the height of the stall
        this.buffer = buffer;

        // Initialize paint for drawing the stall
        paint = new Paint();
        paint.setColor(Color.RED); // Set the color of the stall to red
        paint.setStyle(Paint.Style.FILL); // Set the style to fill
    }

    //Drawing my stall
    public void draw(Canvas canvas) {
        // Draw the stall as a rectangle
        canvas.drawRect(centerX - (float) width / 2, topY, centerX + (float) width / 2, topY + height, paint);
    }

    @Override
    public double[] handleCollide(Player player, Joystick joystick) {
        // Calculate the half-width and half-height of the stall
        int halfWidth = width / 2;

        // Calculate the closest point on the stall to the player's center
        double closestX = Math.max(Math.min(player.positionX, centerX + halfWidth), centerX - halfWidth);
        double closestY = Math.max(Math.min(player.positionY, topY + height), topY);

        // Calculate the distance between the player's center and the closest point on the stall
        double distanceX = player.positionX - closestX;
        double distanceY = player.positionY - closestY;
        double distance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));

        // Check if the distance is less than or equal to the player's radius
        if (distance <= player.radius) {
            double newX = player.positionX - joystick.getActuatorX() * player.MAX_SPEED;
            double newY = player.positionY - joystick.getActuatorY() * player.MAX_SPEED;
            if (buffer.isFoodReady()) {
                buffer.takeFood();
                player.setHasFood(true);
            }

            return new double[] {newX, newY};
        }

        return null;
    }
}
