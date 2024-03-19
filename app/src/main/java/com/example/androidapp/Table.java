package com.example.androidapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Table {
    public int centerX;
    public int centerY; // Add centerY for the circle
    public int radius; // Add radius for the circle
    private Paint paint;

    //Initialize stall and where it is
    public Table(Context context, int X, int Y, int radius) {
        this.centerX = X;
        this.centerY = Y;
        this.radius = radius;

        // Initialize paint for drawing the stall
        paint = new Paint();
        paint.setColor(Color.WHITE); // Set the color of the stall to red
        paint.setStyle(Paint.Style.FILL); // Set the style to fill
    }

    //Drawing my stall
    public void draw(Canvas canvas) {
        // Draw the table as a circle
        canvas.drawCircle(centerX, centerY, radius, paint);

        // Draw seats around the circumference of the table
        int numSeats = 3; // Number of seats
        for (int i = 0; i < numSeats; i++) {
            double angle = (2 * Math.PI / numSeats) * i; // Angle for each seat
            int seatX = (int) (centerX + radius * Math.cos(angle)); // X-coordinate of the seat
            int seatY = (int) (centerY + radius * Math.sin(angle)); // Y-coordinate of the seat
            canvas.drawCircle(seatX, seatY, 20, paint); // Draw the seat as a small circle
        }
    }
}
