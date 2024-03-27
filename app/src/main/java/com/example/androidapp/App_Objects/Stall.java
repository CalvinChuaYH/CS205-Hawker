package com.example.androidapp.App_Objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.androidapp.R;
import com.example.androidapp.Joystick;
import com.example.androidapp.gamelogic.Buffer;

public class Stall implements Roadblock {
    public int centerX;
    public int topY;
    public int width;
    public int height;
    public Bitmap image;
    private Buffer buffer;

    //Initialize stall and where it is
    public Stall(Context context, int centerScreenX, int topScreenY, Buffer buffer) {
        this.centerX = centerScreenX;
        this.topY = topScreenY;
        this.buffer = buffer;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.table, options);

        this.width = image.getWidth(); // Set the width of the stall
        this.height = image.getHeight() - 100; // Set the height of the stall
    }

    //Drawing my stall
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, centerX - (float) image.getWidth() / 2, topY, null);
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
            double collisionAngle = Math.atan2(player.positionY - topY - height, player.positionX - centerX);
            double newX = player.positionX + Math.cos(collisionAngle) * player.MAX_SPEED;
            double newY = player.positionY + Math.sin(collisionAngle) * player.MAX_SPEED;
            if (buffer.isFoodReady()) {
                buffer.takeFood();
                player.setHasFood(true);
            }

            return new double[] {newX, newY};
        }

        return null;
    }
}
