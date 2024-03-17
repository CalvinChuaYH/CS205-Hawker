package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.androidapp.activity.GameActivity;
import com.example.androidapp.activity.LeaderboardActivity;
import com.example.androidapp.firebase.Firebase;

public class MainActivity extends AppCompatActivity {

    private Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        setContentView(R.layout.activity_title_screen);

        firebase = new Firebase();
    }

    public void startGame(View view)  {
        startActivity(new Intent(this, GameActivity.class));
    }

    public void viewLeaderboard(View view) {
        Intent intent = new Intent(this, LeaderboardActivity.class);
        intent.putExtra("firebase", firebase);
        startActivity(intent);
//        startActivity(new Intent(this, LeaderboardActivity.class));
    }
}