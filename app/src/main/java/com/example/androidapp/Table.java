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
    public int id;

    public Customer[] customers; // Array of customers

    //Initialize stall and where it is
    public Table(Context context, int X, int Y, int radius, int id) {
        this.centerX = X;
        this.centerY = Y;
        this.radius = radius;
        this.id = id;

        // Initialize paint for drawing the stall
        paint = new Paint();
        paint.setColor(Color.WHITE); // Set the color of the stall to red
        paint.setStyle(Paint.Style.FILL); // Set the style to fill

        customers = new Customer[3]; // Initialize the array for 3 customers
        // Creating customer objects and placing them in seats
        for (int i = 0; i < customers.length; i++) {
            double angle = (2 * Math.PI / customers.length) * i; // Angle for each seat
            int seatX = (int) (centerX + radius * Math.cos(angle)); // X-coordinate of the seat
            int seatY = (int) (centerY + radius * Math.sin(angle)); // Y-coordinate of the seat
            customers[i] = new Customer(seatX, seatY); // Create customer and assign position
        }
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

        // Draw customers sitting in the seats
        for (Customer customer : customers) {
            if (customer != null) {
                customer.draw(canvas); // Draw each customer
            }
        }
    }
}
