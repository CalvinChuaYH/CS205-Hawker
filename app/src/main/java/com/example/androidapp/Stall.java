package com.example.androidapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Stall {
    public int centerX;
    public int topY;
    public int width;
    public int height;
    private Paint paint;

    //Initialize stall and where it is
    public Stall(Context context, int centerScreenX, int topScreenY) {
        this.centerX = centerScreenX;
        this.topY = topScreenY;
        this.width = 200; // Set the width of the stall
        this.height = 100; // Set the height of the stall

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
}
