package com.example.androidapp.App_Objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.androidapp.R;

public class Stall {
    public int centerX;
    public int topY;
    public int width;
    public int height;
    private Paint paint;
    public Bitmap image;

    //Initialize stall and where it is
    public Stall(Context context, int centerScreenX, int topScreenY) {
        this.centerX = centerScreenX;
        this.topY = topScreenY;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.table, options);

        this.width = image.getWidth(); // Set the width of the stall
        this.height = image.getHeight() - 100; // Set the height of the stall
    }

    //Drawing my stall
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, centerX - (float) image.getWidth() / 2, topY, null);
    }
}
