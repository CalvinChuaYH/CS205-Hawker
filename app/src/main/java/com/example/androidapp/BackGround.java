package com.example.androidapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;

import com.google.firebase.annotations.concurrent.Background;


public class BackGround {
    public Bitmap image;

    public BackGround(Context context){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 0;
        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.floor, options);
    }
    public void draw(Canvas canvas) {

        Paint paint = new Paint();
        ColorFilter filter = new PorterDuffColorFilter(Color.parseColor("#80000000"), PorterDuff.Mode.SRC_ATOP); // Orange color
        paint.setColorFilter(filter);

        // Get the dimensions of the canvas
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        // Calculate the scale factor to resize the image
        float scaleX = (float) canvasWidth / image.getWidth();
        float scaleY = (float) canvasHeight / image.getHeight();
        float scale = Math.max(scaleX, scaleY); // Use the larger scale factor

        // Calculate the scaled dimensions for the image
        int scaledWidth = Math.round(image.getWidth() * scale);
        int scaledHeight = Math.round(image.getHeight() * scale);

        // Calculate the position to center the image on the canvas
        int offsetX = (canvasWidth - scaledWidth) / 2;
        int offsetY = (canvasHeight - scaledHeight) / 2;

        // Draw the scaled image on the canvas
        canvas.drawBitmap(image, null, new Rect(offsetX, offsetY, offsetX + scaledWidth, offsetY + scaledHeight), paint);
    }
}
