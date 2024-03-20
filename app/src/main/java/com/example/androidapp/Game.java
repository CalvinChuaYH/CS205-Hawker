package com.example.androidapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private long start;
    private final Player player;
    private GameLoop gameLoop;
    private Joystick joystick;

    private Stall stall;

    private Table[] tables;

    public Game(Context context) {
        super(context);

        // Get surface holder and add callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);
        start = System.currentTimeMillis()/1000L;

        //initialize Objects

        // Calculate center of the screen
        int screenWidth = ScreenUtils.getScreenWidth(getContext());
        int centerScreenX = screenWidth / 2;
        int topScreenY = 0;

        // Place the stall at the top center of the screen
        tables = new Table[]{
                new Table(getContext(), 200, 200, 90, 1),
                new Table(getContext(), 1200, 400, 90, 2),
                new Table(getContext(), 600, 600, 90, 3)
        };
        stall = new Stall(getContext(), centerScreenX, topScreenY);
        joystick = new Joystick(2000, 700,70,40);
        player = new Player(getContext(), 500, 500, 30, stall, tables);

        setFocusable(true);
    }

    //Handles the on Screen touch especially for joystick
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event){
        //handle touch event actions
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(joystick.isPressed((double) event.getX(), (double) event.getY())) {
                    joystick.setIsPressed(true);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if(joystick.getIsPressed()){
                    joystick.setActuator((double) event.getX(), (double) event.getY());
                }
                return true;
            case MotionEvent.ACTION_UP:
                joystick.setIsPressed(false);
                joystick.resetActuator();
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    //Here you can come out with layout of the game etc items, person
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        stopWatch(canvas);
        for (Table table : tables) {
            table.draw(canvas);
        }
        joystick.draw(canvas);
        player.draw(canvas, joystick);
        stall.draw(canvas);
    }

    //Show the Timer in game screen
    public void stopWatch(Canvas canvas){
        long currentTime = (System.currentTimeMillis()/1000L - start);
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText((currentTime / 60) + " : " + (currentTime % 60) , 100, 100, paint);
    }

    //Responsible to handle updates, when you move joystick and player movements of the game.
    public void update() {
        int screenWidth = ScreenUtils.getScreenWidth(getContext());
        int screenHeight = ScreenUtils.getScreenHeight(getContext());
        joystick.update();
        player.update(joystick, screenWidth, screenHeight);
    }
}
