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
        paint.setColor(Color.BLUE); // Set the color to blue for robot-style customer
        paint.setStyle(Paint.Style.FILL); // Set the style to fill

        // Draw robot body
        canvas.drawRect(seatX - 30, seatY - 50, seatX + 30, seatY + 50, paint);

        // Draw robot head
        canvas.drawCircle(seatX, seatY - 70, 20, paint);

        // Draw robot eyes
        paint.setColor(Color.WHITE); // Set eye color to white
        canvas.drawCircle(seatX - 10, seatY - 80, 5, paint);
        canvas.drawCircle(seatX + 10, seatY - 80, 5, paint);

        // Draw robot antenna
        paint.setColor(Color.YELLOW); // Set antenna color to yellow
        canvas.drawLine(seatX, seatY - 90, seatX, seatY - 120, paint);
        canvas.drawCircle(seatX, seatY - 130, 5, paint);
    }
}
