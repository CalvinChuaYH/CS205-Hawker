package com.example.androidapp.App_Objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.androidapp.GameLoop;
import com.example.androidapp.Joystick;
import com.example.androidapp.R;
import com.example.androidapp.gamelogic.Buffer;

import java.util.HashMap;
import java.util.Stack;

public class Player {
    public int served = 0;
    public final double SPEED_PIXELS_PER_SEC = 400.0;
    public final double MAX_SPEED = SPEED_PIXELS_PER_SEC / GameLoop.MAX_UPS;
    public double positionX;
    public double positionY;
    public double radius;
    private Paint paint;

    private boolean hasFood = false;

    private HashMap<Integer, Stack<Integer>> map;

    public int noFoodColor;
    public int hasFoodColor;

    public Player(Context context, double positionX, double positionY, double radius) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.radius = radius;

        paint = new Paint();
        noFoodColor = ContextCompat.getColor(context, R.color.player);
        hasFoodColor = ContextCompat.getColor(context, R.color.magenta);
        setColor(hasFood,noFoodColor,hasFoodColor);
    }

    public void setColor(boolean hasFood, int noFoodColor, int hasFoodColor) {
        paint.setColor(hasFood ? hasFoodColor:noFoodColor);
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
}
