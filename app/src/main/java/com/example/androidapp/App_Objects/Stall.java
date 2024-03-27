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
    private Paint boxPaint;
    public Bitmap image;
    public Bitmap stallImage;

    //Initialize stall and where it is
    public Stall(Context context, int centerScreenX, int topScreenY) {
        this.centerX = centerScreenX;
        this.topY = topScreenY;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.table, options);
        stallImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.stall, options);

        this.width = image.getWidth(); // Set the width of the stall
        this.height = image.getHeight() - 100; // Set the height of the stall
        paint = new Paint();
        paint.setColor(Color.WHITE); // Text color
        paint.setTextSize(50); // Text size
        paint.setAntiAlias(true);

        boxPaint = new Paint();
        boxPaint.setColor(Color.WHITE); // Box color
        boxPaint.setStyle(Paint.Style.FILL); // Fill the box
        boxPaint.setAlpha(150); // Semi-transparent
    }

    //Drawing my stall
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, centerX - (float) image.getWidth() / 2, topY, null);
        canvas.drawBitmap(stallImage, centerX + (float) image.getWidth() / 2, topY, null);

        // Prepare text to draw
        String textToDraw = "Carrot Cake";
        float textWidth = paint.measureText(textToDraw);



        // Coordinates for the text box (adjust as needed)
        float textBoxLeft = centerX + (float) image.getWidth() + 90;
        float textBoxTop = topY; //+ stallImage.getHeight() + 20; // 20px below the stall image
        float textBoxRight = textBoxLeft + 285;
        float textBoxBottom = textBoxTop + 100; // 100px tall box

        float textX = textBoxLeft + 10;
        float textY = textBoxTop + 75; // Center text vertically in the box

        // Draw the text box
        canvas.drawRect(textBoxLeft, textBoxTop, textBoxRight, textBoxBottom, boxPaint);

        // Draw the text
        canvas.drawText(textToDraw, textX, textY, paint);
    }
}
