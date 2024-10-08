package com.example.androidapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import com.example.androidapp.util.ThreadPool;
import com.google.firebase.annotations.concurrent.Background;

import java.util.Locale;

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private long start;
    private long currentTime;
    private long pauseTime;
    private AppCompatActivity activity;
    private BackGround backGround;
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
    Buffer buffer;

    public Game(AppCompatActivity activity) {
        super(activity);
        this.activity = activity;
        // Initialize threadpool
        this.threadPool = ThreadPool.getInstance(THREAD_COUNT);

        // Get surface holder and add callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);
        start = System.currentTimeMillis()/1000L;

        // Initialize chef thread
        buffer = new Buffer();
        this.chef = new Chef(buffer);


        //initialize Objects

        // Calculate center of the screen
        int screenWidth = ScreenUtils.getScreenWidth(activity);
        int centerScreenX = screenWidth / 2;
        int topScreenY = 0;

        backGround = new BackGround(getContext());

        // Place the stall at the top center of the screen
        tables = new Table[]{
                new Table(200, 400, 90, 1),
                new Table(1500, 700, 90, 2),
                new Table(800, 600, 90, 3)
        };
        stall = new Stall(getContext(), centerScreenX, topScreenY, buffer);
        food = new Food(getContext(), centerScreenX, topScreenY);
        joystick = new Joystick(2000, 700,70,40);
        player = new Player(activity, 500, 500, 30);
        collisionHandler = new CollisionHandler(getContext(),player, stall, tables);

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
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        // Stop the game loop and associated threads
        gameLoop.setRunning(false);
        threadPool.close(); // Properly shut down the thread pool
    }

    //Here you can come out with layout of the game etc items, person
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        backGround.draw(canvas);
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
        canvas.drawText(String.format(Locale.ENGLISH, "%d:%02d", (currentTime / 60), (currentTime % 60)), 100, 100, paint);
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
            activity.runOnUiThread(() -> {
                gameLoop.setRunning(false);
                pauseTime = currentTime;
                showGameOverDialog();
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
                .setCancelable(false)
                .setPositiveButton("Submit", (dialogInterface, i) -> {
                        // Retrieve the input name and update the leaderboard
                        String playerName = playerNameInput.getText().toString();
                        updateLeaderboard(playerName);

                        // Optionally, navigate to the leaderboard screen
                        navigateToLeaderboard();
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                            navigateToLeaderboard();
                        }
                )
                .show();
    }

    private void updateLeaderboard(String playerName) {
        Firebase firebase = FirebaseManager.getInstance();
        firebase.setScore(playerName, (int) pauseTime);
    }

    private void navigateToLeaderboard() {
        // Transition to the leaderboard activity
        Intent intent = new Intent(getContext(), LeaderboardActivity.class);
        getContext().startActivity(intent);
    }
}
