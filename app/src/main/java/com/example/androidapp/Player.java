package com.example.androidapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

public class Player {
    private static final double SPEED_PIXELS_PER_SEC = 400.0;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SEC/ GameLoop.MAX_UPS;
    private double positionX;
    private double positionY;
    private double radius;
    private Paint paint;
    private double velocityX;
    private double velocityY;

    private Stall stall;

    public Player(Context context, double positionX, double positionY, double radius, Stall stall){
        this.positionX = positionX;
        this.positionY = positionY;
        this.radius = radius;
        this.stall = stall;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.player);
        paint.setColor(color);
    }

    //Drawing my player canvas
    public void draw(Canvas canvas) {
        // Head
        canvas.drawCircle((float) positionX, (float) positionY, (float) radius, paint);

        // Body
        canvas.drawRect((float) (positionX - radius / 2), (float) (positionY + radius),
                (float) (positionX + radius / 2), (float) (positionY + 3 * radius), paint);

        // Arms
        canvas.drawRect((float) (positionX - 2 * radius), (float) (positionY + radius),
                (float) (positionX - radius / 2), (float) (positionY + 2 * radius), paint);
        canvas.drawRect((float) (positionX + radius / 2), (float) (positionY + radius),
                (float) (positionX + 2 * radius), (float) (positionY + 2 * radius), paint);

        // Legs
        canvas.drawRect((float) (positionX - radius / 2), (float) (positionY + 3 * radius),
                (float) (positionX - radius / 4), (float) (positionY + 5 * radius), paint);
        canvas.drawRect((float) (positionX + radius / 4), (float) (positionY + 3 * radius),
                (float) (positionX + radius / 2), (float) (positionY + 5 * radius), paint);
    }


    //Handle when the player is moving
    public void update(Joystick joystick, int screenWidth, int screenHeight) {
        // Calculate the new position based on joystick input
        double newX = positionX + joystick.getActuatorX() * MAX_SPEED;
        double newY = positionY + joystick.getActuatorY() * MAX_SPEED;

        // Keep the player within the screen bounds
        if (newX - radius >= 0 && newX + radius <= screenWidth) {
            positionX = newX;
        }
        if (newY - radius >= 0 && newY + radius <= screenHeight) {
            positionY = newY;
        }

        //Checks if colliding with stall, maybe can handle when person takes order here
        if(isCollidingWithStall()){
            positionX -= joystick.getActuatorX() * MAX_SPEED;
            positionY -= joystick.getActuatorY() * MAX_SPEED;
        }
    }

    public void setPosition(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    //Checks if the user collides with the stall object
    public boolean isCollidingWithStall() {
        // Calculate the half-width and half-height of the stall
        int halfWidth = stall.width / 2;
        int halfHeight = stall.height / 2;

        // Calculate the closest point on the stall to the player's center
        double closestX = Math.max(Math.min(positionX, stall.centerX + halfWidth), stall.centerX - halfWidth);
        double closestY = Math.max(Math.min(positionY, stall.topY + stall.height), stall.topY);

        // Calculate the distance between the player's center and the closest point on the stall
        double distanceX = positionX - closestX;
        double distanceY = positionY - closestY;
        double distance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));

        // Check if the distance is less than or equal to the player's radius
        return distance <= radius;
    }
}
