package com.example.androidapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.androidapp.App_Objects.CollisionHandler;
import com.example.androidapp.App_Objects.Food;
import com.example.androidapp.App_Objects.Player;
import com.example.androidapp.App_Objects.Stall;
import com.example.androidapp.App_Objects.Table;
import com.example.androidapp.activity.LeaderboardActivity;
import com.example.androidapp.firebase.Firebase;
import com.example.androidapp.firebase.FirebaseManager;
import com.example.androidapp.gamelogic.Buffer;
import com.example.androidapp.gamelogic.Chef;
import com.example.androidapp.gamelogic.Waiter;
import com.example.androidapp.util.ThreadPool;

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private long start;
    private long currentTime;
    private AppCompatActivity activity;
    private CollisionHandler collisionHandler;
    private final Player player;
    private GameLoop gameLoop;
    private Joystick joystick;

    private Stall stall;
    private Food food;

    private Table[] tables;

    private ThreadPool threadPool;
    private static final int THREAD_COUNT = 3;

    private Chef chef;
    private Waiter waiter;
    Buffer buffer;

    public Game(Context context, AppCompatActivity activity) {
        super(context);
        this.activity = activity;
        // Initialize threadpool
        this.threadPool = ThreadPool.getInstance(THREAD_COUNT);

        // Get surface holder and add callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);
        start = System.currentTimeMillis()/1000L;

        // Initialize chef thread
        buffer = new Buffer(this);
        this.chef = new Chef(buffer);


        //initialize Objects

        // Calculate center of the screen
        int screenWidth = ScreenUtils.getScreenWidth(getContext());
        int centerScreenX = screenWidth / 2;
        int topScreenY = 0;

        // Place the stall at the top center of the screen
        tables = new Table[]{
                new Table(getContext(), 200, 400, 90, 1),
                new Table(getContext(), 1500, 700, 90, 2),
                new Table(getContext(), 800, 600, 90, 3)
        };
        stall = new Stall(getContext(), centerScreenX, topScreenY);
        food = new Food(getContext(), centerScreenX, topScreenY);
        joystick = new Joystick(2000, 700,70,40);
        player = new Player(getContext(), 500, 500, 30);
        collisionHandler = new CollisionHandler(player, stall, tables, buffer);

        this.waiter = new Waiter(buffer, collisionHandler);
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
        threadPool.execute(gameLoop);
        threadPool.execute(chef);
        threadPool.execute(waiter);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        gameLoop.setRunning(false);
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
        collisionHandler.draw(canvas, joystick);
        stall.draw(canvas);

        if (buffer.isFoodReady()) { // Check if the buffer says food is ready
            food.draw(canvas);
        }
    }

    //Show the Timer in game screen
    public void stopWatch(Canvas canvas){
        currentTime = (System.currentTimeMillis()/1000L - start);
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
        collisionHandler.update(joystick, screenWidth, screenHeight);
        checkStopGame();
    }

    public void checkStopGame(){
        //If served 9 etc can stop game with this...
        if (collisionHandler.allCustomerServed()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameLoop.setRunning(false);
                    showGameOverDialog();
                }
            });
        }
    }

    private void showGameOverDialog() {
        // Inflate the custom layout for the dialog
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.gameover, null);
        final EditText playerNameInput = dialogView.findViewById(R.id.playerNameInput);

        // Build and show the dialog
        new AlertDialog.Builder(activity)
                .setTitle("Game Over")
                .setView(dialogView)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Retrieve the input name and update the leaderboard
                        String playerName = playerNameInput.getText().toString();
                        updateLeaderboard(playerName);

                        // Optionally, navigate to the leaderboard screen
                        navigateToLeaderboard();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateLeaderboard(String playerName) {
        Firebase firebase = FirebaseManager.getInstance();
        firebase.setScore(playerName, (int) currentTime);
    }

    private void navigateToLeaderboard() {
        // Transition to the leaderboard activity
        Intent intent = new Intent(getContext(), LeaderboardActivity.class);
        getContext().startActivity(intent);
    }
}
