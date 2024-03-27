package com.example.androidapp.App_Objects;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.androidapp.GameLoop;
import com.example.androidapp.Joystick;
import com.example.androidapp.R;
import com.example.androidapp.gamelogic.Buffer;

import java.util.HashMap;
import java.util.Stack;

public class Player {
    public final double SPEED_PIXELS_PER_SEC = 400.0;
    public final double MAX_SPEED = SPEED_PIXELS_PER_SEC / GameLoop.MAX_UPS;
    public double positionX;
    public double positionY;
    public double radius;
    private final Paint paint = new Paint();

    private boolean hasFood = false;
    public LightingColorFilter noFoodColor;
    public LightingColorFilter hasFoodColor;

    private Bitmap image;

    @SuppressLint("ResourceAsColor")
    public Player(Context context, double positionX, double positionY, double radius) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.radius = radius;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.waiter, options);

        // Get color values from resources
        int playerColor = ContextCompat.getColor(context, R.color.player);
        int greenColor = ContextCompat.getColor(context, R.color.green);

        // Initialize LightingColorFilter objects
        noFoodColor = new LightingColorFilter(playerColor, 0); // No change in color
        hasFoodColor = new LightingColorFilter(greenColor, 0); // No change in color
        paint.setColorFilter(noFoodColor);
    }

    public void setHasFood(boolean hasFood) {
        this.hasFood = hasFood;
        paint.setColorFilter(hasFood ? hasFoodColor : noFoodColor);
    }

    // Drawing the player on canvas
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, (float) positionX, (float) positionY, paint);
    }

    public boolean getHasFood(){
        return hasFood;
    }

}
