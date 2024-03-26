package com.example.androidapp.App_Objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.androidapp.R;

public class Food {
    private Paint platePaint;
    private double centerScreenX;
    private double topScreenY;

    private double radius;

    private Bitmap image;

    public Food(Context context, double centerScreenX, double topScreenY, double radius) {
        this.centerScreenX = centerScreenX;
        this.topScreenY = topScreenY + 70;
        this.radius = radius;

        platePaint = new Paint();
        platePaint.setColor(Color.rgb(255, 165, 0));
        platePaint.setStyle(Paint.Style.FILL);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.food, options);
    }

    public void draw(Canvas canvas) {
        // Calculate plate dimensions based on radius
        float plateCenterX = (float) centerScreenX;
        float plateCenterY = (float) topScreenY;
        float plateRadius = (float) radius;

        // Draw plate circle
        //canvas.drawCircle(plateCenterX, plateCenterY, plateRadius, platePaint);

        canvas.drawBitmap(image, (float) image.getWidth() / 2, plateCenterY, null);
    }
}
