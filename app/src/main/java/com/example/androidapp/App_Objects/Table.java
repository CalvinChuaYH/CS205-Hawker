package com.example.androidapp.App_Objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.androidapp.Joystick;

import java.util.ArrayList;

public class Table implements Roadblock {
    public int centerX;
    public int centerY; // Add centerY for the circle
    public int radius; // Add radius for the circle
    private final Paint paint = new Paint();
    public int id;

    public ArrayList<Customer> customers; // Array of customers
    public final int customerNum = 3;

    //Initialize stall and where it is
    public Table(int X, int Y, int radius, int id) {
        this.centerX = X;
        this.centerY = Y;
        this.radius = radius;
        this.id = id;

        // Initialize paint for drawing the stall
        paint.setColor(Color.WHITE); // Set the color of the stall to red
        paint.setStyle(Paint.Style.FILL); // Set the style to fill

        customers = new ArrayList<>(); // Initialize the array for 3 customers
        // Creating customer objects and placing them in seats
        for (int i = 0; i < customerNum; i++) {
            double angle = (2 * Math.PI / customerNum) * i; // Angle for each seat
            int seatX = (int) (centerX + radius * Math.cos(angle)); // X-coordinate of the seat
            int seatY = (int) (centerY + radius * Math.sin(angle)); // Y-coordinate of the seat
            customers.add(new Customer(seatX, seatY)); // Create customer and assign position
        }

    }

    //Drawing my stall
    public void draw(Canvas canvas) {
        // Draw the table as a circle
        canvas.drawCircle(centerX, centerY, radius, paint);

        // Draw seats around the circumference of the table
        for (int i = 0; i < customerNum; i++) {
            double angle = (2 * Math.PI / customerNum) * i; // Angle for each seat
            int seatX = (int) (centerX + radius * Math.cos(angle)); // X-coordinate of the seat
            int seatY = (int) (centerY + radius * Math.sin(angle)); // Y-coordinate of the seat
            canvas.drawCircle(seatX, seatY, 20, paint); // Draw the seat as a small circle
        }

        // Draw customers sitting in the seats
        for (Customer customer : customers) {
            customer.draw(canvas); // Draw each customer
        }
    }

    // Method to remove a customer from the table
    public void removeCustomer(int index) {
        customers.remove(index);
    }

    @Override
    public double[] handleCollide(Player player, Joystick joystick) {
        // Calculate the distance between the player's center and the table's center
        int adj = 50;
        double distanceX = player.positionX - centerX;
        double distanceY = player.positionY - centerY + adj;
        double distance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));

        // Check if the distance is less than or equal to the sum of player's radius and table's radius
        if (distance <= (player.radius + radius + adj)) {
            // If colliding with a table, adjust the new position
            double collisionAngle = Math.atan2(player.positionY - centerY, player.positionX - centerX);
            double newX = player.positionX + Math.cos(collisionAngle) * player.MAX_SPEED;
            double newY = player.positionY + Math.sin(collisionAngle) * player.MAX_SPEED;

            if (player.getHasFood() && !customers.isEmpty()) {
                //Handles player disappearing logic
                player.increaseServed();
                removeCustomer(0); //remove the customer
            }

            return new double[] {newX, newY};
        }

        return null;
    }
}
