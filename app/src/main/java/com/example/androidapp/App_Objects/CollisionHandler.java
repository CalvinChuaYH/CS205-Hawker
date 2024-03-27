package com.example.androidapp.App_Objects;

import android.graphics.Canvas;

import com.example.androidapp.Joystick;
import com.example.androidapp.gamelogic.Buffer;

import java.util.HashMap;
import java.util.Stack;

public class CollisionHandler {
    public int served = 0;
    private HashMap<Integer, Stack<Integer>> map;
    private Player player;
    private Stall stall;
    private Table[] tables;
    private Buffer buffer;
    public CollisionHandler(Player player, Stall stall, Table[] tables, Buffer buffer){
        this.player = player;
        this.stall = stall;
        this.tables = tables;
        this.buffer = buffer;
        initPeopleDB();
    }

    public void draw(Canvas canvas, Joystick joystick){
        player.draw(canvas, joystick);
    }

    public void update(Joystick joystick, int screenWidth, int screenHeight) {
        double newX = player.positionX + joystick.getActuatorX() * player.MAX_SPEED;
        double newY = player.positionY + joystick.getActuatorY() * player.MAX_SPEED;

        //Handles player disappearing logic
        playerCustomerLogic();

        //Handles Player colliding with table to "Bounce them back"
        for(Table table:tables){
            if(isCollidingWithTable(table)){
                // If colliding with a table, adjust the new position
                double collisionAngle = Math.atan2(player.positionY - table.centerY, player.positionX - table.centerX);
                newX = player.positionX + Math.cos(collisionAngle) * player.MAX_SPEED;
                newY = player.positionY + Math.sin(collisionAngle) * player.MAX_SPEED;
                break;
            }
        }

        // Keep the player within the screen bounds
        if (newX - player.radius >= 0 && newX + player.radius <= screenWidth) {
            player.positionX = newX;
        }
        if (newY - player.radius >= 0 && newY + player.radius <= screenHeight) {
            player.positionY = newY;
        }

        // Checks if colliding with stall
        if (isCollidingWithStall()) {
            player.positionX -= joystick.getActuatorX() * player.MAX_SPEED;
            player.positionY -= joystick.getActuatorY() * player.MAX_SPEED;
            if (buffer.isFoodReady()) {
                player.setHasFood(true);
            }
        }
    }

    private void playerCustomerLogic(){
        // Check if the new position is colliding with any table
        for (Table table : tables) {
            if (isCollidingWithTable(table)) {
                System.out.println(map.get(table.id));
                if (player.getHasFood() && map.get(table.id).size() > 0) {
                    int randomCustomerIndex = map.get(table.id).pop();
                    served++;
                    Table.removeCustomerFromTable(table, randomCustomerIndex); //remove the customer
                    player.setHasFood(false);
                }
                break; // No need to check other tables once collision is detected
            }
        }
    }

    // Method to check collision with the stall
    public boolean isCollidingWithStall() {
        // Calculate the half-width and half-height of the stall
        int halfWidth = stall.width / 2;

        // Calculate the closest point on the stall to the player's center
        double closestX = Math.max(Math.min(player.positionX, stall.centerX + halfWidth), stall.centerX - halfWidth);
        double closestY = Math.max(Math.min(player.positionY, stall.topY + stall.height), stall.topY);

        // Calculate the distance between the player's center and the closest point on the stall
        double distanceX = player.positionX - closestX;
        double distanceY = player.positionY - closestY;
        double distance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));

        // Check if the distance is less than or equal to the player's radius
        return distance <= player.radius;
    }

    public boolean isCollidingWithTable(Table table) {
        // Calculate the distance between the player's center and the table's center
        int adj = 50;
        double distanceX = player.positionX - table.centerX;
        double distanceY = player.positionY - table.centerY + adj;
        double distance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));

        // Check if the distance is less than or equal to the sum of player's radius and table's radius
        return distance <= (player.radius + table.radius + adj);
    }

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

    public boolean allCustomerServed(){
        return served == 9;
    }
}
