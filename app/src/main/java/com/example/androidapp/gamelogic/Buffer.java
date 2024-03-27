package com.example.androidapp.gamelogic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.androidapp.Game;
import com.example.androidapp.R;

public class Buffer {
    private boolean isFull = false;

    public synchronized void putFood() {
        while (isFull) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}

        System.out.println("Chef prepared food");
        isFull = true;
        notifyAll();
    }

    public synchronized void takeFood() {
        while (!isFull) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        System.out.println("Waiter takes food");
        isFull = false;
        notifyAll();
    }

    public boolean isFoodReady() {
        return isFull;
    }
}
