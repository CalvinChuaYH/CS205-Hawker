package com.example.androidapp.gamelogic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.androidapp.Game;
import com.example.androidapp.R;
import java.util.*;

public class Buffer {
    private Queue<Order> orders = new LinkedList<>();
    private int capacity;
    private Game game;

    public Buffer(Game game, int capacity) {
        this.game = game;
        this.capacity = capacity;
    }


    private boolean isFull = false;

    public synchronized void putOrder(Order order) {
        while (orders.size() >= capacity) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        orders.add(order);
        notifyAll();
    }

    public synchronized Order takeOrder() {
        while (orders.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        Order order = orders.remove();
        notifyAll();
        return order;
    }

    // Add a method to retrieve all orders
    public synchronized  Queue<Order> getOrders() {
        return new LinkedList<>(orders); // Return a copy to avoid modification outside the buffer
    }

    public synchronized boolean isFoodReady() {
        return isFull;
    }
}
