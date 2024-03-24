package com.example.androidapp.App_Objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Food {
    private Paint platePaint;
    private double centerScreenX;
    private double topScreenY;

    private double radius;

    public Food(double centerScreenX, double topScreenY, double radius) {
        this.centerScreenX = centerScreenX;
        this.topScreenY = topScreenY;
        this.radius = radius;

        platePaint = new Paint();
        platePaint.setColor(Color.WHITE);
        platePaint.setStyle(Paint.Style.FILL);
    }

    public void draw(Canvas canvas) {
        // Calculate plate dimensions based on radius
        float plateCenterX = (float) centerScreenX;
        float plateCenterY = (float) topScreenY;
        float plateRadius = (float) radius;

        // Draw plate circle
        canvas.drawCircle(plateCenterX, plateCenterY, plateRadius, platePaint);
    }
}
