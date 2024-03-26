package com.example.androidapp.App_Objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.androidapp.R;

public class Food {
    private double centerScreenX;
    private double topScreenY;
    private Bitmap image;

    public Food(Context context, double centerScreenX, double topScreenY) {
        this.centerScreenX = centerScreenX;
        this.topScreenY = topScreenY + 20;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 16;
        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.food, options);
    }

    public void draw(Canvas canvas) {
        // Calculate plate dimensions based on radius
        float plateLeftX = (float) centerScreenX - (float) (image.getWidth() / 2);
        float plateCenterY = (float) topScreenY;

        canvas.drawBitmap(image, plateLeftX, plateCenterY, null);
    }
}
