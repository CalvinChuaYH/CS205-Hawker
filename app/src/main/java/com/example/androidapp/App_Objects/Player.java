package com.example.androidapp.App_Objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.androidapp.GameLoop;
import com.example.androidapp.Joystick;
import com.example.androidapp.R;
import com.example.androidapp.gamelogic.Buffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class Player {
    public int served = 0;
    private static final double SPEED_PIXELS_PER_SEC = 400.0;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SEC / GameLoop.MAX_UPS;
    private double positionX;
    private double positionY;
    private double radius;
    private Paint paint;
    private Stall stall;
    private Table[] tables;

    private Buffer buffer;

    private boolean hasFood = false;

    private HashMap<Integer, Stack<Integer>> map;

    public Player(Context context, double positionX, double positionY, double radius, Stall stall, Table[] tables, Buffer buffer) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.radius = radius;
        this.stall = stall;
        this.tables = tables;
        this.buffer = buffer;
        initPeopleDB();

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

        //Handles player disappearing logic
        playerCustomerLogic();

        //Handles Player colliding with table to "Bounce them back"
        for(Table table:tables){
            if(isCollidingWithTable(table)){
                // If colliding with a table, adjust the new position
                double collisionAngle = Math.atan2(positionY - table.centerY, positionX - table.centerX);
                newX = positionX + Math.cos(collisionAngle) * MAX_SPEED;
                newY = positionY + Math.sin(collisionAngle) * MAX_SPEED;
                break;
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
            if (buffer.isFoodReady()) {
                hasFood = true;
                buffer.setTakenFood();
            }
        }

        //If served 9 etc can stop game with this...
        System.out.println(served);
    }

    private void playerCustomerLogic(){
        // Check if the new position is colliding with any table
        for (Table table : tables) {
            if (isCollidingWithTable(table)) {
                System.out.println(map.get(table.id));
                if (hasFood && map.get(table.id).size() > 0) {
                    int randomCustomerIndex = map.get(table.id).pop();
                    served++;
                    Table.removeCustomerFromTable(table, randomCustomerIndex); //remove the customer
                    hasFood = false;
                }
                break; // No need to check other tables once collision is detected
            }
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

    //Initializes the map for removal of customers, once all empty could end game etc.
    public void initPeopleDB(){
        map = new HashMap<>();
        for(Table table:tables){
            Stack<Integer> stack = new Stack<>();
            stack.add(0);
            stack.add(1);
            stack.add(2);
            map.put(table.id, stack);
        }
    }
}
