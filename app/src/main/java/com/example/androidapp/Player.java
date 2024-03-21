package com.example.androidapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import androidx.core.content.ContextCompat;

public class Player {
    private static final double SPEED_PIXELS_PER_SEC = 400.0;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SEC / GameLoop.MAX_UPS;
    private double positionX;
    private double positionY;
    private double radius;
    private Paint paint;
    private Stall stall;
    private Table[] tables;

    public Player(Context context, double positionX, double positionY, double radius, Stall stall, Table[] tables) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.radius = radius;
        this.stall = stall;
        this.tables = tables;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.player);
        paint.setColor(color);
    }

    // Drawing the player on canvas
    public void draw(Canvas canvas, Joystick joystick) {
        // Head
        canvas.drawCircle((float) positionX, (float) positionY, (float) radius, paint);

        // Body
        canvas.drawRect((float) (positionX - radius / 2), (float) (positionY + radius),
                (float) (positionX + radius / 2), (float) (positionY + 3 * radius), paint);

        // Check joystick input to determine direction of rectangle
        if (joystick.getActuatorX() < 0) {
            canvas.drawRect((float) (positionX - 2 * radius), (float) (positionY + radius),
                    (float) (positionX - radius / 2), (float) (positionY + 2 * radius), paint);
        } else if (joystick.getActuatorX() > 0) {
            canvas.drawRect((float) (positionX + radius / 2), (float) (positionY + radius),
                    (float) (positionX + 2 * radius), (float) (positionY + 2 * radius), paint);
        }

        // Legs
        canvas.drawRect((float) (positionX - radius / 2), (float) (positionY + 3 * radius),
                (float) (positionX - radius / 4), (float) (positionY + 5 * radius), paint);
        canvas.drawRect((float) (positionX + radius / 4), (float) (positionY + 3 * radius),
                (float) (positionX + radius / 2), (float) (positionY + 5 * radius), paint);
    }

    // Handling player movement
    public void update(Joystick joystick, int screenWidth, int screenHeight) {
        // Calculate the new position based on joystick input
        double newX = positionX + joystick.getActuatorX() * MAX_SPEED;
        double newY = positionY + joystick.getActuatorY() * MAX_SPEED;

        // Check if the new position is colliding with any table
        for (Table table : tables) {
            if (isCollidingWithTable(table)) {
                Log.d("Player", "Collided with table: " + table.id);
                int randomCustomer = (int) (Math.random() * 3);
                Table.removeCustomerFromTable(table, randomCustomer);
                // If colliding with a table, adjust the new position
                double collisionAngle = Math.atan2(positionY - table.centerY, positionX - table.centerX);
                newX = positionX + Math.cos(collisionAngle) * MAX_SPEED;
                newY = positionY + Math.sin(collisionAngle) * MAX_SPEED;
                break; // No need to check other tables once collision is detected
            }
        }

        // Keep the player within the screen bounds
        if (newX - radius >= 0 && newX + radius <= screenWidth) {
            positionX = newX;
        }
        if (newY - radius >= 0 && newY + radius <= screenHeight) {
            positionY = newY;
        }

        // Checks if colliding with stall
        if (isCollidingWithStall()) {
            positionX -= joystick.getActuatorX() * MAX_SPEED;
            positionY -= joystick.getActuatorY() * MAX_SPEED;
        }
    }

    // Method to check collision with the stall
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

    // Method to check collision with a specific table
    public boolean isCollidingWithTable(Table table) {
        // Calculate the distance between the player's center and the table's center
        double distanceX = positionX - table.centerX;
        double distanceY = positionY - table.centerY;
        double distance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));

        // Check if the distance is less than or equal to the sum of player's radius and table's radius
        return distance <= (radius + table.radius);
    }
}
