package com.example.androidapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Customer {
    public int seatX;
    public int seatY;

    public Customer(int seatX, int seatY) {
        this.seatX = seatX;
        this.seatY = seatY;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED); // Set the color of the stall to red
        paint.setStyle(Paint.Style.FILL); // Set the style to fill
        canvas.drawCircle(seatX, seatY, 40, paint);
    }
}
